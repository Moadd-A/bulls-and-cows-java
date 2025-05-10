import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.*;

public class Game
{
    private Scanner scan;
//    private HashMap<Player, ArrayList<ArrayList<String>>> playerGameMapping; //mapping from player to saved game (eg past guesses)
    private Player currentPlayer;
    private Players currentPlayers;

    private ArrayList<String> playerGuesses;
    private ArrayList<Integer> playerBulls;
    private ArrayList<Integer> playerCows;
    public String currentCode;
    public String currentType;
    private boolean loadFlag;
    private int hintCounter; //increment for every hint???

    public Game(Player p, String gameType)
    {
        scan = new Scanner(System.in);
        currentPlayer = p;
        playerGuesses = new ArrayList<>();
        playerBulls = new ArrayList<>();
        playerCows = new ArrayList<>();
        currentType = gameType;
        loadFlag = false;
        hintCounter = 0;
//        playerGameMapping = new HashMap<>();

        playGame(gameType);
    }

    public void getHint() {

        if (playerGuesses.isEmpty()) {
            System.out.println("No guesses have been made, please enter a guess");
            return;
        }

        List<Integer> incorrectPositions = new ArrayList<>();
        String lastGuess = playerGuesses.get(playerGuesses.size() - 1);

        for (int i = 0; i < currentCode.length(); i++) {
            if (lastGuess.charAt(i) != currentCode.charAt(i)) {
                incorrectPositions.add(i);
            }
        }

        if (hintCounter >= 8) {
            System.out.println("No hints available, all hints have been given");
        } else {
            // Use modulo to prevent out-of-bounds access
            int hintPosition = incorrectPositions.get(hintCounter % incorrectPositions.size());
            System.out.println("At position " + hintPosition + " the value is " + currentCode.charAt(hintPosition));
        }

        hintCounter++;
    }


    /** Loads a player into the game, returns true if successful, false if player not found */
//    public boolean loadPlayer() {}

    /** contains the code for the main game loop. this handles getting an input, comparing that input to the solution and return feedback based on that input*/
    public void playGame(String gameType) //code type is either "numbers" or "letters"
    {
        SecretCode code;
        if(gameType.equals("numbers") || gameType.equals("letters"))
        {
            code = requestCode(gameType, currentPlayer); // Generate the code
            currentCode = code.decipheredCode;
            if(code.decipheredCode == null)  //error handling
            {
                return;
            }
        }
        else if (gameType.equals("debug"))
        {
            code = new NumbersCode();
            code.decipheredCode = "56781234";
            this.currentCode = "56781234";
        }
        else
        {
            code = loadGame(currentPlayer);
        }

        String input;
        while (true)
        {
            input = enterGuess(code); // Get user input

            if (input.equals("exit"))
            {
                System.out.println("Exiting to main menu!");
                System.out.println("------------------------------");
                break; // Exit the loop if the user wants to quit
            }

            else if(input.equals("save game")){
                saveGame();
                System.out.println("Saved " + currentPlayer.username + "'s game!");
                System.out.println("Exiting to main menu!");
                System.out.println("------------------------------");
                break; // Exit the game loop
            }
            else if (input.equals("code game mismatch"))
            {
                System.out.println("Guess includes both numbers and letters, please enter code for valid game type.");
            }
            else if (input.equals("invalid duplicate"))
            {
                System.out.println("Guess not valid, please enter a non-duplicate code");
            }
            else if (input.equals("wrong amount"))
            {
                System.out.println("Please enter a guess of correct length 8");
            }
            else if (input.equals("get hint"))
            {
                getHint();
            }
            else if(input.equals("show solution"))
            {
                showSolution(code);
                break;
            }
            else if (input.equals(code.decipheredCode))
            {
                System.out.println("Congratulations! You guessed the code.");
                currentPlayer.incrementCodesDeciphered();
                if (loadFlag)
                {
                    File file = new File(currentPlayer.username + "Save.txt");
                    if (file.delete())
                    {
                        System.out.println("Deleted " + currentPlayer.username + "'s save file");
                    }
                    else
                    {
                        System.out.println("Could not delete " + currentPlayer.username + "'s save file");
                    }
                }
                break; // Exit the loop if the guess is correct
            }
            else
            {
                // Feedback based on user input (returns Bulls and Cows)

                System.out.println("Bulls: " + currentPlayer.getGameBulls());
                System.out.println("Cows: " + currentPlayer.getGameCows());
                System.out.println("------------------------------");
            }
        }
    }


    public SecretCode requestCode(String gameType, Player p)
    {
        p.incrementCodesAttempted(); //record another game on the players record

        if(gameType.equals("numbers"))
        {
            SecretCode numCode = new NumbersCode();
            numCode.genCode();
            return numCode;
        }
        else
        {
            SecretCode letterCode = new LettersCode();
            letterCode.genCode();
            return letterCode;
        }
    }

    public String enterGuess(SecretCode code)
    {
        System.out.println("Type exit to stop the game and return to the main menu.");
        System.out.println("Type hint to get a hint");
        System.out.println("Type show solution to forfeit the game");
        System.out.println("Enter your guess: ");

        String userGuess =  scan.nextLine();

        if (userGuess.equals("exit")) //special case for exiting an in progress game
        {
            return "exit";
        }
        if (userGuess.equals("save game"))
        {
            return "save game";
        }
        if (userGuess.equals("show solution"))
        {
            return "show solution";
        }
        if (userGuess.equals("hint"))
        {
            return "get hint";
        }
        if(userGuess.length() != currentCode.length())
        {
            return "wrong amount";
        }
        currentPlayer.updateGameBulls(0);
        currentPlayer.updateGameCows(0);

        int gameType;
        if (Character.isDigit(code.decipheredCode.charAt(0)))
        {
            gameType = 0; // 0 is numbers game
        }
        else
        {
            gameType = 1; // 1 is letters game
        }

        // Checking if we're playing letters or numbers game
        for(int i = 0; i < userGuess.length(); i++)
        {
            if(Character.isDigit(userGuess.charAt(i)) && gameType == 1)
            {
                return ("code game mismatch");
            }
            else if (!Character.isDigit(userGuess.charAt(i)) && gameType == 0)
            {
                return ("code game mismatch");
            }
        }

        // Increment
        boolean cowFlag = false;
        boolean bullFlag = false;
        for(int i = 0; i < code.decipheredCode.length(); i++)
        {
            cowFlag = false;
            bullFlag = false;
            for(int j = 0; j < userGuess.length(); j++)
            {
                if(userGuess.charAt(i) == userGuess.charAt(j) && i != j)
                {
                    return ("invalid duplicate");
                }
                if (code.decipheredCode.charAt(i) == userGuess.charAt(j))
                {
                    if (i == j)
                    {
                        currentPlayer.updateGameBulls(currentPlayer.getGameBulls() + 1);
                        bullFlag = true;
                    }
                    else
                    {
                        currentPlayer.updateGameCows(currentPlayer.getGameCows() + 1);
                        cowFlag = true;
                    }
                }
            }
        }

        if (cowFlag){currentPlayer.updateBulls(currentPlayer.getBulls() + 1);}
        if (bullFlag){currentPlayer.updateCows(currentPlayer.getCows() + 1);}
        playerGuesses.add(userGuess);
        playerBulls.add(currentPlayer.getGameBulls());
        playerCows.add(currentPlayer.getGameCows());
        currentPlayer.updateTotalGuess();

        return userGuess;
    }

    public void saveGame()
    {
        File file = new File(currentPlayer.username + "Save.txt");
        if (file != null)
        {
            System.out.println("Save already exists - would you like to overwrite? (Y/N)");
            boolean requiresInput = true;
            while(requiresInput)
            {
                String response = scan.next();
                if(response.equals("N") || response.equals("n"))
                {
                    // anything else we need to do here? does this take them back to the right place

                    // im just a single line comment
                    return;
                }
                else if(!(response.equals("Y") || response.equals("y")))
                {
                    System.out.println("Not a valid input - please enter 'Y' or 'N'");
                }
                else
                {
                    requiresInput = false;
                }
            }
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(currentPlayer.username + "Save.txt", false)))
        { // using player name so each player has their own unique save file
            pw.println(currentCode);
            for(int i = 0; i < playerGuesses.size(); i++)
            {
                if(i == playerGuesses.size() -1)
                {
                    pw.print(playerGuesses.get(i) + " " + playerBulls.get(i) + " " + playerCows.get(i));
                }
                else
                {
                    pw.println(playerGuesses.get(i) + " " + playerBulls.get(i) + " " + playerCows.get(i));
                }
            }
//            pw.println("end");
            System.out.println("Printed to " + currentPlayer.username + "\"Save.txt\" succesfully.");
        }
        catch(Exception e)
        {
            System.out.println("Couldn't write to file. Exception: " + e);
        }
    }

    public SecretCode loadGame(Player p)
    {
        File file = new File(currentPlayer.username + "Save.txt");

        // Loading in the playerGames file and searching for the username of current player
        try(Scanner scanner = new Scanner(file))
        {

            String gameCode = scanner.next(); // game code is the first String held in the playerGames.txt based on this we are making a new secret code
            SecretCode code;
            if (Character.isDigit(gameCode.charAt(0)))
            {
                code = new NumbersCode();
            }
            else {
                code = new LettersCode();
            }
            code.decipheredCode = gameCode;
            this.currentCode = code.decipheredCode;
            System.out.println("Guess | Bulls | Cows");
            while(scanner.hasNextLine())
            {
                System.out.println("" + scanner.next() + "      " + scanner.nextInt() + "      " + scanner.nextInt());
            }
            System.out.println("--------------------");
            loadFlag = true;
            return code;
        }
        catch(FileNotFoundException e)
        { // better exception catch plz
            System.out.println("Couldn't read from file " + e);
        }
        return null;
    }

    public void showSolution(SecretCode code)
    {
        System.out.println("The code is " + code.decipheredCode);
    }

    public static void main(String[] args)
    {
        Players players = new Players(); //calling this in game constructer for use
        boolean gameLoop = true;
        while (gameLoop)
        {
            Scanner scan = new Scanner(System.in); //initialise scanner for input
            System.out.println("Welcome to Bulls and Cows");
            System.out.println("-------------------------");
            System.out.print("" +
                    "    ^__^                  \n" +
                    "    (oo)\\_______         \n" +
                    "    (__)\\       )\\/\\   \n" +
                    "         ||----w  |       \n" +
                    "         ||      ||       \n"
            );
            System.out.println("What would you like to do?");
            System.out.println("Type 'new game' to start a new game! (Default: Numbers game)");
            System.out.println("If you want to start a new letters game, type 'new letters game'");
            System.out.println("If you want to load a saved game, type 'load game'");
            System.out.println("If you want to get your player stats, type 'get stats'");
            System.out.println("If you want to view the top ten players, type 'leaderboard'");
            System.out.println("To exit, type exit.");
            String userInput = scan.nextLine();
            Game game;
            switch (userInput)
            {
                case "new game": // new numbers game
                    System.out.println("What is your username?");
                    String name = scan.nextLine();
                    Player p = players.findPlayer(name); // Sets p to the player of that username if they exist - if not, is null
                    if (p == null)
                    { // If player existed
                        p = new Player(name);
                        players.addPlayer(p);
                    }
                    game = new Game(p, "numbers");
                    break;

                case "new letters game":
                    System.out.println("What is your username?");
                    String lettersName = scan.nextLine();
                    Player lettersP = players.findPlayer(lettersName); // Sets p to the player of that username if they exist - if not, is null
                    if (lettersP == null)
                    { // If player existed
                        lettersP = new Player(lettersName);
                        players.addPlayer(lettersP);
                    }
                        // else  player does not exist we feed in the inputted name from the user to new player
                    game = new Game(lettersP, "letters");
                    break;

                case "load game":
                    System.out.println("What is your username?");
                    String playerName = scan.nextLine();
                    Player gameP = players.findPlayer(playerName);
                    if (gameP == null)
                    {
                        System.out.println("Couldn't find player " + playerName);
                        continue;
                    }
                    game = new Game(gameP, "load");
                    break;

                case "get stats":
                    System.out.println("What is your username?");
                    String gameName = scan.nextLine();
                    Player gameL = players.findPlayer(gameName);
                    if (gameL == null)
                    {
                        System.out.println("Couldn't find player " + gameName);
                        continue;
                    }
                    Player L = players.findPlayer(gameName);
                    System.out.println("Codes attempted: " + L.getCodesAttempted());
                    System.out.println("Codes deciphered: " + L.getCodesDeciphered());
                    System.out.println("Total bulls: " + L.getBulls());
                    System.out.println("Total cows: " + L.getCows());
                    float fbulls = (float) L.getBulls();
                    float fcows = (float) L.getCows();
                    float fguess = (float) gameL.getTotalGuess();
                    System.out.println("bulls as percentage of guesses: " + Math.round((fbulls / fguess) * 100) + "%");
                    System.out.println("cows as percentage of guesses: " + Math.round((fcows / fguess) * 100) + "%\n");
                    continue;

                case "leaderboard":
                    displayLeaderboard(players);
                    break;

                case "exit":
                    gameLoop = false;
                    break;

                default:
                    System.out.println("Please enter a valid option");
            }
            players.savePlayers();
        }
    }
    public static void displayLeaderboard(Players players)
    {
        List<Player> pList = players.getPlayers(); //get the list of players
        if(!pList.isEmpty())
        {
            Players.sortPlayersByCodesDeciphered(pList); //sort the list
            int leadercount = 0; //to make sure we only print top 10 we use this variable
            for(Player player : pList) //start hte loop
            {
                leadercount++; //increment
                if(leadercount > 10) //if its greater than 10 stop displaying
                {
                    break;
                }
                System.out.println(player.getUsername()); //print everything identical to get stats
                System.out.println("Codes attempted: " + player.getCodesAttempted());
                System.out.println("Codes deciphered: " + player.getCodesDeciphered());
                System.out.println("Total bulls: " + player.getBulls());
                System.out.println("Total cows: " + player.getCows());
                float lbulls = (float) player.getBulls();
                float lcows = (float) player.getCows();
                float lguess = (float) player.getTotalGuess();
                System.out.println("bulls as percentage of guesses: " + Math.round((lbulls / lguess) * 100) + "%");
                System.out.println("cows as percentage of guesses: " + Math.round((lcows / lguess) * 100) + "%\n");
                System.out.println("------------------------------------"); //print a boarder
            }
        }
        else
        {
            System.out.print("No players found!");
        }
    }
}