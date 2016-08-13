package advanced.SentimentData;

/**
 * Created by carlmccann2 on 13/01/2016.
 * Data Structure to Hold Sentence sentiment data for movies
 */
public class MovieSD {
    private String character;
    private String sentence;
    private int sentiment;
    private double normalisedSent;
    private String scene;
    private int charactersSentencePosition;
    private int movieSentencePosition;

    /*
    formula for normalising the 0-4 inclusive sentiment values

    1   =   m * (max + c)
    -1  =   m * (min + c)

    1   =   m * (4 + c)
    -1  =   m * (0 + c)

    after solving simultaneous equations
    m   =   0.5
    c   =   -2

    normalisedSent  =   0.5 * (sentiment - 2)

     */

    public MovieSD(String character, String sentence, int sentiment, String scene,
                   int charactersWordPosition, int movieWordPosition) {
        this.character = character;
        this.sentence = sentence.trim();
        this.sentiment = sentiment;
        this.scene = scene;
        this.normalisedSent = 0.5 * (sentiment - 2);
        this.charactersSentencePosition = charactersWordPosition;
        this.movieSentencePosition = movieWordPosition;


    }

    public void dataPrinter() {
        System.out.println("Character:                    " + character);
        System.out.println("Sentence:                     " + sentence);
        System.out.println("Sentiment:                    " + sentiment);
        System.out.println("Normalised:                   " + normalisedSent);
        System.out.println("Scene:                        " + scene);
        System.out.println("Characters Sentence Position: " + charactersSentencePosition);
        System.out.println("movie Sentence Position:      " + movieSentencePosition);
        System.out.println();
    }

    public double getNormSent() {
        return normalisedSent;
    }

    public double getSentiment() {
        return sentiment;
    }

    public int getMSP() {
        return movieSentencePosition;
    }

    public String getCharacter() {
        return character;
    }

    public String getSentence() {
        return sentence;
    }

    public String getScene() {
        return scene;
    }

    public int getCSP() {
        return charactersSentencePosition;
    }
}
