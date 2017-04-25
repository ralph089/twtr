package edu.hm.ccwi.semantic.commons.twitter;


import org.apache.commons.lang3.Validate;

/**
 * A Tweet.
 *
 * @author Ralph Offinger
 */
public class Tweet {

    private final String tweetID;
    private final TwitterUser twitterUser;
    private final String tweetText;

    /**
     * Instantiates a new Tweet.
     *
     * @param tweetID     the tweet id
     * @param twitterUser the twitter user
     * @param tweetText   the tweet text
     */
    public Tweet(String tweetID, TwitterUser twitterUser, String tweetText) {
        this.tweetID = Validate.notNull(tweetID, "Empty Tweet ID ist not allowed!");
        this.twitterUser = Validate.notNull(twitterUser, "Empty Twitter User not allowed!");
        this.tweetText = Validate.notNull(tweetText, "Empty Text not allowed!");
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
     * Gets twitter user.
     *
     * @return the twitter user
     */
    public TwitterUser getTwitterUser() {
        return twitterUser;
    }

    /**
     * Gets tweet text.
     *
     * @return the tweet text
     */
    public String getTweetText() {
        return tweetText;
    }
}
