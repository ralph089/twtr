package edu.hm.ccwi.semantic.tagger.triplet;

import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;
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


    /**
     * Tag semantics list.
     *
     * @param tweetText the tweet text
     * @return the list
     */
    public abstract List<Triplet<Subj, Verb, Obj>> tagTriplet(String tweetText);
}
