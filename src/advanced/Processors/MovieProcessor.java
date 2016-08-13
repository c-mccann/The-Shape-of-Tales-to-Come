package advanced.Processors;

import advanced.SentimentAnalyser;
import advanced.SentimentData.MovieSD;

import java.io.*;
import java.util.*;


/**
 * Created by carlmccann2 on 02/11/15.
 * Processes movie scripts
 */
public class MovieProcessor {


    private static HashMap<String, ArrayList<MovieSD>> charactersSentiment = new HashMap<>();
    private static List<MovieSD> allSentiment = new ArrayList<>();
    private static String characterData;
    private static String sentenceData;
    //private static int sentimentData;
    //private static double normalisedSentData;
    private static String sceneData;
    private static int charactersSentencePositionData = 0;
    private static int movieSentencePositionData = 0;
    private static String carryOverString = null;
    private static boolean carryOver = false;
    private static boolean newCharacter = false;
    private static String prevCharacter = null;
    private static SentimentAnalyser sentimentAnalyser = new SentimentAnalyser();
    private SentimentAnalyser sA;


    public MovieProcessor() {
        sentimentAnalyser.init();
    }

    public static boolean newCharacter(String name) {

        if (!charactersSentiment.containsKey(name)) {
            charactersSentiment.put(name, new ArrayList<MovieSD>());
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
        //dialogue = dialogue.toLowerCase();
        //dialogue = dialogue.replaceAll("\\!|\\,|\\.|\\?|\\*", "");
        //dialogue = dialogue.replaceAll("\\'"," ");//may need to change this due to conjunction/possessive usage
        dialogue = dialogue.trim();

        return dialogue;
    }

    public static void dialogueProcessor(String dialogue) {

        String sentenceRegex = "\\.{1,3} |\\! |\\? ";
        //to abstract the code within the dialogue if statement

        //int wordCount = characterCount.get(currCharacter);
        String temp;
        String[] wordArray = {};

        //System.out.println(count + " Dialogue: " + temp);
        Arrays.fill(wordArray, null);
        temp = dialogueTrimmer(dialogue);

        wordArray = temp.split("\\s+");
        //System.out.println(Arrays.toString(wordArray));


        //characterCount.put(currCharacter,characterCount.get(currCharacter) + wordArray.length);

        String[] sentenceSplit = {};
        //System.out.println(dialogue);
        //if(dialogue.matches(sentenceRegex)){
        //System.out.println(dialogue);
        sentenceSplit = dialogue.split(sentenceRegex);
        for (int i = 0; i < sentenceSplit.length; i++) {
            sentenceSplit[i] = sentenceSplit[i].trim();
            sentenceSplit[i] = sentenceSplit[i].replace(".", "");
            sentenceSplit[i] = sentenceSplit[i].replace("?", "");
            sentenceSplit[i] = sentenceSplit[i].replace("!", "");
            sentenceSplit[i] = sentenceSplit[i].replace("...", "");
        }

        /*
        Need to fix logic to concat lines together within the list
        if they are part of the same sentence, if there is an end
        of sentence, we need to store the first part of the line
        in the current sentence in the arraylist, then start a new
        one then repeat this process.
         */

        if (sentenceSplit.length == 1) {
            String temp2;

            //characterDialogue.get(currCharacter).add(sentenceSplit[0]);
        }
        if (sentenceSplit.length == 2) {
            String concatSentence = null;
            //int concatIndex = cDControl.get(currCharacter);
            //List tempList = characterDialogue.get(currCharacter);

            //concatSentence = characterDialogue.get(currCharacter).get(cDControl.get(currCharacter));

            //concatSentence = (String)tempList.get(concatIndex);
            //System.out.println(concatSentence);

            //System.out.println(characterDialogue.get(currCharacter).get(cDControl.get(currCharacter)));
            //System.out.println("TEST");
            //characterDialogue.get(currCharacter);
            //cDControl.put(currCharacter,cDControl.get(currCharacter) + 1);
            //characterDialogue.get(currCharacter).add(sentenceSplit[1]);
        } else if (sentenceSplit.length == 3) {
            System.out.println("sentence in middle of the line");
            //cDControl.put(currCharacter, cDControl.get(currCharacter) + 1);
            //characterDialogue.get(currCharacter).add(sentenceSplit[1]);
            //cDControl.put(currCharacter,cDControl.get(currCharacter) + 1);
            //characterDialogue.get(currCharacter).add(sentenceSplit[2]);
        }

        System.out.println(Arrays.toString(sentenceSplit));
        //}
    }

    public static void characterProcessor(String character) {
        //to abstract the code within the character if statement
        character = characterTrimmer(character);
        newCharacter(character);
        //currCharacter = character;
    }

    public static void scriptReader(String fileName, int characterWS,
                                    int dialWS, int sceneWS) throws FileNotFoundException {

        System.out.println("Initiating File Reader on: ");
        String filePath = "src/res/";
        filePath += fileName;
        System.out.println(filePath + "\n");

        //Donnie Darko format
//        String dialogue = "\\ {25}.\\w.*";
//        String dialogue2 = "\\ {25}.\\w.*";
//        String character = "\\ {37}.\\w.*";
//        String scene  = " {15}INT\\..*$";
//        String scene2 = " {15}EXT\\..*$";
//        String spokenTo = "\\ {30}[(]to.*";
//        String description = "\\ {15}.\\w.*";

//        with passed variables
        String dialogue = "\\ {" + Integer.toString(dialWS) + "}.\\w.*";
        String dialogue2 = "\\ {" + Integer.toString(dialWS) + "}.\\w.*";
        String character = "\\ {" + Integer.toString(characterWS) + "}.\\w.*";
        String scene = " {" + Integer.toString(sceneWS) + "}INT.*$";
        String scene2 = " {" + Integer.toString(sceneWS) + "}EXT.*$";
//        String scene3 = "\\s{" + Integer.toString(sceneWS) + "}(([A-Z]*)(\\s|[A-Z])*)$"; // scenes found in fargo


        String spokenTo = "\\ {30}[(]to.*";
        String description = "\\ {0}.\\w.*";
//        //not done yet
        String subScene = "\\d[A-Z].*";

        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filePath));
            String currentLine = rdrr.readLine();


            while (currentLine != null) {
                System.out.println(currentLine);
                if (currentLine.matches(scene) || currentLine.matches(scene2) /*|| temp.matches(scene3)*/) {
                    //System.out.println(count + " Scene Beginning: " + temp);
                    sceneData = currentLine.trim();

                } else if (currentLine.matches(subScene)) {
                    //System.out.println(count + " Sub Scene Beginning: " + temp);
                } else if (currentLine.matches(description)) {
                    //System.out.println(count + " Description: " + temp);
                } else if (currentLine.matches(character)) {           //Needs Work

                    if (characterData != currentLine) {
                        System.out.println();
                        prevCharacter = characterData;
                        newCharacter = true;
                    } else {
                        newCharacter = false;
                    }

                    currentLine = characterTrimmer(currentLine);
                    characterData = currentLine;
                    newCharacter(currentLine);
                    characterProcessor(currentLine);
                } else if (currentLine.matches(spokenTo)) {
                    //System.out.println(count + " Spoken To: " + temp);
                } else if (currentLine.matches(dialogue) || currentLine.matches(dialogue2)) {
                    //System.out.println(count + " Dialogue: " + temp);
                    //dialogueProcessor(temp);

                    sentenceFinder(currentLine);
                }
                currentLine = rdrr.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not open: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sentenceFinder(String line) {
        int sentiment = -1;
        line = dialogueTrimmer(line);
        String partialSentence = null;


        // case for when a new character speaks but the previous character has unprocessed lines due to lack
        // of terminal punctuation e.g. in Donnie Darko Rose Says: How did you know --

        if (newCharacter && carryOver) {  // sceneData could be wrong in this, need to update
            System.out.println("\n\n NEW CHARACTER\n\n");

            sentiment = sentimentAnalyser.determineSentenceSentiment(carryOverString);
            movieSentencePositionData += 1;
            MovieSD movieSD = new MovieSD(prevCharacter, carryOverString, sentiment, sceneData,
                    charactersSentencePositionData, movieSentencePositionData);
        charactersSentiment.get(prevCharacter).add(movieSD);
            allSentiment.add(movieSD);

            newCharacter = false;
            carryOver = false;
            carryOverString = null;

        }

        if (carryOver) {
            System.out.println("\n\nIN CARRY OVER IF \n\n");
            partialSentence = carryOverString;

            //line = carryOverString + " " + line;      // testing to see if adding to partial string rather
            //carryOver = false;                          // than line will improve performace, ie one less iteration
            //carryOverString = null;                     // of loop per line


        }

        String delims = "\\.|\\!|\\?";
        String nullRegex = "^null[.!?]$";

        StringTokenizer sT = new StringTokenizer(line, delims, true);

        String currentToken = null;


        boolean endingFound = false;

        /*

        need to write case in the while statement where partialSentence = Mr. a.m. p.m. etc.
        so it doesnt run SA on them and counts them as part of sentence

        also, debate over ... being a marker for sentence ending or just a pause within a sentence
        most likely latter case

         */

        while (sT.hasMoreTokens()) {
            currentToken = sT.nextToken();
            System.out.println("Current Token:                  " + currentToken);

            if (!currentToken.matches(delims)) {

                System.out.println("Delim not Found");

                if (!endingFound) {
                    if (carryOver) {
                        partialSentence += " " + currentToken;
                        carryOver = false;
                    }   // caters for carryover
                    else {
                        partialSentence = currentToken;
                    }                              // and reducing loops

                    System.out.println("Partial Sentence:               " + partialSentence);
                }

                if (endingFound) {
                    System.out.println("Ending Found next not ending:   " + partialSentence);

                    if (!partialSentence.matches(nullRegex)) {
                        if (partialSentence.endsWith("...")) {
                            partialSentence += currentToken;
                        } else {
                            sentiment = sentimentAnalyser.determineSentenceSentiment(partialSentence);
                            movieSentencePositionData += 1;
                            MovieSD movieSD = new MovieSD(characterData, partialSentence, sentiment, sceneData,
                                    charactersSentencePositionData, movieSentencePositionData);
                            charactersSentiment.get(characterData).add(movieSD);
                            allSentiment.add(movieSD);
                            endingFound = false;
                            partialSentence = currentToken;
                            carryOverString = null;
                        }
                    }
                }
            }
            if (currentToken.matches(delims)) {

                partialSentence += currentToken;
                System.out.println("Ending Found next not ending:   " + partialSentence);
                if (partialSentence.endsWith("...")) {
                    carryOverString = partialSentence;
                    carryOver = true;
                } else {
                    endingFound = true;
                }
            }
        }
        if (currentToken.matches(delims)) {

            if (!partialSentence.matches(nullRegex)) {
                if (!partialSentence.endsWith("...")) { // caters for ellipsis at end of line
                    sentiment = sentimentAnalyser.determineSentenceSentiment(partialSentence);
                    movieSentencePositionData += 1;
                    MovieSD movieSD = new MovieSD(characterData, partialSentence, sentiment, sceneData,
                            charactersSentencePositionData, movieSentencePositionData);
                    charactersSentiment.get(characterData).add(movieSD);

                    allSentiment.add(movieSD);
                    carryOverString = null;
                    carryOver = false;
                    partialSentence = null;
                } else {
                    carryOverString = partialSentence;
                    carryOver = true;
                }
            }
        }
        if (!currentToken.matches(delims)) {
            carryOverString = partialSentence;
            carryOver = true;
        }

        System.out.println("CT at End:                      " + currentToken);
        System.out.println("PS at End:                      " + partialSentence);
        System.out.println("CarryOver:                      " + carryOverString);

        //            to loop through token by token for testing
//            boolean test = false;
//            Scanner reader = new Scanner(System.in);  // Reading from System.in
//            while(!test){
//                System.out.println("enter y: ");
//                String n = reader.next();
//                if( n.equals("y")){test = true;}
//            }

        newCharacter = false;

        return partialSentence;             // if there is carry over, it'll be returned. Then tagged onto next line
    }

    public static void outputSentimentFile(String script, int charWS,
                                           int dialWS, int sceneWS) throws FileNotFoundException {

        System.out.println("Script: " + script);
        scriptReader(script, charWS, dialWS, sceneWS);
        String[] split = script.split("/");
        String scriptName = split[split.length - 1];
        File file = new File("src/advanced/out/Movie/" + scriptName + "-all-sentiment.csv");
//        File file = new File("Erin-Brockovich-all-sentiment.txt", "UTF-8");
        file.getParentFile().mkdirs();

        PrintWriter wrtr = new PrintWriter(file);

        System.out.println(allSentiment);
        for (int i = 0; i < allSentiment.size(); i++) {
            MovieSD movieSD = allSentiment.get(i);
            movieSD.dataPrinter();
            wrtr.println(movieSD.getMSP() + "§" + movieSD.getNormSent() + "§" +
                    movieSD.getCharacter() + "§" + movieSD.getSentence() + "§" + movieSD.getScene() +
                    "§" + movieSD.getCSP());
        }

        if (wrtr != null) {
            wrtr.close();
        }

//        resetting variables for next use
        allSentiment.clear();
        charactersSentiment.clear();
        allSentiment.clear();
        charactersSentencePositionData = 0;
        movieSentencePositionData = 0;
        carryOverString = null;
        carryOver = false;
        newCharacter = false;
        prevCharacter = null;


    }

    public static void main(String args[]) throws IOException, InterruptedException {

        MovieProcessor tp2 = new MovieProcessor();
        //tp2.sentenceFinder("hello its me");

//        String script = "Movie/Donnie+Darko.txt";
//        String script = "Movie/Erin+Brockovich.txt";
        //String script = "testMovies/Donnie-Darko-sample.txt";
        //String script = "Movie/American Werewolf In London, An.txt";
//        System.out.println(script);


        //String script = "scraped_scripts2/American-Beauty.txt";

        String script = "scraped_scripts2/American-Beauty.txt";

//        needs whitespace vars
//        scriptReader(script,);

        for (String key : charactersSentiment.keySet()) {

            for (int i = 0; i < charactersSentiment.get(key).size(); i++) {

                charactersSentiment.get(key).get(i).dataPrinter();
            }

            System.out.println("_____________________________________________________________________________");
        }


//advanced/out/Movie
        File file = new File("src/advanced/out/Movie/American-Beauty-all-sentiment.csv");
//        File file = new File("Erin-Brockovich-all-sentiment.txt", "UTF-8");
        file.getParentFile().mkdirs();
//        File fqe = new File("hi",
        PrintWriter wrtr = new PrintWriter(file);

        System.out.println(allSentiment);
        for (int i = 0; i < allSentiment.size(); i++) {
            MovieSD movieSD = allSentiment.get(i);
            movieSD.dataPrinter();
            wrtr.println(movieSD.getMSP() + "," + movieSD.getNormSent() + ',' +
                    movieSD.getCharacter() + ',' + movieSD.getSentence() + ',' + movieSD.getScene() +
                    ',' + movieSD.getCSP());
        }

        if (wrtr != null) {
            wrtr.close();
        }


        System.out.println("Testing 3");

//        File path = new File("src/res/scraped_scripts2");
//        System.out.println(path);
//        File[] files = path.listFiles();
//        System.out.println("dL " + files);
//        if (files != null){
//            System.out.println("Success");
//            for(File child : files){
//                allSentiment.clear();
//                scriptReader(child);
//                if(!child.getName().equals(".DS_Store")){
//                    PrintWriter wrtr2 = new PrintWriter("advanced/out/Movie/" + child.getName() + "-sentiment.csv", "UTF-8");
//                    for (int i = 0; i < allSentiment.size(); i++) {
//                        MovieSD movieSD =  allSentiment.get(i);
//                        movieSD.dataPrinter();
//                        wrtr2.println(movieSD.getMSP() + "," + movieSD.getNormSent() + ',' +
//                                movieSD.getCharacter() + ',' + movieSD.getSentence() + ',' + movieSD.getScene() +
//                                ',' + movieSD.getCSP());
//                    }
//                }
//            }
//        }
//        else{
//            System.out.println("Dir Not Found");
//        }


//        PrintWriter writer = new PrintWriter("Erin-Raw-Sentiment.txt", "UTF-8");
//        ArrayList tempList = charactersSentiment.get("ERIN");
//
//        for (int i = 0; i < tempList.size() ; i++) {
//            MovieSD tempMovie = (MovieSD) tempList.get(i);
//            writer.println(tempMovie.getNormSent() + ", " + tempMovie.getMSP());
//        }
//        writer.close();

        //System.out.println(characters);
        //System.out.println(characterCount);

//        List zeroWordCharacter = new ArrayList<>();                         //Removes zero word characters
//        for(String key : characterCount.keySet()){
//            if(characterCount.get(key) == 0){
//                zeroWordCharacter.add(key);
//            }
//        }
//        for(int i = 0; i< zeroWordCharacter.size(); i++){
//            characterCount.remove(zeroWordCharacter.get(i));
//        }
//        //System.out.println(characterCount);
//
//        System.out.println(characterDialogue.get("DONNIE"));
//        System.out.println(cDControl);
    }

    public HashMap charSentReturner() {
        return charactersSentiment;
    }
}
