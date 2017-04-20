package edu.hm.ccwi.semantic.parser.tagger.model;

import edu.hm.ccwi.semantic.parser.tagger.Tagger;

import java.io.*;
import java.util.HashMap;


/**
 * A POS-tagged Tweet.
 */
public class TaggedTweet {

    private String tweetId;
    private HashMap<String, Triplet> analysedTweet;
    private String tagger;

    /**
     * Instantiates a new TaggedTweet.
     *
     * @param tweetId       the tweet id
     * @param analysedTweet The sentence of the tweet and the Subject-Verb-Object {@link Triplet}
     * @param tagger        the {@link Tagger} name
     */
    public TaggedTweet(String tweetId, HashMap analysedTweet, String tagger) {
        this.tweetId = tweetId;
        this.analysedTweet = analysedTweet;
        this.tagger = tagger;
    }

    /**
     * Exports the TaggedTweet to a CSV file.
     *
     * @param resourceUrl the resource url
     */
    public void toCSV(String resourceUrl) {

        PrintWriter pw = null;

        final File file = new File(resourceUrl);

        boolean alreadyExists = file.exists();

        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        if (!alreadyExists) {
            sb.append("TweetID");
            sb.append(';');
            sb.append("Tagger");
            sb.append(';');
            sb.append("Satz");
            sb.append(';');
            sb.append("Subjekt");
            sb.append(';');
            sb.append("Verb");
            sb.append(';');
            sb.append("Objekt");
            sb.append('\n');
        }

        analysedTweet.forEach((sentence, triplet) -> {
            sb.append(tweetId);
            sb.append(';');
            sb.append(tagger);
            sb.append(';');
            sb.append(sentence.replace(";", ","));
            sb.append(';');
            sb.append(triplet.getSubject());
            sb.append(';');
            sb.append(triplet.getVerb());
            sb.append(';');
            sb.append(triplet.getObject());
            sb.append('\n');
        });

        pw.write(sb.toString());
        pw.close();
    }

    /**
     * Gets tweet id.
     *
     * @return the tweet id
     */
    public String getTweetId() {
        return tweetId;
    }

    /**
     * Gets the tagger.
     *
     * @return the tagger
     */
    public String getTagger() {
        return tagger;
    }

    /**
     * Gets the analysed tweet.
     *
     * @return the analysed tweet
     */
    public HashMap<String, Triplet> getAnalysedTweet() {
        return analysedTweet;
    }
}
