package edu.hm.ccwi.semantic.parser.tagger;

import edu.hm.ccwi.semantic.parser.relational.RelationalEntry;
import edu.hm.ccwi.semantic.parser.tagger.model.TaggedTweet;


/**
 * The interface of a Tagger.
 */
public interface Tagger {

    /**
     * Tags SPO-Triples of a twitter {@link RelationalEntry}.
     *
     * @param entry the twitter {@link RelationalEntry}
     * @return the POS-Tagged {@link TaggedTweet}
     */
    TaggedTweet tagTweet(RelationalEntry entry);
}
