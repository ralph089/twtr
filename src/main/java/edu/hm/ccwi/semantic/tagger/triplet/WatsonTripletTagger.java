package edu.hm.ccwi.semantic.tagger.triplet;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import edu.hm.ccwi.semantic.tagger.WatsonNLP;
import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;

import java.util.ArrayList;
import java.util.List;

/**
 * The IBM Watson Triplet Tagger.
 * <p>
 * Uses the Natural Language Understanding API for extracting subject, verb and object.
 *
 * @author Ralph Offinger
 */
public class WatsonTripletTagger extends TripletTagger {

    private NaturalLanguageUnderstanding service;

    /**
     * Instantiates a new Watson Tagger.
     */
    public WatsonTripletTagger() {
        service = WatsonNLP.getInstance().getService();
    }

    @Override
    public List<Triplet<Subj, Verb, Obj>> tagSemantics(String tweetText) {
        AnalysisResults results = getNLUResults(tweetText);

        ArrayList<Triplet<Subj, Verb, Obj>> semantics = new ArrayList<>();

        for (SemanticRolesResult result : results.getSemanticRoles()) {

            // Only fill semantics, when all semantics have been recognized.
            if (result.getSubject() != null && result.getAction() != null && result.getObject() != null) {

                Subj subject = new Subj(result.getSubject().getText());
                Verb verb = new Verb(result.getAction().getText());
                Obj object = new Obj(result.getObject().getText());

                Triplet<Subj, Verb, Obj> triplet = new Triplet<>(subject, verb, object);
                semantics.add(triplet);
            }
        }
        return semantics;
    }

    /**
     * Gets nlu results.
     *
     * @param tweetText the tweet text
     * @return the nlu results
     */
    protected AnalysisResults getNLUResults(String tweetText) {
        Features features;

        SemanticRolesOptions options = new SemanticRolesOptions.Builder()
                .limit(5)
                .keywords(false)
                .build();

        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .limit(5)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .limit(5)
                .build();

        features = new Features.Builder()
                .semanticRoles(options)
                .entities(entitiesOptions)
                .keywords(keywordsOptions)
                .build();

        AnalyzeOptions parameters =
                new AnalyzeOptions
                        .Builder()
                        .text(tweetText)
                        .features(features)
                        .returnAnalyzedText(true)
                        .build();

        return service.analyze(parameters).execute();
    }
}
