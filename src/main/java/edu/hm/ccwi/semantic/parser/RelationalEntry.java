package edu.hm.ccwi.semantic.parser;

import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;

/**
 * A representation of a Twitter CSV entry exported from a relational database.
 *
 * @author Max Auch
 */
@CsvDataType()
public class RelationalEntry {

    @CsvField(pos = 1)
    private String tweet_id;

    @CsvField(pos = 2)
    private String dbidTwitterUser;

    @CsvField(pos = 3)
    private String accountAgeInDays;

    @CsvField(pos = 4)
    private String statuses_count;

    @CsvField(pos = 5)
    private String follower_count;

    @CsvField(pos = 6)
    private String friends_count;

    @CsvField(pos = 7)
    private String listed_count;
    @CsvField(pos = 8)
    private String location;
    @CsvField(pos = 9)
    private String username;
    @CsvField(pos = 10)
    private String userId;
    @CsvField(pos = 11)
    private String userDescription;
    @CsvField(pos = 12)
    private String tweetText;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets dbid twitter user.
     *
     * @return the dbid twitter user
     */
    public String getDbidTwitterUser() {
        return dbidTwitterUser;
    }

    /**
     * Sets dbid twitter user.
     *
     * @param dbidTwitterUser the dbid twitter user
     */
    public void setDbidTwitterUser(String dbidTwitterUser) {
        this.dbidTwitterUser = dbidTwitterUser;
    }

    /**
     * Gets tweet id.
     *
     * @return the tweet id
     */
    public String getTweet_id() {
        return tweet_id;
    }

    /**
     * Sets tweet id.
     *
     * @param tweet_id the tweet id
     */
    public void setTweet_id(String tweet_id) {
        this.tweet_id = tweet_id;
    }

    /**
     * Gets account age in days.
     *
     * @return the account age in days
     */
    public String getAccountAgeInDays() {
        return accountAgeInDays;
    }

    /**
     * Sets account age in days.
     *
     * @param accountAgeInDays the account age in days
     */
    public void setAccountAgeInDays(String accountAgeInDays) {
        this.accountAgeInDays = accountAgeInDays;
    }

    /**
     * Gets statuses count.
     *
     * @return the statuses count
     */
    public String getStatuses_count() {
        return statuses_count;
    }

    /**
     * Sets statuses count.
     *
     * @param statuses_count the statuses count
     */
    public void setStatuses_count(String statuses_count) {
        this.statuses_count = statuses_count;
    }

    /**
     * Gets follower count.
     *
     * @return the follower count
     */
    public int getFollower_count() {
        try {
            return Integer.parseInt(follower_count);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Sets follower count.
     *
     * @param follower_count the follower count
     */
    public void setFollower_count(String follower_count) {
        this.follower_count = follower_count;
    }

    /**
     * Gets friends count.
     *
     * @return the friends count
     */
    public String getFriends_count() {
        return friends_count;
    }

    /**
     * Sets friends count.
     *
     * @param friends_count the friends count
     */
    public void setFriends_count(String friends_count) {
        this.friends_count = friends_count;
    }

    /**
     * Gets listed count.
     *
     * @return the listed count
     */
    public String getListed_count() {
        return listed_count;
    }

    /**
     * Sets listed count.
     *
     * @param listed_count the listed count
     */
    public void setListed_count(String listed_count) {
        this.listed_count = listed_count;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets user description.
     *
     * @return the user description
     */
    public String getUserDescription() {
        return userDescription;
    }

    /**
     * Sets user description.
     *
     * @param userDescription the user description
     */
    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    /**
     * Gets tweet text.
     *
     * @return the tweet text
     */
    public String getTweetText() {
        return tweetText;
    }

    /**
     * Sets tweet text.
     *
     * @param tweetText the tweet text
     */
    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }
}
