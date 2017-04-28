package edu.hm.ccwi.semantic;

import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.parser.RelationalParser;
import edu.hm.ccwi.semantic.rdf.TwtrArq;
import edu.hm.ccwi.semantic.rdf.TwtrModel;
import edu.hm.ccwi.semantic.tagger.StanfordTagger;
import edu.hm.ccwi.semantic.tagger.models.TaggedTweet;
import edu.hm.ccwi.semantic.tagger.ner.StanfordNER;
import edu.hm.ccwi.semantic.tagger.triplet.StanfordTripletTagger;
import edu.hm.ccwi.semantic.tagger.triplet.WatsonTripletTagger;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Arrays;
import java.util.List;

/**
 * The Controller.
 */
public class Controller {

    /**
     * Main.
     *
     * @param args the args
     */
    static Logger log = Logger.getLogger(Controller.class);

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(final String[] args) {

        String log4jConfPath = "src/main/resources/log/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        log.info("Start!");

        // #1 parse relational Data
        List<RelationalEntry> twitterData = new RelationalParser()
                .parseRelationalExportedData("src/main/resources/tweets/twitter_IoT_pared_short.csv");

        // #2 nlp on data
        List<StanfordTagger> taggers = Arrays.asList(new StanfordTagger(new StanfordTripletTagger(), new StanfordNER()));

        TwtrModel twitterModel = new TwtrModel();
        TwtrArq twtrArq = new TwtrArq(twitterModel);

        for (RelationalEntry entry : twitterData) {
            log.info(String.format("Parsing Tweet: %s", entry.getTweetText()));

            for (StanfordTagger tagger : taggers) {
                try {
                    TaggedTweet taggedTweet = tagger.tagTweet(entry);
                    if (taggedTweet != null) {
                        twitterModel.addTweet(taggedTweet);
                        taggedTweet.toCSV("src/main/resources/tweets/Stanford_Triplet_Tagger.csv");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        twitterModel.write(System.out);

        twtrArq.printTweetsWithTriplet();
        twtrArq.printSubjectCommonNouns();
    }
}
