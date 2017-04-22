package edu.hm.ccwi.semantic.parser.tagger;


/**
 * A Twitter user.
 *
 * @author Ralph Offinger
 */
public class TwitterUser {

    private int followerCount;
    private String userID;
    private String userName;
    private String userDescription;

    /**
     * Instantiates a new Twitter user.
     *
     * @param followerCount   the follower count
     * @param userID          the user id
     * @param userName        the user name
     * @param userDescription the user description
     */
    public TwitterUser(int followerCount, String userID, String userName, String userDescription) {
        this.followerCount = followerCount;
        this.userID = userID;
        this.userName = userName;
        this.userDescription = userDescription;
    }

    /**
     * Gets follower count.
     *
     * @return the follower count
     */
    public int getFollowerCount() {
        return followerCount;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets user description.
     *
     * @return the user description
     */
    public String getUserDescription() {
        return userDescription;
    }
}
