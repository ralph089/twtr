package edu.hm.ccwi.semantic.tagger.triplet;

import edu.hm.ccwi.semantic.commons.utils.TweetTextFilter;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Extracts {@link Triplet} from Tweets.
 *
 * @author Ralph Offinger
 */
public abstract class TripletTagger {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(TripletTagger.class);

    private static String validateText(String text) {
        Validate.notEmpty(text);

        text = TweetTextFilter.removeNonAscii(text);
        text = text.replaceAll("[#]", "");
        text = text.replaceAll("[@]", "");

        text = text.replaceAll("https?://\\S+\\s?", "");

        if (text.charAt(text.length() - 1) != '.') {
            text = (text + ".");
        }
        return text;
    }

    /**
     * Tag semantics list.
     *
     * @param entry the entry
     * @return the list
     */
    public List<Triplet<Subj, Verb, Obj>> tagSemantics(RelationalEntry entry) {

        logger.debug(String.format("Cleaning Text: %s", entry.getTweetText()));
        String text = validateText(entry.getTweetText());
        logger.debug(String.format("Tagging Triplet of cleaned text: %s", text));

        return tagSemantics(text);
    }

    /**
     * Tag semantics list.
     *
     * @param tweetText the tweet text
     * @return the list
     */
    public abstract List<Triplet<Subj, Verb, Obj>> tagSemantics(String tweetText);
}
