package edu.hm.ccwi.semantic.tagger.ner;

import org.apache.commons.lang3.text.WordUtils;
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
     * Finds a Named Entity to a word such as a Location, a Person or an Organization.
     *
     * @param word Word to tag
     * @param tweetText Tweet Text
     * @return Category
     */
    public String findNamedEntity(String word, String tweetText) {
        logger.info("NER: Try to identify: " + word);

        String entity = identifyNE(word, tweetText);
        return WordUtils.capitalizeFully(entity);
    };

    abstract String identifyNE(String word, String tweetText);
}
