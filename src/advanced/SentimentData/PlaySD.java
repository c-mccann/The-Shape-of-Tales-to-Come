package advanced.SentimentData;

/**
 * Created by carlmccann2 on 13/01/2016.
 * Data Structure to Hold Sentence sentiment data for plays
 */
public class PlaySD {
    private String character;
    private String sentence;
    private int sentiment;
    private double normalisedSent;
    private String act;
    private String scene;
    private int playSentencePosition;

    public PlaySD(String character, String sentence, int sentiment, String act, int playSentencePosition) {
        this.character = character;
        this.sentence = sentence.trim();
        this.sentiment = sentiment;
        this.act = act;
        this.normalisedSent = 0.5 * (sentiment - 2);
        this.playSentencePosition = playSentencePosition;
    }

    public void dataPrinter() {
        System.out.println("Character:                    " + character);
        System.out.println("Sentence:                     " + sentence);
        System.out.println("Sentiment:                    " + sentiment);
        System.out.println("Normalised:                   " + normalisedSent);
        System.out.println("Act:                          " + act);
        System.out.println("Play Sentence Position:       " + playSentencePosition);
        System.out.println();
    }

    public double getNormSent() {
        return normalisedSent;
    }

    public double getSentiment() {
        return sentiment;
    }

    public int getPSP() {
        return playSentencePosition;
    }

    public String getCharacter() {
        return character;
    }

    public String getSentence() {
        return sentence;
    }

    public String getAct() {
        return act;
    }

    public String getScene() {
        return scene;
    }
}
