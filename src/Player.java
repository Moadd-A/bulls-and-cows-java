public class Player {
    public String username;
    private int numberOfBulls;
    private int numberOfCows;
    public int codesAttempted;
    public int codesDeciphered;
    public int gameBulls;
    public int gameCows;
    public int totalGuess;

    public Player(String username) {
        this.username = username;
        numberOfBulls = 0;
        numberOfCows = 0;
        codesAttempted = 0;
        codesDeciphered = 0;
        gameBulls = 0;
        gameCows = 0;
        totalGuess = 0;
    }

    public void updateBulls(int numberOfBulls){
        this.numberOfBulls = numberOfBulls;
    }

    public void updateCows(int numberOfCows){
        this.numberOfCows = numberOfCows;
    }

    public void updateGameBulls(int numberOfBulls){
        this.gameBulls = numberOfBulls;
    }

    public void updateGameCows(int numberOfCows){
        this.gameCows = numberOfCows;
    }

    public void incrementCodesAttempted(){
        this.codesAttempted++;
    }

    public void incrementCodesDeciphered(){
        this.codesDeciphered++;
    }

    public int getBulls(){
        return this.numberOfBulls;
    }

    public int getCows(){
        return this.numberOfCows;
    }

    public int getGameBulls() {return this.gameBulls;}

    public int getGameCows() {return this.gameCows;}

    public int getCodesAttempted(){
        return this.codesAttempted;
    }

    public int getCodesDeciphered(){
        return this.codesDeciphered;
    }

    public String getUsername()
    {
        return this.username;
    }
    public String getScores() //not used hrmmm
    {
        return
        (
            username + "\n" +
            "----------------------"                               + "\n" +
            "Codes attempted:  " + String.valueOf(codesAttempted)  + "\n" +
            "Codes deciphered: " + String.valueOf(codesDeciphered) + "\n"
        );
    }

    public int getTotalGuess(){return this.totalGuess;}

    public void updateTotalGuess(){this.totalGuess++;}

}
