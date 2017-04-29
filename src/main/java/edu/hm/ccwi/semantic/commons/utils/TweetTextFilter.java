package edu.hm.ccwi.semantic.commons.utils;

import edu.hm.ccwi.semantic.tagger.Tagger;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter Utilities for Tweets.
 *
 * @author Ralph Offinger
 */
public class TweetTextFilter {

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

    public static String removeHashTagSymbols(String text){
        text = text.replaceAll("[#]", "");
        return text;
    }

    public static String removeMentionsSymbol(String text){
        text = text.replaceAll("[@]", "");
        return text;
    }

    public static String removeURLs(String text){
        text = text.replaceAll("https?://\\S+\\s?", "");
        return text;
    }

    public static String ensureSentenceWithDot(String text){
        if(text.length() > 0) {
            if (text.charAt(text.length() - 1) != '.') {
                text = (text + ".");
            }
        } else {
            logger.warn(String.format("Text hat Länge 0: %s", text));
        }
        return text;
    }

    public static String removeReTweet(String text){
        text = text.replaceAll("(RT|retweet|from|via) .\\S+:", "");
        return text;
    }

    public static String clearTweet(String text) {
        text = Validate.notEmpty(text);
        text = removeNonAscii(text);
        text = removeReTweet(text);
        text = removeHashTagSymbols(text);
        text = removeMentionsSymbol(text);
        text = removeURLs(text);
        text = text.trim();
        text = ensureSentenceWithDot(text);

        return text;
    }

}
