import java.util.Random;

public class NumbersCode extends SecretCode{

    public void genCode() {
       String numAsString;
        Random rand = new Random(); // can inherit
        int[] ranNum = {10, 10, 10, 10, 10, 10, 10, 10}; // all 10s so doesn't compare with 0-9

        boolean dupeFlag = false;
        for (int i = 0; i < 8; i++) {
            dupeFlag = false;
            int num = rand.nextInt(0, 10);
            for (int k : ranNum) {
                if (num == k) {
                    dupeFlag = true;
                }
            }
            if (dupeFlag) {
                i--;
            } else ranNum[i] = num;
        }
        numAsString = "";
        for (int i = 0; i < 8; i++) {
            numAsString += (char) (ranNum[i] + '0');
        }
        decipheredCode = numAsString;
    }
}
