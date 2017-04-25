package edu.hm.ccwi.semantic.commons.twitter;

import edu.hm.ccwi.semantic.tagger.Tagger;
import edu.hm.ccwi.semantic.tagger.Triplet;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.util.HashMap;
import java.util.List;


/**
 * A POS-tagged Tweet.
 *
 * @author Ralph Offinger
 */
public class TaggedTweet extends Tweet {

    /**
     * The constant CSV_DELIMITER.
     */
    public static final char CSV_DELIMITER = ';';

    private HashMap<String, Triplet> triplet;
    private String tagger;

    private Entity properNoun;

    private String adjective;
    private List<String> commonNouns;


    /**
     * Instantiates a tagged Tweet.
     *
     * @param tweetID     the tweet id
     * @param twitterUser a {@link TwitterUser}
     * @param tweetText   the text of the tweet
     * @param triplet     the sentence of the tweet and the related Subject, Verb, Object-{@link Triplet}
     * @param tagger      the name of the {@link Tagger}
     * @param properNoun  the proper noun
     * @param commonNouns the common nouns
     */
    public TaggedTweet(String tweetID, TwitterUser twitterUser, String tweetText, HashMap<String, Triplet> triplet, String tagger, Entity properNoun, List<String> commonNouns) {
        super(tweetID, twitterUser, tweetText);
        this.triplet = Validate.notEmpty(triplet);
        this.tagger = Validate.notBlank(tagger);
        this.properNoun = properNoun;
        this.commonNouns = commonNouns;
    }

    /**
     * Exports the SVO-{@link TaggedTweet} to a CSV file.
     *
     * @param resourceUrl the resource url
     */
    public void toCSV(String resourceUrl) {

        PrintWriter pw = null;

        final File file = new File(resourceUrl);

        boolean alreadyExists = file.exists();

        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (!alreadyExists) {
            sb.append("TweetID");
            sb.append(CSV_DELIMITER);
            sb.append("Tagger");
            sb.append(CSV_DELIMITER);
            sb.append("Satz");
            sb.append(CSV_DELIMITER);
            sb.append("Subjekt");
            sb.append(CSV_DELIMITER);
            sb.append("Verb");
            sb.append(CSV_DELIMITER);
            sb.append("Objekt");
            sb.append('\n');
        }

        triplet.forEach((sentence, triplet) -> {
            sb.append(this.getTweetID());
            sb.append(CSV_DELIMITER);
            sb.append(tagger);
            sb.append(CSV_DELIMITER);
            sb.append(sentence.replace(";", ","));
            sb.append(CSV_DELIMITER);
            sb.append(triplet.getSubject());
            sb.append(CSV_DELIMITER);
            sb.append(triplet.getVerb());
            sb.append(CSV_DELIMITER);
            sb.append(triplet.getObject());
            sb.append('\n');
        });

        pw.write(sb.toString());
        pw.close();
    }

    /**
     * Returns the tagger.
     *
     * @return the tagger
     */
    public String getTagger() {
        return tagger;
    }

    /**
     * Returns the analysed tweet.
     *
     * @return the analysed tweet
     */
    public HashMap<String, Triplet> getTriplet() {
        return triplet;
    }

    /**
     * Gets proper noun.
     *
     * @return the proper noun
     */
    public Entity getProperNoun() {
        return properNoun;
    }

}
