package edu.hm.ccwi.semantic.tagger.triplet;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import edu.hm.ccwi.semantic.tagger.WatsonNLP;
import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;

import java.util.ArrayList;
import java.util.List;

/**
 * Extracts {@link Triplet} with IBM Watson NLU.
 * <p>
 * Uses the Natural Language Understanding (NLU) API for extracting subject, verb and object.
 *
 * @author Ralph Offinger
 */
public class WatsonTripletTagger extends TripletTagger {

    @Override
    public List<Triplet<Subj, Verb, Obj>> tagTriplet(String tweetText) {

        ArrayList<Triplet<Subj, Verb, Obj>> semantics = new ArrayList<>();

        for (SemanticRolesResult result : getTripletResults(tweetText)) {

            // Only fill triplet, when all semantics have been recognized.
            if (result.getSubject() != null && result.getAction().getVerb() != null && result.getObject() != null) {

                Subj subject = new Subj(result.getSubject().getText());
                Verb verb = new Verb(result.getAction().getVerb().getText());
                Obj object = new Obj(result.getObject().getText());

                logger.info(String.format("Found Triplet: Subject: %s, Verb: %s, Object: %s",
                        subject,
                        verb,
                        object));

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
    private List<SemanticRolesResult> getTripletResults(String tweetText) {
        return WatsonNLP.getInstance().getResults(tweetText).getSemanticRoles();
    }
}
