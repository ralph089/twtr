package edu.hm.ccwi.semantic;

import edu.hm.ccwi.semantic.commons.twitter.TaggedTweet;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.parser.RelationalParser;
import edu.hm.ccwi.semantic.rdf.TwtrArq;
import edu.hm.ccwi.semantic.rdf.TwtrModel;
import edu.hm.ccwi.semantic.tagger.Tagger;
import edu.hm.ccwi.semantic.tagger.watson.WatsonTagger;

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
    public static void main(final String[] args) {

        // #1 parse relational Data
        List<RelationalEntry> twitterData = new RelationalParser()
                .parseRelationalExportedData("src/main/resources/tweets/tweet_sample_no_RT_EN_short.csv");

        // #2 nlp on data
        List<Tagger> taggers = Arrays.asList(new WatsonTagger(5));

        TwtrModel twitterModel = new TwtrModel();
        TwtrArq twtrArq = new TwtrArq(twitterModel);

        for (RelationalEntry entry : twitterData) {

            for (Tagger tagger : taggers) {
                TaggedTweet taggedTweet = tagger.tagTweet(entry);
                if (taggedTweet != null) {
                    taggedTweet.getTriplet().forEach((sentence, triplet) ->
                            System.out.println(String.format("%s Ergebnis für Satz: '%s'\n%s", taggedTweet.getTagger(), sentence, triplet)));
                    twitterModel.addTweet(taggedTweet);
                }
            }
        }
        twitterModel.write(System.out);

        twtrArq.printTweetsWithProperNoun();

    }
}
