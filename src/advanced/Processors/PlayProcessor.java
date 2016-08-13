package advanced.Processors;

import advanced.SentimentAnalyser;
import advanced.SentimentData.PlaySD;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by carlmccann2 on 31/01/2016.
 * Processes play scripts, format of the importance of being earnest, salome however fits the format of the shakespeare
 * processor
 */
public class PlayProcessor {

    public ArrayList<PlaySD> allSentiment = new ArrayList<>();

    /*


FIRST ACT                                                                       All caps no terminal chars


SCENE                                                                           All caps no terminal chars


Morning-room in Algernon's flat in Half-Moon Street.  The room is               Description of scene: block of text
luxuriously and artistically furnished.  The sound of a piano is heard in       doesnt have character as first sentence
the adjoining room.                                                             straight after SCENE

[Lane is arranging afternoon tea on the table, and after the music has          Setup of the scene with characters
ceased, Algernon enters.]                                                       enclosed by []

Algernon.  Did you hear what I was playing, Lane?                               speech begins with character name
                                                                                as first sentence
Lane.  I didn't think it polite to listen, sir.

ACT DROP                                                                        all caps tells reader act has ended
     */
    public static void main(String args[]) throws FileNotFoundException {


        PlayProcessor pp = new PlayProcessor();

        ArrayList lines = pp.fileOpen("Play/The Importance of Being Earnest. Wilde, O.txt");
        ArrayList blocks = pp.fileOpen2("Play/The Importance of Being Earnest. Wilde, O.txt");
        ArrayList characters = pp.characterPuller(lines);


//        ArrayList<String> safv = new ArrayList<>();
//        for(int i = 0; i<lines2.size();i++) {
//            //System.out.println(i + ":   " +(String)lines2.get(i));
//                //safv.add(lines.get(i));
//        }

        HashMap characterSentiment = pp.sentimentAnalyser(blocks, characters);

        for (Object key : characterSentiment.keySet()) {
            ArrayList temp = (ArrayList) characterSentiment.get((String) key);
            System.out.println("____________________________________________________________________________________");
            System.out.println(key);
            System.out.println("____________________________________________________________________________________");
            for (int i = 0; i < temp.size(); i++) {
                PlaySD psd = (PlaySD) temp.get(i);

                psd.dataPrinter();
            }
        }

        for (Object key : characterSentiment.keySet()) {
            System.out.println(key);
        }
    }

    public ArrayList fileOpen(String fileName) throws FileNotFoundException {
        String filePath = "src/res/";
        filePath += fileName;
        System.out.println("Initiating File Reader on:      " + filePath);
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filePath));
            String currentLine = rdrr.readLine();
            while (currentLine != null) {
                lines.add(currentLine);
                currentLine = rdrr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't Open file:             " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }   // returns lines of writing

    public ArrayList fileOpen2(String fileName) throws FileNotFoundException {       // returns blocks of writing
        String currentBlock = null;
        String filePath = "src/res/";
        filePath += fileName;
        System.out.println("Initiating File Reader on:      " + filePath);
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filePath));
            String currentLine = rdrr.readLine();

            while (currentLine != null) {
                if (!currentLine.matches("^\\s*$")) {
                    if (currentBlock == null) {
                        currentBlock = currentLine;
                    } else {
                        currentBlock += " " + currentLine;
                    }
                } else {
                    if (currentBlock != null) {
                        lines.add(currentBlock);
                        currentBlock = null;
                    }
                }
                currentLine = rdrr.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't Open file:             " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public ArrayList characterPuller(ArrayList lines) {
        ArrayList<String> characters = new ArrayList<>();
        int i = lines.indexOf("THE PERSONS IN THE PLAY");
        for (i += 3; i < lines.size(); i++) {
            if (lines.get(i).equals("")) {
                break;
            }
            characters.add((String) lines.get(i));
        }
        return characters;
    }

    public boolean stringIsAllUpper(String line) {
        if (line.matches("^\\s*$")) {
            return false;
        }
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isUpperCase(line.charAt(i)) && !Character.isWhitespace(line.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public HashMap sentimentAnalyser(ArrayList blocks, ArrayList characters) {
        HashMap<String, ArrayList<PlaySD>> characterSentiment = new HashMap<>();
        SentimentAnalyser sentimentAnalyser = new SentimentAnalyser();
        sentimentAnalyser.init();

        int playSentencePosition = 1;
        String listCharacter = null;
        String currentCharacter = null;
        String currentSentence = null;
        String currentAct = null;
        boolean colonUsed = false;

        int startingPoint = blocks.indexOf("FIRST ACT");
        int endingPoint = blocks.indexOf("TABLEAU");

        System.out.println("Starting Point:      " + startingPoint);
        System.out.println("Ending Point:        " + endingPoint);

        for (int i = startingPoint; i < endingPoint; i++) {                          // iterates a block at a time
            String block = (String) blocks.get(i);

            block = block.replace("...", "--");              // could be put in seperate block cleaner method
            block = block.replace(". . .", "--");
            block = block.replace("Mr.", "Mr");
            block = block.replace("Mrs.", "Mrs");
            block = block.replace("_", "");
            block = block.replaceAll("\\[.*\\.\\]", "");


            String blockSplit[] = block.split("(?<=(\\.|\\!|\\?))");
            if (blockSplit[0].contains(":")) {                                            // if speaker is denoted by :
                currentCharacter = blockSplit[0].split("\\:")[0];                       // rather than first sentence.
                blockSplit[0] = blockSplit[0].split("\\:")[1];       // ensures first sentence is not passed over
                colonUsed = true;
            } else if (!blockSplit[0].matches("\\s*")) {// stops array index out of bounds
                currentCharacter = blockSplit[0].substring(0, blockSplit[0].length() - 1);
                colonUsed = false;
            }
            if (blockSplit[0].equals("TABLEAU")) {
                return characterSentiment;
            }
            if (blockSplit[0].endsWith("ACT")) {
                currentAct = blockSplit[0];
            }
            System.out.println("CURRENT CHARACTER:  " + currentCharacter);
            System.out.println("charsent size:      " + characterSentiment.size());

            //System.out.println(Arrays.toString(blockSplit));
            //System.out.println(blockSplit[0]);
            int characterAmount = 0;
            if (i == 0) {
                characterAmount = characters.size();
            }

            if (characterSentiment.size() != characters.size()) {
                for (int j = 0; j < characters.size(); j++) {
                    listCharacter = (String) characters.get(j);

//                System.out.println("Character:  " + listCharacter);
//                System.out.println("bsplit:      " + currentCharacter);
                    if (listCharacter.contains(currentCharacter)) {
                        System.out.println(block);
                        if (!characterSentiment.containsKey(currentCharacter)) {
                            characterSentiment.put(currentCharacter, new ArrayList<PlaySD>());
                            characters.remove(j);       // cut down on loops
                        }
                    }
                }
            }

            int j = 1;
            if (colonUsed) {
                j = 0;
            }  // if colon is used to denote a speaker, this ensures 1st sentence is analysed
            for (j = j; j < blockSplit.length; j++) {
                currentSentence = blockSplit[j].trim();
                if (!currentSentence.matches("\\s*\\[.*\\.")) {
                    if (currentSentence.startsWith("] ")) {
                        currentSentence = currentSentence.substring(2);
                    }
                    if (!currentSentence.equals("]")) {
                        currentSentence = currentSentence.replaceAll("\\[.*\\]", "");
                        if (characterSentiment.containsKey(currentCharacter)) {
                            int sentiment = sentimentAnalyser.determineSentenceSentiment(currentSentence);
                            PlaySD playSD = new PlaySD(currentCharacter, currentSentence, sentiment,
                                    currentAct, playSentencePosition);
                            characterSentiment.get(currentCharacter).add(playSD);
                            allSentiment.add(playSD);
                            playSentencePosition++;
                        }
                    }
                }
            }
        }

        for (String key : characterSentiment.keySet()) {

            ArrayList temp = characterSentiment.get(key);

            for (int i = 0; i < temp.size(); i++) {

            }
        }

        return characterSentiment;
    }

    public void fileWriter(String playTitle, ArrayList<PlaySD> allSent) {
        try {
            File file = new File("src/advanced/out/Play/" + playTitle + "-all-sentiment.csv");
            file.getParentFile().mkdirs();
            PrintWriter wrtr = new PrintWriter(file);


            for (int i = 0; i < allSent.size(); i++) {
                PlaySD playSD = allSent.get(i);
                wrtr.println(playSD.getPSP() + "§" + playSD.getNormSent() + "§" +
                        playSD.getCharacter() + "§" + playSD.getSentence() + "§" + playSD.getAct() +
                        "§" + playSD.getScene());

            }
            wrtr.close();


        } catch (Exception e) {
            System.out.println("Error Writing File");
        }

    }
}
