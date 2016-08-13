package advanced.Processors;

import advanced.SentimentAnalyser;
import advanced.SentimentData.ShakespeareSD;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by carlmccann2 on 10/02/2016.
 * Processes shakespearean plays, could possibly be used for other plays of the same format
 */
public class ShakespeareanProcessor {
    public ArrayList<ShakespeareSD> allSentiment = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        String test = "SCENE IV. The platform.";
        String test2 = "ACT II";


        ShakespeareanProcessor sP = new ShakespeareanProcessor();

//        try {
//            ArrayList blocks =  sP.fileOpen2("Hamlet.txt");
//            for (int i = 0; i < blocks.size() ; i++) {
//                System.out.println(blocks.get(i)+":     " + sP.stringIsAllUpper((String)blocks.get(i)));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        ArrayList blocks = sP.fileOpen2("Hamlet.txt");
        HashMap hM = sP.sentimentAnalyser(blocks);

        for (Object key : hM.keySet()) {

            System.out.println("_____________________________________________________________________________________");
            ArrayList aL = (ArrayList) hM.get(key);
            for (int i = 0; i < aL.size(); i++) {
                ShakespeareSD ssSD = (ShakespeareSD) aL.get(i);
                ssSD.dataPrinter();


            }

        }

        sP.fileWriter2("Hamlet", sP.allSentiment);

        //sP.fileWriter("Hamlet", hM);


    }

    public ArrayList fileOpen2(String fileName) throws FileNotFoundException {       // returns blocks of writing
        String currentBlock = null;
        String filePath = "src/res/Shakespearean/";
        filePath += fileName;
        System.out.println("Initiating File Reader on:      " + filePath);
        ArrayList<String> blocks = new ArrayList<>();
        try {
            BufferedReader rdrr = new BufferedReader(new FileReader(filePath));
            String currentLine = rdrr.readLine();

            while (currentLine != null) {
                if (!currentLine.matches("^\\s*$")) {
                    if (!currentLine.matches("((Exeunt)|(Enter)|(Exit)).*")) {
                        if (currentBlock == null) {
                            currentBlock = currentLine;
                        } else {
                            currentBlock += " " + currentLine;
                        }
                    }
                } else {
                    if (currentBlock != null) {
                        blocks.add(currentBlock.replaceAll("\\[.*\\]\\s*", "").trim());
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
        return blocks;
    }

    public boolean stringIsAllUpper(String line) {
        if (line.matches("^\\s*$")) {
            return false;
        }
        if (line.matches("^([A-Z]+|^[A-Z]+\\s)*")) {
            return true;
        }
        for (int i = 0; i < line.length(); i++) {              // might not be necessary if regex works fully
            if (!Character.isUpperCase(line.charAt(i)) && !Character.isWhitespace(line.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public HashMap sentimentAnalyser(ArrayList blocks) {
        String previousCharacter = null;
        String currentCharacter = null;
        String currentAct = null;
        String currentScene = null;
        boolean lastLineIsCharacter = false;
        int playSentencePosition = 1;

        HashMap<String, ArrayList<ShakespeareSD>> characterSentiment = new HashMap<>();

        SentimentAnalyser sentimentAnalyser = new SentimentAnalyser();
        sentimentAnalyser.init();

        String actOne = "ACT I";
        int startOfPlay = 0;
        if (blocks.contains(actOne)) {
            startOfPlay = blocks.indexOf(actOne);
        }

        System.out.println(startOfPlay);
        for (int i = startOfPlay; i < blocks.size(); i++) {

            String block = (String) blocks.get(i);
            String blockSplit[] = block.split("(?<=(\\.|\\!|\\?))");

            if (stringIsAllUpper(blockSplit[0])) {
                if (lastLineIsCharacter) {
                    previousCharacter = currentCharacter;
                }
                currentCharacter = blockSplit[0];
                if (!characterSentiment.containsKey(currentCharacter)) {
                    characterSentiment.put(currentCharacter, new ArrayList<ShakespeareSD>());
                }
                lastLineIsCharacter = true;
            } else if (blockSplit[0].matches("(SCENE).*")) {
                currentScene = blockSplit[0].replaceAll("\\.", "");
                lastLineIsCharacter = false;
            } else if (blockSplit[0].matches("(ACT).*")) {
                currentAct = blockSplit[0].replaceAll("\\s(SCENE).*", "");
                lastLineIsCharacter = false;
            } else {
                for (int j = 0; j < blockSplit.length; j++) {

//                    if(blockSplit[j].matches("((Exeunt)|(Enter)|(Exit)).*\n")){
//                        blockSplit[j] = blockSplit[j].replaceAll("((Exeunt)|(Enter)|(Exit)).*\n","");
//                    }
//                    else {
                    if (characterSentiment.containsKey(currentCharacter)) {
                        int sentiment = sentimentAnalyser.determineSentenceSentiment(blockSplit[j]);
                        ShakespeareSD ssSD = new ShakespeareSD(currentCharacter, blockSplit[j], sentiment,
                                currentAct, currentScene, playSentencePosition);
                        characterSentiment.get(currentCharacter).add(ssSD);
                        allSentiment.add(ssSD);
                        if (lastLineIsCharacter && previousCharacter != null) {
                            if (!previousCharacter.equals(currentCharacter)) {
                                ShakespeareSD ssSD2 = new ShakespeareSD(previousCharacter, blockSplit[j], sentiment,
                                        currentAct, currentScene, playSentencePosition);
                                characterSentiment.get(previousCharacter).add(ssSD2);
                                allSentiment.add(ssSD);
                                lastLineIsCharacter = false;
                                previousCharacter = null;

                            }
                        }
                        playSentencePosition++;
                    }
                    //}
                }
            }
        }
        return characterSentiment;
    }

    public void fileWriter2(String playTitle, ArrayList<ShakespeareSD> allSent) {
        try {
            File file = new File("src/advanced/out/Shakespearean/" + playTitle + "-all-sentiment.csv");
            file.getParentFile().mkdirs();
            PrintWriter wrtr = new PrintWriter(file);


            for (int i = 0; i < allSent.size(); i++) {
                ShakespeareSD ssSD = allSent.get(i);
                wrtr.println(ssSD.getPSP() + "§" + ssSD.getNormSent() + '§' +
                        ssSD.getCharacter() + '§' + ssSD.getSentence() + '§' + ssSD.getAct() +
                        "§" + ssSD.getScene());

            }
            wrtr.close();


        } catch (Exception e) {
            System.out.println("Error Writing File");
        }

    }

    public void fileWriter(String playTitle, HashMap<String, ArrayList<ShakespeareSD>> characterSentiment) {

        for (String key : characterSentiment.keySet()) {


            try {
                Writer output = null;
                File file = new File("src/advanced/out/Shakespearean/" + playTitle + "/" + key + "raw-sentiment.txt");
                output = new BufferedWriter(new FileWriter(file));
                ArrayList characterData = characterSentiment.get(key);

                for (int i = 0; i < characterData.size(); i++) {
                    ShakespeareSD ssSD = (ShakespeareSD) characterData.get(i);
                    output.append(ssSD.getNormSent() + ", " + ssSD.getPSP());

                }


            } catch (Exception e) {
                System.out.println("Error Writing File");
            }

        }


//        try{
//            Writer output = null;
//            File file = new File("src/simple/out/"+character+"-sentiment.csv");
//
//            output = new BufferedWriter(new FileWriter(file));
//            output.append(characterCount.get(character) + ", END\n");
//
//            HashMap<Integer,String> tHash = characters.get(character);
//            for(Integer key : tHash.keySet()){
//                output.append(key + ", " + tHash.get(key) + "\n");
//            }
//            output.close();
//            System.out.println(character + " sentiment file has been written");
//        }catch(Exception e){
//            System.out.println("Error Writing File");
//        }
    }
}
