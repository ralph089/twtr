package edu.hm.ccwi.semantic.tagger.ner;

import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts {@link Triplet} from Tweets.
 *
 * @author Ralph Offinger
 */
public abstract class NERTagger {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(NERTagger.class);

    /**
     * identify Name,organization location etc entities and return Category
     *
     * @param text -- data
     * @return Category string
     */
    public abstract String identifyNER(String text);
}
