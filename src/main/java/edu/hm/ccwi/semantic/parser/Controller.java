package edu.hm.ccwi.semantic.parser;

import edu.hm.ccwi.semantic.parser.relational.RelationalEntry;
import edu.hm.ccwi.semantic.parser.relational.RelationalParser;
import edu.hm.ccwi.semantic.parser.tagger.WatsonTagger;
import edu.hm.ccwi.semantic.parser.tagger.StanfordTagger;
import edu.hm.ccwi.semantic.parser.tagger.Tagger;
import edu.hm.ccwi.semantic.parser.tagger.model.TaggedTweet;

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
        List<Tagger> taggers = Arrays.asList(new WatsonTagger(), new StanfordTagger());

        for (RelationalEntry entry : twitterData) {

            for (Tagger tagger : taggers) {
                TaggedTweet taggedTweet = tagger.tagTweet(entry);
                System.out.println(String.format("Ergebnis für Tweet: %s", taggedTweet.getTweetId()));
                taggedTweet.getAnalysedTweet().forEach((sentence, triplet) ->
                        System.out.println(String.format("%s Ergebnis für Satz: '%s'\n%s", taggedTweet.getTagger(), sentence, triplet)));
                taggedTweet.toCSV("src/main/resources/tweets/taggedTweets.csv");
            }
        }
    }
}
