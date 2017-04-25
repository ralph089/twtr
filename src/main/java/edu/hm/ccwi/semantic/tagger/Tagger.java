package edu.hm.ccwi.semantic.tagger;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import edu.hm.ccwi.semantic.commons.twitter.Entity;
import edu.hm.ccwi.semantic.commons.twitter.TaggedTweet;
import edu.hm.ccwi.semantic.commons.twitter.TwitterUser;
import edu.hm.ccwi.semantic.parser.RelationalEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


/**
 * The abstract base class of a Tweet Tagger.
 * <p>
 * Uses the IBM Natural Language Understanding API for Named Entity Recognition.
 *
 * @author Ralph Offinger
 */
public abstract class Tagger {

    private NaturalLanguageUnderstanding service = null;
    private Features features = null;

    /**
     * Instantiates a new Tagger.
     *
     * @param limit the limit
     */
    public Tagger(int limit) {
        Properties prop = new Properties();

        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("cred/bluemix.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                prop.getProperty("username"),
                prop.getProperty("password")
        );


        SemanticRolesOptions options = new SemanticRolesOptions.Builder()
                .limit(limit)
                .keywords(false)
                .build();

        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .limit(2)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .limit(2)
                .build();

        this.features = new Features.Builder()
                .semanticRoles(options)
                .entities(entitiesOptions)
                .keywords(keywordsOptions)
                .build();
    }

    /**
     * Tags Tweets based on a {@link RelationalEntry}.
     *
     * @param entry the twitter {@link RelationalEntry}
     * @return the POS-Tagged {@link TaggedTweet}
     */
    public TaggedTweet tagTweet(RelationalEntry entry) {

        TwitterUser twitterUser = new TwitterUser(entry.getFollower_count(), entry.getUserId(), entry.getUsername(), entry.getUserDescription());
        HashMap<String, Triplet> tripletMap = new HashMap<>();

        AnalysisResults results = getResults(entry);

        try {
            return new TaggedTweet(
                    entry.getTweet_id(),
                    twitterUser,
                    entry.getTweetText(),
                    tagSemantics(entry, results),
                    this.getClass().getSimpleName(),
                    getProperNoun(results),
                    getCommonNouns(results));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Tag semantics hash map.
     *
     * @param entry   the entry
     * @param results the results
     * @return the hash map
     */
    protected abstract HashMap<String, Triplet> tagSemantics(RelationalEntry entry, AnalysisResults results);

    private AnalysisResults getResults(RelationalEntry entry) {
        AnalyzeOptions parameters =
                new AnalyzeOptions
                        .Builder()
                        .text(entry.getTweetText())
                        .features(features)
                        .returnAnalyzedText(true)
                        .build();

        return service.analyze(parameters).execute();
    }

    private List<String> getCommonNouns(AnalysisResults results) {
        List<String> commonNounList = new ArrayList<>();
        if (results.getKeywords() != null) {
            for (KeywordsResult keyword : results.getKeywords()) {
                commonNounList.add(keyword.getText());
            }
        }
        return commonNounList;
    }

    private Entity getProperNoun(AnalysisResults results) {
        EntitiesResult most_relevant_entity = null;
        if (results.getEntities() != null) {
            for (EntitiesResult entity : results.getEntities()) {
                if (most_relevant_entity == null) {
                    most_relevant_entity = entity;
                } else {
                    if (entity.getRelevance() > most_relevant_entity.getRelevance()) {
                        most_relevant_entity = entity;
                    }
                }
            }
            if (most_relevant_entity != null) {
                return new Entity(most_relevant_entity.getText(), most_relevant_entity.getType());
            }
        }
        return null;
    }
}
