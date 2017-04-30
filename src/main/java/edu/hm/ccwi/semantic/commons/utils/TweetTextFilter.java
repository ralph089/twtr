package edu.hm.ccwi.semantic.commons.utils;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter Utilities for Tweets.
 *
 * @author Ralph Offinger
 */
public class TweetTextFilter {

    /**
     * The Logger.
     */
    static final Logger logger = LoggerFactory.getLogger(TweetTextFilter.class);

    /**
     * Remove non ascii characters from string.
     *
     * @param text the text
     * @return the string
     */
    public static String removeNonAscii(String text) {
        return text.replaceAll("[^\\x0A\\x0D\\x20-\\x7E]", "");
    }

    /**
     * Remove hash tag symbols string.
     *
     * @param text the text
     * @return the string
     */
    public static String removeHashTagSymbols(String text) {
        text = text.replaceAll("[#]", "");
        return text;
    }

    /**
     * Remove mentions symbol string.
     *
     * @param text the text
     * @return the string
     */
    public static String removeMentionsSymbol(String text) {
        text = text.replaceAll("[@]", "");
        return text;
    }

    /**
     * Remove ur ls string.
     *
     * @param text the text
     * @return the string
     */
    public static String removeURLs(String text) {
        text = text.replaceAll("https?://\\S+\\s?", "");
        return text;
    }

    /**
     * Ensure sentence ending string.
     *
     * @param text the text
     * @return the string
     */
    public static String ensureSentenceEnding(String text) {
        if (text.length() > 0) {
            if (text.charAt(text.length() - 1) != '.' ||
                    text.charAt(text.length() - 1) != '?' ||
                    text.charAt(text.length() - 1) != '!') {
                text = (text + ".");
            } else if (text.charAt(text.length() - 1) == ':') {
                text = text.substring(0, text.length() - 1);
                text = (text + ".");
            }
        }
        return text;
    }

    /**
     * Remove re tweet string.
     *
     * @param text the text
     * @return the string
     */
    public static String removeReTweet(String text) {
        text = text.replaceAll("(RT|retweet|retweeted|from|via) .\\S+:", "");
        return text;
    }

    /**
     * Clear tweet string.
     *
     * @param text the text
     * @return the string
     */
    public static String clearTweet(String text) {
        text = Validate.notEmpty(text);
        text = removeNonAscii(text);
        text = removeReTweet(text);
        text = removeHashTagSymbols(text);
        text = removeMentionsSymbol(text);
        text = removeURLs(text);
        text = text.trim();
        text = ensureSentenceEnding(text);

        return text;
    }

}
