package advanced;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentPipeline;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;


/**
 * Created by carlmccann2 on 02/11/15.
 */
public class SentimentAnalyser {


    static StanfordCoreNLP pipeline;
    static SentimentPipeline smPipeline;


//    public static void test2(){
//        Annotator pipeline = new StanfordCoreNLP();
//        Annotation annotation = new Annotation("Can you parse my sentence?");
//        pipeline.annotate((edu.stanford.nlp.pipeline.Annotation) annotation);
//    }

//    public static void test() {
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
////        props.setProperty("annotators", "tokenize,ssplit");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//
//
//        String text = "hi carl how are you";
//        edu.stanford.nlp.pipeline.Annotation document = new edu.stanford.nlp.pipeline.Annotation(text);
//
//        pipeline.annotate(document);
//
//        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//        for (CoreMap sentence : sentences) {
//            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                String word = token.get(CoreAnnotations.TextAnnotation.class);
//                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//            }
//
//
//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//        }
//
//
//        Map<Integer,CorefChain> graph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
//    }

//    public SentimentAnalyser(){
//
//    }

    public static void main(String args[]) {

        //SentimentAnalyser.init();
        SentimentAnalyser sA = new SentimentAnalyser();
        sA.init();


        sA.determineSentenceSentiment("This was the worst day of my life, my wife left with the kids, and I have no home.");
        sA.determineSentenceSentiment("I don't know half of you half as well as I should like, and I like less than half of you half as well as you deserve");
        sA.determineSentenceSentiment("The food was okay");
        sA.determineSentenceSentiment("The food was good");
        sA.determineSentenceSentiment("I'm so happy you're here!");


        //test();
    }

    public void init() {
        pipeline = new StanfordCoreNLP("MyPropFile.properties");
    }

    public int determineSentenceSentiment(String sentence) {

        edu.stanford.nlp.pipeline.Annotation annotation = pipeline.process(sentence);
        int mainSentiment = 0;

        if (sentence != null && sentence.length() > 0) {

            int longest = 0;
            for (CoreMap sent : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sent.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sent.toString();
                System.out.println();
                System.out.println(partText);
                System.out.println("Parse Tree: " + tree);
                System.out.println("Sentiment Value: " + sentiment);
                System.out.println();

                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        return mainSentiment;
    }
}
