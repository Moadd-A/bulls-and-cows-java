import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class GameTests {
    //tests for acceptance criteria

    @Test
    void testRequestSecretCode() {
        String testInput = "exit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        SecretCode testSecretCode;
        testSecretCode = game.requestCode(game.currentType, testPlayer); // request numbers code

        assertEquals(8, testSecretCode.decipheredCode.length()); // The code is 4 long
        assertTrue(testSecretCode.decipheredCode.matches("\\d{8}")); // The code is only digits
    }
    @Test
    void RequestLetterCode(){
        String testInput = "exit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "letters"); // game type

        SecretCode testSecretCode;
        testSecretCode = game.requestCode(game.currentType, testPlayer); // request letters code

        assertEquals(8, testSecretCode.decipheredCode.length());
        System.out.println(testSecretCode.decipheredCode);
        assertTrue(testSecretCode.decipheredCode.matches("[a-zA-Z]{8}"));
    }
    @Test
    void RequestLetterNoFile(){
        //test for if the words file does not exist
    }


    @Test
    void testEnterCode(){
        String testInput = "1235\n7658\nexit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        int bull1 = testPlayer.getGameBulls(); // variables to store things from first game/guesses
        int cow1 = testPlayer.getGameCows();
        int gameattempt1 = (testPlayer.getCodesAttempted());
        int guess1 = (testPlayer.getTotalGuess());

        String testInput2 = "12356789\n09431245\nexit\n"; //simulates terminal input
        InputStream inputStream2 = new ByteArrayInputStream(testInput2.getBytes());
        System.setIn(inputStream2);

        Game game2 = new Game(testPlayer, "numbers"); // game type

        int bull2 = testPlayer.getGameBulls(); // variables to store things from second game/guess
        int cow2 = testPlayer.getGameCows();
        int gameattempt2 = (testPlayer.getCodesAttempted());
        int guess2 = (testPlayer.getTotalGuess());

        assertTrue(gameattempt1 < gameattempt2); // attempt stat updates
        assertTrue(guess1 < guess2); // guesses stat updates
        assertTrue(bull1 != 0 || bull2 != 0 || cow1 != 0 || cow2 != 0);
        //there will be at least a bull or a cow displayed from the guesses and is correctly displayed
    }

    @Test
    void correctGuess(){
        String testInput = "Backfire\n" +
                "comrades\n" +
                "overcast\n" +
                "centauri\n" +
                "sunlight\n" +
                "manicure\n" +
                "hospital\n" +
                "paintbox\n" +
                "stamping\n" +
                "chivalry\n" +
                "outmarch\n" +
                "waterfox\n" +
                "campfire\n" +
                "postcard\n" +
                "bachelor\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));


        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "letters"); // game type

        String expectedOutput = "Congratulations! You guessed the code.";
        assertTrue(outputStream.toString().contains(expectedOutput)); // congrats text is displayed
        assertTrue(testPlayer.getCodesDeciphered() > 0); // codes deciphered is updated

    }

    @Test
    void invalidLengthGuess(){
        String testInput = "123456\nexit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        String expectedOutput = "Please enter a guess of correct length 8";
        assertTrue(outputStream.toString().contains(expectedOutput)); // incorect length text is displayed

    }

    @Test
    void invalidLGuess(){
        String testInput = "qwe1awda\nexit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "letters"); // game type

        String expectedOutput = "Guess includes both numbers and letters, please enter code for valid game type.";
        assertTrue(outputStream.toString().contains(expectedOutput)); // incorect length text is displayed
    }
    @Test
    void invalidNGuess(){
        String testInput = "123aeiou\nexit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        String expectedOutput = "Guess includes both numbers and letters, please enter code for valid game type.";
        assertTrue(outputStream.toString().contains(expectedOutput)); // incorect length text is displayed
    }


    @Test
    void testSaveCode(){
        String testInput = "bean\nsave game\ny\n"; //simulates terminal input new game, guess bean, save game, y
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "letters"); // game type

        File f = new File("TestPlayerSave.txt");

        assertTrue(f.exists());
        f.delete(); // delete file after it is checked if it exists
    }
    @Test
    void testOverwriteSaveCode(){
        int count1 = 0; // to count the number of lines in file to prove overwrite has happened
        int count2 = 0;

        String testInput = "bean\nsave game\ny\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "letters"); // game type
        // first save a letter game with guess 'bean'
        try {
            File f = new File("TestPlayerSave.txt");
            Scanner read = new Scanner(f);
            while (read.hasNextLine()) {
                read.nextLine();
                count1 ++; // increment count 1 for each line in file
            }
            read.close();
        }catch (FileNotFoundException e) {
            System.out.println("error with file");
        }

        String testInput2 = "12345678\n56781234\n13572468\nsave game\ny\n"; //simulates terminal input
        InputStream inputStream2 = new ByteArrayInputStream(testInput2.getBytes());
        System.setIn(inputStream2);

        Game game2 = new Game(testPlayer, "numbers"); // game type
        // then save a game with number guesses, overwrite the save so only number guesses are saved
        try {
            File f = new File("TestPlayerSave.txt");
            Scanner read = new Scanner(f);
            while (read.hasNextLine()) {
                read.nextLine();
                count2 ++; // increment count 2 for each line in file
            }
            read.close();
        }catch (FileNotFoundException e) {
            System.out.println("error with file");
        }

        assertTrue(count2 > count1);
        File f = new File("TestPlayerSave.txt");
        f.delete();
    }



    @Test
    void testLoadGame() {
        String testInput = "12345678\nsave game\ny\n"; //simulates terminal input
        //new game, guess 1234, save game, y
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        File f = new File("TestPlayerSave.txt");

        game.loadGame(testPlayer);
        String expectedOutput =  "Guess | Bulls | Cows"; // line of the loaded game's information
        assertTrue(outputStream.toString().contains(expectedOutput));
        f.delete();
    }

    @Test
    void testLoadGameNoSave(){ // I CANNOT MAKE IT ATTEMPT TO LOAD GAME PROPERLY THIS HAS THE SAME EFFECT AS NO FILE FOUND
        String testInput = "exit\n"; //simulates terminal input
        //new game, guess 1234, save game, y
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        game.loadGame(testPlayer);
        String expectedOutput =  "Couldn't read from file"; // file could not be found for now
        assertTrue(outputStream.toString().contains(expectedOutput));

    }

    @Test
    void testLoadGameBadFile() {
        String testInput = "load game\nexit\n"; //simulates terminal input
        //new game, guess 1234, save game, y
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("notReal"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        File f = new File("notReal.txt");

        game.loadGame(testPlayer);
        String expectedOutput = "Couldn't read from file"; // username cant be found message
        assertTrue(outputStream.toString().contains(expectedOutput));
    }




    @Test
    void testShowSolution() { // SPRINT 3 TEST
        String testInput = "show solution\nexit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        SecretCode testSecretCode = new NumbersCode();
        testSecretCode.decipheredCode = "1234"; //test secret code

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        game.showSolution(testSecretCode); //calls show solution with test code

        String expectedOutput = "The code is 1234";
        assertTrue(outputStream.toString().contains(expectedOutput));
    }

    @Test
    void testTrackCodeDeciphered(){
        String testInput = "backfire\n" +
                "comrades\n" +
                "overcast\n" +
                "centauri\n" +
                "sunlight\n" +
                "manicure\n" +
                "hospital\n" +
                "paintbox\n" +
                "stamping\n" +
                "chivalry\n" +
                "outmarch\n" +
                "waterfox\n" +
                "campfire\n" +
                "postcard\n" +
                "bachelor\n" +
                "exit\n";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));


        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "letters"); // game type
        String code = game.currentCode;
        int codeD1 = testPlayer.codesDeciphered;

        String testInput2 = "Backfire\n" +
                "comrades\n" +
                "overcast\n" +
                "centauri\n" +
                "sunlight\n" +
                "manicure\n" +
                "hospital\n" +
                "paintbox\n" +
                "stamping\n" +
                "chivalry\n" +
                "outmarch\n" +
                "waterfox\n" +
                "campfire\n" +
                "postcard\n" +
                "bachelor\n" +
                "exit\n";
        InputStream inputStream2 = new ByteArrayInputStream(testInput2.getBytes());
        System.setIn(inputStream2);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream2));

        Game game2 = new Game(testPlayer, "letters"); // game type
        code = game2.currentCode;
        int codeD2 = testPlayer.codesDeciphered;

        assertTrue(codeD2 > codeD1);
    }


    @Test
    void testTrackNotYetDeciphered(){
        String testInput = "12345678\n87654321\nexit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        int bull1 = testPlayer.getGameBulls(); // variables to store things from first game/guesses
        int cow1 = testPlayer.getGameCows();
        int gameattempt1 = (testPlayer.getCodesAttempted());
        int guess1 = (testPlayer.getTotalGuess());
        int deciphered = testPlayer.codesDeciphered;

        String testInput2 = "13572468\n24681357\nexit\n"; //simulates terminal input
        InputStream inputStream2 = new ByteArrayInputStream(testInput2.getBytes());
        System.setIn(inputStream2);

        Game game2 = new Game(testPlayer, "numbers"); // game type

        int bull2 = testPlayer.getGameBulls(); // variables to store things from second game/guess
        int cow2 = testPlayer.getGameCows();
        int gameattempt2 = (testPlayer.getCodesAttempted());
        int guess2 = (testPlayer.getTotalGuess());
        int deciphered2 = testPlayer.codesDeciphered;

        assertTrue(gameattempt1 < gameattempt2); // attempt stat updates
        assertTrue(guess1 < guess2); // guesses stat updates
        assertTrue(bull1 != 0 || bull2 != 0 || cow1 != 0 || cow2 != 0);
        assertEquals(deciphered2, deciphered); // deciphered is not updated
        //there will be at least a bull or a cow displayed from the guesses and is correctly displayed
    }


    @Test
    void testTrackCodeAttempts() {
        String testInput = "1235\n7658\nexit\n"; //simulates terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        int attempts1 = testPlayer.getCodesAttempted();

        String testInput2 = "1235\n0943\nexit\n"; //simulates terminal input
        InputStream inputStream2 = new ByteArrayInputStream(testInput2.getBytes());
        System.setIn(inputStream2);

        Game game2 = new Game(testPlayer, "numbers"); // game type

        int attempts2 = testPlayer.getCodesAttempted();

        assertTrue(attempts1 < attempts2); // attempts 2 is more than 1
    }

    @Test
    void testLoadDontIncAttempt(){
        String testInput = "12345678\nsave game\ny\n"; //simulates terminal input
        //new game, guess 1234, save game, y
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        int attempts1 = testPlayer.getCodesAttempted();

        File f = new File("TestPlayerSave.txt");

        game.loadGame(testPlayer); // load the game

        int attempts2 = testPlayer.getCodesAttempted();

        assertEquals(attempts1 ,attempts2); // attempts before and after load are the same
        f.delete();
    }

    @Test
    void testNoBullsCows()
    {
        String testInput = "exit\n"; // terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        // check if we're incrementing
        assertTrue(testPlayer.getGameBulls() == 0 || testPlayer.getGameCows() == 0);
        assertTrue(testPlayer.getBulls() == 0 || testPlayer.getCows() == 0);
    }
    @Test
    void testTrackBullsCows()
    {
        String testInput = "12345678\n56781234\n87690321\nexit\n"; // terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer2"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        // check if we're incrementing
        assertTrue(testPlayer.getGameBulls() > 0 || testPlayer.getGameCows() > 0, String.valueOf(testPlayer.getBulls()));
        assertTrue(testPlayer.getBulls() > 0 || testPlayer.getCows() > 0);
    }

    @Test
    void testPlayStats()
    {
        String testInput = "12345678\n56781234\n13572490\nexit\n"; // terminal input
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "numbers"); // game type

        assertTrue(testPlayer.getCodesAttempted() > 0); // codes attempted should increment
        assertTrue(testPlayer.getTotalGuess() > 0); // total guesses should increment
        assertTrue(testPlayer.getBulls() > 0 || testPlayer.getCows() > 0); // bulls or cows should increment
    }

    @Test
    void testLoadDetails()
    {
        String saveInput = "12345678\nsave game\ny\n"; // terminal input
        InputStream saveInputStream = new ByteArrayInputStream(saveInput.getBytes());
        System.setIn(saveInputStream);

        Player testPlayer = new Player("TestPlayer"); // test player
        Game saveGame = new Game(testPlayer, "numbers"); // game type

        String loadInput = "load game\nexit\n";
        InputStream loadInputStream = new ByteArrayInputStream(loadInput.getBytes());
        System.setIn(loadInputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Game loadGame = new Game(testPlayer, "load");

        String expectedOutput = "Guess | Bulls | Cows";
        assertTrue(outputStream.toString().contains(expectedOutput));
    }

    @Test
    void testTopScores() // SPRINT 3 TEST
    {
        Players players = new Players();

        Player player1 = new Player("Player1");
        player1.codesDeciphered = 5;
        players.addPlayer(player1);

        Player player2 = new Player("Player2");
        player2.codesDeciphered = 3;
        players.addPlayer(player2);

        Player player3 = new Player("Player3");
        player3.codesDeciphered = 7;
        players.addPlayer(player3);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Game.displayLeaderboard(players);

        String expectedOutput = "Player3"; // player3 should be at the top with 7 codes deciphered
        assertTrue(outputStream.toString().contains(expectedOutput), "Expected Player3 to be at the top of the leaderboard.");

        //this test was such a pain
    }

    @Test
    void testTopScoresNoPlayers() throws IOException // no more jank commenting and uncommenting required!
    {
        // backup original playerStats.txt
        Path statsFile = Paths.get("playerStats.txt");
        Path backupFile = Paths.get("playerStats2.txt");

        // check if the og file exists. if not make a blank one
        if (!Files.exists(statsFile))
        {
            Files.createFile(statsFile);
        }

        // copy to a backup
        Files.copy(statsFile, backupFile, StandardCopyOption.REPLACE_EXISTING);

        try
        {
            // reinitalise playerStats.txt
            Files.write(statsFile, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);

            // actual test code
            Players players = new Players();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            Game.displayLeaderboard(players);

            String expectedOutput = "No players found!";
            assertTrue(outputStream.toString().contains(expectedOutput),
                    "Expected 'No players found!' message");

        }
        finally
        {
            // restore the file
            Files.copy(backupFile, statsFile, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(backupFile);
        }
    }

    @Test
    void testGetHint() { // SPRINT 3 TEST
        String testInput = "12345678\nhint\nhint\nhint\nhint\nhint\nhint\nhint\nhint\nhint\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Player testPlayer = new Player("TestPlayer"); // test player
        Game game = new Game(testPlayer, "debug"); // debug game type

        // Extract output and verify
        String output = outputStream.toString();
        for (int i = 0; i < game.currentCode.length(); i++) {
            String expectedOutput = "At position " + i + " the value is " + game.currentCode.charAt(i);
            assertTrue(output.contains(expectedOutput), "Hint output mismatch at position " + i + ":\n" + output);
        }

        assertTrue(output.contains("No hints available, all hints have been given"),
                "Expected exhaustion message missing:\n" + output);
    }

}
