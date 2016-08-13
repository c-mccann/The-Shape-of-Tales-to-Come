package simple;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by carlmccann2 on 21/09/15.
 */
public class SimpleTextProcessor {

    private static HashMap<String, HashMap<Integer, String>> characters = new HashMap<String, HashMap<Integer, String>>();
    private static HashMap<String, Integer> characterCount = new HashMap<String, Integer>();
    private static String currCharacter;
    private static String currSentiment;


    public static boolean newCharacter(String name) {
        characterCount.remove(null);        // may remove later on

        if (!characters.containsKey(name)) {
            characters.put(name, new LinkedHashMap<Integer, String>());
            if (name != null) characterCount.put(name, 0);   // linked to character hashmap
            return true;
        }
        return false;
    }

    public static String characterTrimmer(String name) {
//        ArrayList<String> redundancy = new ArrayList<>(Arrays.asList(
//                " (V.O.)","(V.O.)"," (O.S.)"," (O.C.)"," (CONT'D)"," (PRE-LAP)",
//                "DIFFERENT VOICE (",")"
//        ));
        name = name.replace(" (V.O.)", "");
        name = name.replace("(V.O.)", "");
        name = name.replace(" (O.S.)", "");                // verify which is nicer method
        name = name.replace(" (O.C.)", "");
        name = name.replace(" (CONT'D)", "");
        name = name.replace(" (PRE-LAP)", "");

        name = name.replace("DIFFERENT VOICE (", "");

        name = name.replace(" (OVER BLACK)", "");
        name = name.replace(" (ON TV)", "");
        name = name.replace(" (AT FIRST O.S.)", "");
        name = name.replace(" (ON THE PHONE)", "");

        name = name.replace(")", "");

//        for(String redundant: redundancy)
//            name.replace(redundant,"");

        name = name.trim();
        name = name.replaceAll("\\s+.\\*", "");

        return name;
    }

    public static String dialogueTrimmer(String dialogue) {
        dialogue = dialogue.toLowerCase();
        dialogue = dialogue.replaceAll("\\!|\\,|\\.|\\?|\\*", "");
        //dialogue = dialogue.replaceAll("\\'"," ");//may need to change this due to conjunction/possessive usage
        dialogue = dialogue.trim();

        return dialogue;
    }

    public static String sentimentAnalyser(String word, HashMap sentiment /*, int wordCount*/) {
        // searches the sentiment hashmap
        String sent = "neutral";
        if (sentiment.containsKey(word) == true) {

            if (sentiment.get(word) == "positive") {
                //graph.put(wordCount,"positive");
                sent = "positive";
            } else if (sentiment.get(word) == "negative") {
                //graph.put(wordCount,"negative");
                sent = "negative";
            }
        }
        return sent;
    }

    public static void dialogueProcessor(String dialogue, HashMap sentiment) {
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
        characterCount.put(currCharacter, characterCount.get(currCharacter) + wordArray.length);
    }

    public static void characterProcessor(String character) {
        //to abstract the code within the character if statement
        character = characterTrimmer(character);
        newCharacter(character);
        currCharacter = character;
    }

    public static void scriptReader(String fileName, HashMap sentiment) throws FileNotFoundException {
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
        String scene2 = "EXT.";
        String spokenTo = "\\ {30}[(]to.*";
        String description = "\\ {15}.\\w.*";
//        //not done yet
        String subScene = "\\d[A-Z].*";

        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filePath));
            String temp = rdrr.readLine();

            while (temp != null) {
                if (temp.matches(scene) || temp.matches(scene2)) {
                    //System.out.println(count + " Scene Beginning: " + temp);
                } else if (temp.matches(subScene)) {
                    //System.out.println(count + " Sub Scene Beginning: " + temp);
                } else if (temp.matches(description)) {
                    //System.out.println(count + " Description: " + temp);
                } else if (temp.matches(character)) {           //Needs Work
                    temp = characterTrimmer(temp);
                    newCharacter(temp);
                    characterProcessor(temp);
                } else if (temp.matches(spokenTo)) {
                    //System.out.println(count + " Spoken To: " + temp);
                } else if (temp.matches(dialogue)) {
                    //System.out.println(count + " Dialogue: " + temp);
                    dialogueProcessor(temp, sentiment);
                }
                temp = rdrr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not open: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileWriter(String character) {
        try {
            Writer output = null;
            File file = new File("src/simple/out/" + character + "-sentiment.csv");

            output = new BufferedWriter(new FileWriter(file));
            output.append(characterCount.get(character) + ", END\n");

            HashMap<Integer, String> tHash = characters.get(character);
            for (Integer key : tHash.keySet()) {
                output.append(key + ", " + tHash.get(key) + "\n");
            }
            output.close();
            System.out.println(character + " sentiment file has been written");
        } catch (Exception e) {
            System.out.println("Error Writing File");
        }
    }


    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("TextProcessor Initiated\n");


        ResourceLoader rl = new ResourceLoader();
        rl.sentimentFiller();

        String script = "Movie/Donnie+Darko.txt";

        System.out.println("Size of sentiment hashmap: " + rl.sentiment.size() + "\n");

        try {
            //scriptReader("testMovies/The-Wolf-Of-Wall-Street-Script.txt",rl.sentiment);
            //scriptReader("Movie/Donnie+Darko.txt",rl.sentiment);
            scriptReader(script, rl.sentiment);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ArrayList<String> invalidCharacterList = new ArrayList<>();

        for (String key : characterCount.keySet())
            if (characterCount.get(key) == 0) invalidCharacterList.add(key);


        for (String invalidCharacter : invalidCharacterList) {
            characterCount.remove(invalidCharacter);            // if not a character(not spoken any words)
            characters.remove(invalidCharacter);                // tested on Donnie Darko which had
        }                                                       // OCTOBER 18 1988 as a character

//        System.out.println(characterCount);
//        System.out.println(characters);

        for (String key : characterCount.keySet()) {

            fileWriter(key);
        }

        double posTest = 0;
        double negTest = 0;

//        for(String value : characters.get("DONNIE").values()){
//            if(value.equals("positive")){
//                posTest++;
//            }
//            else{
//                negTest++;
//            }
//        }

//        System.out.println("\n\n\n");
//        for(String key : characters.keySet()){
//            System.out.println(key);
//        }

//        System.out.println();
//        System.out.println("Donnie positive test: " + posTest);
//        System.out.println("Donnie negative test: " + negTest);
//        System.out.println("Donnie ratio: " + posTest/negTest);


    }
}
