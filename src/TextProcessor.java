import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by carlmccann2 on 21/09/15.
 */
public class TextProcessor {

    public static HashMap<String, HashMap<String,String>> characters = new HashMap<String, HashMap<String,String>>();
    public static HashMap<String,Integer> characterCount = new HashMap<String,Integer>();


    public static boolean newCharacter(String name){
        characterCount.remove(null);

        name = name.replace(" (V.O.)","");
        if(!characters.containsKey(name) ){
            characters.put(name, new HashMap<String, String>());
            //characters.get(name).put(wordCount,sentimentpos/neg);
            if(name != null){
                characterCount.put(name,0);   // linked to character hashmap
            }


            return true;
        }
        return false;

    }

    public static String characterTrimmer(String name){
        name = name.replace(" (V.O.)","");
        name = name.replace("(V.O.)","");
        name = name.replace(" (O.S.)","");
        name = name.replace(" (O.C.)","");
        name = name.replace(" (CONT'D)","");
        name = name.replace(" (PRE-LAP)","");
        name = name.trim();
        name = name.replaceAll("\\s+.\\*","");

        return name;

    }

    public static String dialogueTrimmer(String dialogue){
        dialogue = dialogue.toLowerCase();
        dialogue = dialogue.replaceAll("\\!|\\,|\\.|\\?|\\*", "");
        dialogue = dialogue.replaceAll("\\'"," ");//may need to change this due to conjunction/possessive usage
        dialogue = dialogue.trim();

        return dialogue;
    }

    public static String sentimentAnalyser(String word, HashMap sentiment /*, int wordCount*/){
                                                    // searches the sentiment hashmap

        String sent = "neutral";
        if(sentiment.containsKey(word) == true){

            if(sentiment.get(word) == "positive"){
                //graph.put(wordCount,"positive");
                sent = "positive";
            }
            else if(sentiment.get(word) == "negative"){
                //graph.put(wordCount,"negative");
                sent = "negative";
            }
        }
        return sent;
    }

    public static void scriptReader(String fileName,HashMap sentiment) throws FileNotFoundException{
        System.out.println("Initiating File Reader on: ");
        String filePath = "src/res/";
        filePath += fileName;
        System.out.println(filePath + "\n");



        //wolf format
//        String dialogue = "\\ {16}.\\w.*";
//        String character = "\\ {30}.\\w.*";
//        String scene = "\\d.*";
//        String subScene = "\\d[A-Z].*";
//        String spokenTo = "\\ {21}[(]to.*";
//        String description = "\\ {5}.\\w.*";

        //Darko format
        String dialogue = "\\ {25}.\\w.*";
        String character = "\\ {37}.\\w.*";
        String scene = "INT.";
        String spokenTo = "\\ {30}[(]to.*";
        String description = "\\ {15}.\\w.*";
        //not done yet
        String subScene = "\\d[A-Z].*";



        String lineType = null;

        String currCharacter = null;
        String currSentiment = "neutral";

        int wordCount = 0;

        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filePath));
            String[] wordArray = {};
            String temp = rdrr.readLine();

            while(temp != null){

                if(temp.matches(scene)){
                    //System.out.println(count + " Scene Beginning: " + temp);
                }
                else if(temp.matches(subScene)){
                    //System.out.println(count + " Sub Scene Beginning: " + temp);
                }
                else if(temp.matches(description)){
                    //System.out.println(count + " Description: " + temp);
                }
                else if(temp.matches(character)){           //Needs Work

                    temp = characterTrimmer(temp);
                    newCharacter(temp);

                    if( temp.matches(currCharacter)){

                        if(characterCount.containsKey(currCharacter)){      // updates overall wordCount for character
                            characterCount.put(currCharacter, characterCount.get(currCharacter) + wordCount);
                        }
                        else{
                            characterCount.put(currCharacter,wordCount);
                        }

                        if(characterCount.get(temp) == null){
                            wordCount = 0;
                        }
                        else{
                            wordCount=characterCount.get(temp); // update wordCount for new current character
                        }

                    }

                    currCharacter = temp;               // update current character

                    //System.out.println(count + " Character: " + temp);
                }
                else if(temp.matches(spokenTo)){
                    //System.out.println(count + " Spoken To: " + temp);
                }


                else if(temp.matches(dialogue)){
                    //System.out.println(count + " Dialogue: " + temp);
                    Arrays.fill(wordArray, null);
                    temp = dialogueTrimmer(temp);

                    wordArray = temp.split("\\s+");
                    //System.out.println(Arrays.toString(wordArray));

                    for (int i = 0; i < wordArray.length; i++) {
                        wordCount++;
                        currSentiment = sentimentAnalyser(wordArray[i],sentiment);



//

                        if(currSentiment != "neutral"){

                            System.out.println();
                            if(currCharacter.matches("DONNIE")){
                                if(characterCount.get(currCharacter) < 0){
                                    // will be testing for negative word position
                                }

                                System.out.println("Charcount: " +characterCount.get(currCharacter));
                                System.out.println("currCharacter: " + currCharacter);
                                System.out.println("HM characterCount: " + characterCount);

                            }
                            System.out.println();

                            int a = characterCount.get(currCharacter) + i;
                            characters.get(currCharacter).put(Integer.toString(wordCount), currSentiment);


                        }
                    }
                }
                temp = rdrr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not open: "+filePath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileWriter(String character){

        try{
            Writer output = null;
            File file = new File("src/out/"+character+"-sentiment.csv");
            output = new BufferedWriter(new FileWriter(file));

            output.append(characterCount.get(character) + ", END\n");

            HashMap<String,String> tHash = characters.get(character);
            for(String key : tHash.keySet()){
                output.append(key + ", " + tHash.get(key) + "\n");
            }

            output.close();
            System.out.println(character + " sentiment file has been written");

        }catch(Exception e){
            System.out.println("Could not create file");
        }

    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("TextProcessor Initiated\n");

        ResourceLoader rl = new ResourceLoader();
        rl.sentimentFiller();

        System.out.println("Size of sentiment hashmap: " + rl.sentiment.size() +"\n");

        try {
            //scriptReader("WOWS-Scene9.txt",rl.sentiment);
            //scriptReader("The-Wolf-Of-Wall-Street-Script.txt",rl.sentiment);
            scriptReader("text/Donnie+Darko.txt",rl.sentiment);
            //scriptReader("12-Years-A-Slave.txt",rl.sentiment);
            //scriptReader("10-scenes-100-to-109-WOWS.txt",rl.sentiment);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fileWriter("GRETCHEN");
    }
}
