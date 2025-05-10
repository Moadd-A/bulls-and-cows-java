import java.io.*;
import java.util.*;

public class LettersCode extends SecretCode {

    public int genLetterCode(String[] word) {
        try {
            int i = 0;
            File file = new File("words.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                word[i] = data;
                i++;
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Words file is not found");
            return -1;

        }
        return 0;
    }

    public void genCode() {
        String[] word = new String[15];
        genLetterCode(word);
        Random random = new Random(); // can inherit
        int rand = random.nextInt(0, 15);
        decipheredCode = word[rand];
    }


}





