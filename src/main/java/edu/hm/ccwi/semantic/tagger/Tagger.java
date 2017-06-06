package edu.hm.ccwi.semantic.tagger;

import edu.hm.ccwi.semantic.commons.twitter.Tweet;
import edu.hm.ccwi.semantic.commons.twitter.TwitterUser;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.tagger.models.TaggedSentence;
import edu.hm.ccwi.semantic.tagger.models.TaggedTweet;
import edu.hm.ccwi.semantic.tagger.ner.NERTagger;
import edu.hm.ccwi.semantic.tagger.ner.StanfordNER;
import edu.hm.ccwi.semantic.tagger.triplet.TripletTagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * The abstract base class of a Tweet Tagger.
 *
 * @author Ralph Offinger
 */
public abstract class Tagger {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(Tagger.class);
    /**
     * The Triplet tagger.
     */
    TripletTagger tripletTagger;
    /**
     * The Ner tagger.
     */
    NERTagger nerTagger;

    StanfordNER userNerTagger = new StanfordNER();

    /**
     * Instantiates a new Triplet tagger.
     *
     * @param tripletTagger the triplet tagger
     * @param nerTagger     the ner tagger
     */
    Tagger(TripletTagger tripletTagger, NERTagger nerTagger) {
        this.tripletTagger = tripletTagger;
        this.nerTagger = nerTagger;
    }

    /**
     * Tags Tweets based on a {@link RelationalEntry}.
     *
     * @param entry the twitter {@link RelationalEntry}
     * @return the POS-Tagged {@link TaggedTweet}
     */
    public TaggedTweet tagTweet(RelationalEntry entry) {

        TwitterUser twitterUser = new TwitterUser(entry.getFollower_count(), entry.getUserId(), entry.getUsername(), entry.getUserDescription(), userNerTagger.identifyNER(entry.getUsername()));
        Tweet tweet = new Tweet(entry.getTweet_id(), twitterUser, entry.getTweetText());

        List<TaggedSentence> taggedSentences = tagSentences(entry);

        return new TaggedTweet(tweet, taggedSentences);
    }

    /**
     * Tag twitter text list.
     *
     * @param entry the entry
     * @return the list
     */
    protected abstract List<TaggedSentence> tagSentences(RelationalEntry entry);

    /**
     * Tags the entity.
     *
     * @param entityName the name of the entity
     * @return the entity
     */
    protected abstract String tagEntity(String entityName);

}
