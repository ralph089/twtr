package edu.hm.ccwi.semantic.commons.utils;

/**
 * Filter Utilities for Tweets.
 *
 * @author Ralph Offinger
 */
public class TweetTextFilter {

    /**
     * Remove non ascii characters from string.
     *
     * @param text the text
     * @return the string
     */
    public static String removeNonAscii(String text) {
        return text.replaceAll("[^\\x0A\\x0D\\x20-\\x7E]", "");
    }
}
