import java.io.*;
import java.util.*;
import java.io.File;


public class Players
{
    private List<Player> allPlayers;

    public Players()
    {
        allPlayers = new ArrayList<>();
        File file = new File("playerStats.txt");
//        File file = new File("playerStats2.txt");


        try(Scanner scanner = new Scanner(file))
        {
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim(); // where scanned item is and clears whitespace
                if (line.isEmpty())
                {
                    continue;
                }
                // create a new sub scanner for the line then iterate through and store all values
                Scanner lineScanner = new Scanner(line);
                String username = lineScanner.next();

                //tokensize the input
                int bulls = lineScanner.nextInt();
                int cows = lineScanner.nextInt();
                int codesAttempted = lineScanner.nextInt();
                int codesDeciphered = lineScanner.nextInt();
                int totalGuesses = lineScanner.nextInt();

                // load values into player
                Player player = new Player(username);
                player.updateBulls(bulls);
                player.updateCows(cows);
                player.codesAttempted = codesAttempted;
                player.codesDeciphered = codesDeciphered;
                player.totalGuess = totalGuesses;


                allPlayers.add(player);
                lineScanner.close();;
            }
        }
        catch(FileNotFoundException e)
        { // better exception catch plz
            System.out.println("Couldn't read from file " + e);
        }

    }

    public void addPlayer(Player p) {
        allPlayers.add(p);
    }

    public void savePlayers() {

        // Using printwriter as it is easier to write formatted files
        try (PrintWriter pw = new PrintWriter("playerStats.txt"); ) {
            for(Player p : allPlayers) {
                pw.println(p.username + " " + p.getBulls() + " " + p.getCows() + " " + p.getCodesAttempted() + " " + p.getCodesDeciphered() + " " + p.getTotalGuess());
            }
            System.out.println("Printed to \"playerStats.txt\" succesfully.");
        }
        catch(Exception e) {
            System.out.println("Couldn't write to file. Exception: " + e);
        }

    }

    public Player findPlayer(String username){
        for(Player player: allPlayers){
            if(player.username.equals(username)) {
                return player;
            }
        }
        return null;

    }
    public List<Player> getPlayers()
    {
        return allPlayers;
    }

    public static void sortPlayersByCodesDeciphered(List<Player> players)
    {
        Collections.sort(players, new Comparator<Player>()
        {
            @Override
            public int compare(Player p1, Player p2)
            {
                return Integer.compare(p2.getCodesDeciphered(), p1.getCodesDeciphered());
            }
        });
    }

//    getAllPlayersBulls() {
//
//    }
//
//    getAllPlayersCows(){
//
//    }
//
//    getAllPlayerSecretCodesAttempted(){
//
//    }
//
//    getAllPlayersSecretCodesDeciphered() {
//    }
}


