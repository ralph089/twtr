package edu.hm.ccwi.semantic.tagger.models;

import com.twitter.Extractor;
import edu.hm.ccwi.semantic.commons.twitter.Tweet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A fully-tagged Tweet.
 * <p>
 * Contains
 * - the untagged {@link Tweet}
 * - a list of {@link TaggedSentence}
 * - a list of mentioned twitter accounts
 * - a list of hashtags
 *
 * @author Ralph Offinger
 */
public class TaggedTweet {

    private Tweet tweet;
    private List<TaggedSentence> taggedSentences;
    private List<String> mentionesList;
    private List<String> hashtagsList;

    /**
     * Instantiates a new Tagged tweet.
     *
     * @param tweet           the tweet
     * @param taggedSentences the pos tagged sentences
     */
    public TaggedTweet(Tweet tweet, List<TaggedSentence> taggedSentences) {
        this.tweet = tweet;
        this.taggedSentences = taggedSentences;

        Extractor extractor = new Extractor();
        this.hashtagsList = (ArrayList<String>) extractor.extractHashtags(this.tweet.getTweetText());
        this.mentionesList = (ArrayList<String>) extractor.extractMentionedScreennames(this.tweet.getTweetText());
    }

    /**
     * Gets mentiones list.
     *
     * @return the mentiones list
     */
    public List<String> getMentionesList() {
        return mentionesList;
    }

    /**
     * Gets hashtags list.
     *
     * @return the hashtags list
     */
    public List<String> getHashtagsList() {
        return hashtagsList;
    }

    /**
     * Gets tweet.
     *
     * @return the tweet
     */
    public Tweet getTweet() {
        return tweet;
    }

    /**
     * Gets pos tagged sentences.
     *
     * @return the pos tagged sentences
     */
    public List<TaggedSentence> getTaggedSentences() {
        return taggedSentences;
    }

    /**
     * Exports the {@link TaggedTweet} to a CSV file.
     *
     * @param resourceUrl the resource url
     */
    public void toCSV(String resourceUrl) {

        char CSV_DELIMITER = ';';

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
            sb.append("Satz");
            sb.append(CSV_DELIMITER);
            sb.append("Subjekt");
            sb.append(CSV_DELIMITER);
            sb.append("Verb");
            sb.append(CSV_DELIMITER);
            sb.append("Objekt");
            sb.append('\n');
        }

        for (TaggedSentence sentence : taggedSentences) {
            for (Triplet triplet : sentence.getTriplets()) {
                sb.append(tweet.getTweetID());
                sb.append(CSV_DELIMITER);
                sb.append(sentence.getSentence());
                sb.append(CSV_DELIMITER);
                sb.append(triplet.getSubject().getWord());
                sb.append(CSV_DELIMITER);
                sb.append(triplet.getVerb().getWord());
                sb.append(CSV_DELIMITER);
                sb.append(triplet.getObject().getWord());
                sb.append('\n');
            }
        }

        pw.write(sb.toString());
        pw.close();
    }
}
