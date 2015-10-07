import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by carlmccann2 on 30/09/15.
 */
public class ResourceLoader {

    public static HashMap<String,String> sentiment = new HashMap<String,String>();

    public static void sentimentFiller() throws FileNotFoundException {

        int lexiconInfo = 36;
        int pcount = 0, ncount = 0;
        System.out.println("Filling Hashmap with sentiment word list");
        try {
            BufferedReader rdrr = new BufferedReader(new FileReader("src/res/positive-words.txt"));

            String temp = null;
            for (int i = 0; i < lexiconInfo ; i++) {
                temp = rdrr.readLine();
            }


            while (temp != null) {
                sentiment.put(temp, "positive");
                pcount++;
                //System.out.println("P: " + pcount);
                temp = rdrr.readLine();
            }

            rdrr = new BufferedReader(new FileReader("src/res/negative-words.txt"));


            for (int i = 0; i < lexiconInfo; i++) {
                temp = rdrr.readLine();
            }
            while (temp != null) {
                sentiment.put(temp, "negative");
                ncount++;
                //System.out.println("N: " + ncount);
                temp = rdrr.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Words Loaded");
    }
}
