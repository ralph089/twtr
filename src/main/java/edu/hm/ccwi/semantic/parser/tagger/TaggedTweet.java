package edu.hm.ccwi.semantic.parser.tagger;

import java.io.*;
import java.util.HashMap;


/**
 * A POS-tagged Tweet.
 *
 * @author Ralph Offinger
 */
public class TaggedTweet {

    private String tweetID;
    private TwitterUser twitterUser;
    private String tweetText;
    private HashMap<String, Triplet> analysedTweet;
    private String tagger;

    /**
     * Instantiates a new TaggedTweet.
     *
     * @param tweetID       the tweet id
     * @param twitterUser   the twitter user
     * @param tweetText     the tweet text
     * @param analysedTweet The sentence of the tweet and the Subject-Verb-Object {@link Triplet}
     * @param tagger        the {@link Tagger} name
     */
    public TaggedTweet(String tweetID, TwitterUser twitterUser, String tweetText, HashMap<String, Triplet> analysedTweet, String tagger) {
        this.tweetID = tweetID;
        this.twitterUser = twitterUser;
        this.tweetText = tweetText;
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
            sb.append(tweetID);
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
    public String getTweetID() {
        return tweetID;
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

    public TwitterUser getTwitterUser() {
        return twitterUser;
    }

    public String getTweetText() {
        return tweetText;
    }
}
