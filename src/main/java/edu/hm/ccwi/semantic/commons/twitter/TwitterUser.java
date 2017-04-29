package edu.hm.ccwi.semantic.commons.twitter;


import org.apache.commons.lang3.Validate;

/**
 * A Twitter user.
 *
 * @author Ralph Offinger
 */
public class TwitterUser {

    private final int followerCount;
    private final String userID;
    private final String userName;
    private final String userDescription;
    private final String type;

    /**
     * Instantiates a new Twitter user.
     *
     * @param followerCount   the follower count
     * @param userID          the user id
     * @param userName        the user name
     * @param userDescription the user description
     * @param type            the type
     */
    public TwitterUser(int followerCount, String userID, String userName, String userDescription, String type) {
        this.followerCount = Validate.notNull(followerCount, "Empty Follower Count is not allowed!");
        this.userID = Validate.notNull(userID, "Empty User ID ist not allowed!");
        this.userName = Validate.notNull(userName, "Empty Username is not allowed!");
        this.userDescription = Validate.notNull(userDescription);
        this.type = Validate.notNull(type);
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
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
