import java.io.*;
import java.util.*;

/**
 * Created by carlmccann2 on 21/09/15.
 */
public class TextProcessor {

    private static HashMap<String, HashMap<Integer,String>> characters = new HashMap<String, HashMap<Integer,String>>();
    private static HashMap<String,Integer> characterCount = new HashMap<String,Integer>();
    private static String currCharacter;
    private static String currSentiment;


    public static boolean newCharacter(String name){
        characterCount.remove(null);

        name = name.replace(" (V.O.)","");
        if(!characters.containsKey(name) ){
            characters.put(name, new LinkedHashMap<Integer, String>());
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
        //dialogue = dialogue.replaceAll("\\'"," ");//may need to change this due to conjunction/possessive usage
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

    public static void dialogueProcessor(String dialogue,HashMap sentiment) {
        //to abstract the code within the dialogue if statement
        int wordCount = characterCount.get(currCharacter);
        String temp;
        String[] wordArray = {};

        //System.out.println(count + " Dialogue: " + temp);
        Arrays.fill(wordArray, null);
        temp = dialogueTrimmer(dialogue);

        wordArray = temp.split("\\s+");
        //System.out.println(Arrays.toString(wordArray));

        for (int i = 0; i < wordArray.length; i++) {
            wordCount++;
            currSentiment = sentimentAnalyser(wordArray[i], sentiment);

            if (!currSentiment.equals("neutral")) {
                characters.get(currCharacter).put(wordCount, currSentiment);
            }
        }
        characterCount.put(currCharacter,characterCount.get(currCharacter) + wordArray.length);
    }

    public static void characterProcessor(String character,int wordCount){
        //to abstract the code within the character if statement
        character = characterTrimmer(character);
        newCharacter(character);
        currCharacter = character;
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

//        //Darko format
        String dialogue = "\\ {25}.\\w.*";
        String character = "\\ {37}.\\w.*";
        String scene = "INT.";
        String spokenTo = "\\ {30}[(]to.*";
        String description = "\\ {15}.\\w.*";
//        //not done yet
        String subScene = "\\d[A-Z].*";

        int wordCount = 0;

        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filePath));
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
                    characterProcessor(temp,wordCount);
                }

                else if(temp.matches(spokenTo)){
                    //System.out.println(count + " Spoken To: " + temp);
                }

                else if(temp.matches(dialogue)){
                    //System.out.println(count + " Dialogue: " + temp);
                    dialogueProcessor(temp,sentiment);
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

            HashMap<Integer,String> tHash = characters.get(character);
            for(Integer key : tHash.keySet()){
                output.append(key + ", " + tHash.get(key) + "\n");
            }

            output.close();
            System.out.println(character + " sentiment file has been written");

        }catch(Exception e){
            System.out.println("Error Writing File");
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
            //scriptReader("Donnie-Darko-sample.txt",rl.sentiment);
            //scriptReader("12-Years-A-Slave.txt",rl.sentiment);
            //scriptReader("10-scenes-100-to-109-WOWS.txt",rl.sentiment);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        String tempChars[] = new String[1000];
        for(String key : characterCount.keySet()){
            int i = 0;
            if (characterCount.get(key) == 0){
                tempChars[i] = key;
                i++;
            }
        }
        for (int i = 0; i < tempChars.length ; i++) {
            System.out.println(tempChars[i]);       //null when should be string
            characterCount.remove(tempChars[i]);            // if not a character(not spoken any words)
            characters.remove(tempChars[i]);
        }

        System.out.println(characterCount);
        System.out.println(characters);

        fileWriter("DONNIE");

        double postest = 0;
        double negtest = 0;

        for(String value : characters.get("DONNIE").values()){
            if(value.equals("positive")){
                postest++;
            }
            else{
                negtest++;
            }
        }

        for(String key : characters.keySet()){
            System.out.println(key);
        }

        System.out.println();
        System.out.println("Donnie positive test: " + postest);
        System.out.println("Donnie negative test: " + negtest);
        System.out.println(postest/negtest);
//        fileWriter("ROSE");




    }
}
