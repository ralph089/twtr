package edu.hm.ccwi.semantic.tagger.ner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts Named Entities.
 *
 * @author Ralph Offinger
 */
public abstract class NERTagger {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(NERTagger.class);

    /**
     * Tags a word as a Location, as a Person or as an Organization.
     *
     * @param word
     * @param tweetText
     * @return Category
     */
    public abstract String identifyNER(String word, String tweetText);
}
