package edu.hm.ccwi.semantic.tagger.keywords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Extracts Named Entities.
 *
 * @author Ralph Offinger
 */
public abstract class KeywordTagger {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(KeywordTagger.class);

    /**
     * Tags a word as a Location, as a Person or as an Organization.
     *
     * @param tweetText Tweet Text
     * @return Category
     */
    public abstract List<String> identifyKeywords(String tweetText);
}
