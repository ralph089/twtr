package edu.hm.ccwi.semantic.tagger;

import edu.hm.ccwi.semantic.commons.twitter.Tweet;
import edu.hm.ccwi.semantic.commons.twitter.TwitterUser;
import edu.hm.ccwi.semantic.commons.utils.TweetTextFilter;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.tagger.keywords.KeywordTagger;
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
    /**
     * The Keyword tagger.
     */
    KeywordTagger keywordTagger;

    // TODO: Mit Watson wird lediglich der Tweet Text analysiert.
    // TODO: Kurze Namen können nicht analysiert werden, da Mindestlänge. Ideen?
    StanfordNER userNerTagger = new StanfordNER();

    /**
     * Instantiates a new Triplet tagger.
     *
     * @param tripletTagger the triplet tagger
     * @param nerTagger     the ner tagger
     */
    Tagger(TripletTagger tripletTagger, NERTagger nerTagger, KeywordTagger keywordTagger) {
        this.tripletTagger = tripletTagger;
        this.nerTagger = nerTagger;
        this.keywordTagger = keywordTagger;
    }

    /**
     * Tags Tweets based on a {@link RelationalEntry}.
     *
     * @param entry the twitter {@link RelationalEntry}
     * @return the POS-Tagged {@link TaggedTweet}
     */
    public TaggedTweet tagTweet(RelationalEntry entry) {

        TwitterUser twitterUser = new TwitterUser(
                entry.getFollower_count(),
                entry.getUserId(),
                entry.getUsername(),
                entry.getUserDescription(),
                userNerTagger.findNamedEntity(entry.getUsername(), ""));

        Tweet tweet = new Tweet(entry.getTweet_id(), twitterUser, entry.getTweetText());

        return new TaggedTweet(tweet, tagSentences(entry), tagKeywords(TweetTextFilter.clearTweet(entry.getTweetText())));
    }

    /**
     * Tags the sentences of a Tweet.
     *
     * @param entry the entry
     * @return the list
     */
    protected abstract List<TaggedSentence> tagSentences(RelationalEntry entry);

    /**
     * Identifies the entity to a given name.
     *
     * @param entityName the name of the entity
     * @return the entity
     */
    protected abstract String tagEntity(String entityName);

    /**
     * Identifies the keywords.
     *
     * @param tweetText the text of the tweet
     * @return the keyword
     */
    protected List<String> tagKeywords(String tweetText) {
        return keywordTagger.identifyKeywords(tweetText);
    }
}
