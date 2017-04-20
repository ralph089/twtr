package edu.hm.ccwi.semantic.parser.relational;

import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;

/**
 * A representation of a Twitter CSV entry exported from a relational database.
 */
@CsvDataType()
public class RelationalEntry {

    @CsvField(pos = 1)
    private String resultid;

    @CsvField(pos = 2)
    private String dbidTwitterUser;

    @CsvField(pos = 3)
    private String tweet_id;

    @CsvField(pos = 4)
    private String accountAgeInDays;

    @CsvField(pos = 5)
    private String statuses_count;

    @CsvField(pos = 6)
    private String follower_count;

    @CsvField(pos = 7)
    private String friends_count;

    @CsvField(pos = 8)
    private String listed_count;

    @CsvField(pos = 9)
    private String strategic_position;

    @CsvField(pos = 10)
    private String activity;

    @CsvField(pos = 11)
    private String activityNegativ;

    @CsvField(pos = 12)
    private String username;

    @CsvField(pos = 13)
    private String userId;

    @CsvField(pos = 14)
    private String userDescription;

    @CsvField(pos = 15)
    private String tweetText;


    /**
     * Gets resultid.
     *
     * @return the resultid
     */
    public String getResultid() {
        return resultid;
    }

    /**
     * Sets resultid.
     *
     * @param resultid the resultid
     */
    public void setResultid(String resultid) {
        this.resultid = resultid;
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
    public String getFollower_count() {
        return follower_count;
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
     * Gets strategic position.
     *
     * @return the strategic position
     */
    public String getStrategic_position() {
        return strategic_position;
    }

    /**
     * Sets strategic position.
     *
     * @param strategic_position the strategic position
     */
    public void setStrategic_position(String strategic_position) {
        this.strategic_position = strategic_position;
    }

    /**
     * Gets activity.
     *
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Sets activity.
     *
     * @param activity the activity
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * Gets activity negativ.
     *
     * @return the activity negativ
     */
    public String getActivityNegativ() {
        return activityNegativ;
    }

    /**
     * Sets activity negativ.
     *
     * @param activityNegativ the activity negativ
     */
    public void setActivityNegativ(String activityNegativ) {
        this.activityNegativ = activityNegativ;
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
