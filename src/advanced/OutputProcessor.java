package advanced;

import advanced.SentimentData.MovieSD;
import advanced.SentimentData.PlaySD;
import advanced.SentimentData.ShakespeareSD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by carlmccann2 on 08/04/2016.
 * pulls data into hashmaps and lists from processor output files for use in
 */
public class OutputProcessor {
    public List allSentiment = new ArrayList<>();
    public List allSentimentLookup = new ArrayList<>();
    public HashMap<String, ArrayList> characterSentiment = new HashMap<>();

    public int processResults(String filename, String mediaType) {

        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filename));
            String temp = rdrr.readLine();

//            to normalise
//            1   =   m * (max + c)
//            -1  =   m * (min + c)
//
//            1   =   m * (4 + c)
//            -1  =   m * (0 + c)
//
//            after solving simultaneous equations
//            m   =   0.5
//            c   =   -2

//              to undo normalisation
//            4   =   m * (max + c)
//            0  =   m * (min + c)
//
//            4   =   m * (1 + c)
//            0  =   m * (-1 + c)
//
//            after solving simultaneous equations
//            m   =   2
//            c   =   1


            while (temp != null) {

                String[] lineSplit = temp.split("§");
                int sentiment = (int) (2 * (Double.parseDouble(lineSplit[1]) + 1));
                if (mediaType.equals("Movie")) {
                    MovieSD SD = new MovieSD(lineSplit[2], lineSplit[3], sentiment,
                            lineSplit[4], 0, Integer.parseInt(lineSplit[0]));
                    allSentiment.add(SD);

                    if(allSentimentLookup.size() > 0){
                        if(((MovieSD)allSentimentLookup.get
                                (allSentimentLookup.size()-1)).getMSP() != SD.getMSP()){
                            allSentimentLookup.add(SD);
                        }

                    }
                    else{
                        allSentimentLookup.add(SD);
                    }

                    ArrayList tempList = characterSentiment.get(lineSplit[2]);
                    if (tempList == null) {
                        characterSentiment.put(lineSplit[2], new ArrayList());
                    } else {
                        tempList.add(SD);
                        characterSentiment.put(lineSplit[2], tempList);
                    }


                } else if (mediaType.equals("Play")) {
                    PlaySD SD = new PlaySD(lineSplit[2], lineSplit[3], sentiment,
                            lineSplit[4], Integer.parseInt(lineSplit[0]));
                    allSentiment.add(SD);

                    if(allSentimentLookup.size() > 0){
                        if(((PlaySD)allSentimentLookup.get
                                (allSentimentLookup.size()-1)).getPSP() != SD.getPSP()){
                            allSentimentLookup.add(SD);

                        }
                    }
                    else{
                        allSentimentLookup.add(SD);
                    }
                    ArrayList tempList = characterSentiment.get(lineSplit[2]);
                    if (tempList == null) {
                        characterSentiment.put(lineSplit[2], new ArrayList());
                    } else {
                        tempList.add(SD);
                        characterSentiment.put(lineSplit[2], tempList);
                    }
                } else if (mediaType.equals("Shakespearean")) {
                    ShakespeareSD SD = new ShakespeareSD(lineSplit[2], lineSplit[3], sentiment,
                            lineSplit[4], lineSplit[5], Integer.parseInt(lineSplit[0]));
                    allSentiment.add(SD);

                    if(allSentimentLookup.size() > 0){
                        if(((ShakespeareSD)allSentimentLookup.get
                                (allSentimentLookup.size()-1)).getPSP() != SD.getPSP()){
                            allSentimentLookup.add(SD);
                        }
                    }
                    else{
                        allSentimentLookup.add(SD);
                    }

                    ArrayList tempList = characterSentiment.get(lineSplit[2]);
                    if (tempList == null) {
                        characterSentiment.put(lineSplit[2], new ArrayList());
                    } else {
                        tempList.add(SD);
                        characterSentiment.put(lineSplit[2], tempList);
                    }
                }

                temp = rdrr.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
