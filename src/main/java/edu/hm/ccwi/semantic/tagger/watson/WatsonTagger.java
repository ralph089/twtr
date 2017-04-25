package edu.hm.ccwi.semantic.tagger.watson;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SemanticRolesResult;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.tagger.Tagger;
import edu.hm.ccwi.semantic.tagger.Triplet;

import java.util.HashMap;

/**
 * The IBM Watson Tagger.
 * <p>
 * Uses the Natural Language Understanding API for semantic tagging (subject, verb, object).
 *
 * @author Ralph Offinger
 */
public class WatsonTagger extends Tagger {

    /**
     * Instantiates a new Watson tagger.
     *
     * @param limit the limit
     */
    public WatsonTagger(int limit) {
        super(limit);
    }

    @Override
    protected HashMap<String, Triplet> tagSemantics(RelationalEntry entry, AnalysisResults results) {
        HashMap<String, Triplet> tripletList = new HashMap<>();

        for (SemanticRolesResult result : results.getSemanticRoles()) {

            // Only fill semantics, when all semantics have been recognized.
            if (result.getSubject() != null && result.getAction() != null && result.getObject() != null) {

                String subject = result.getSubject().getText();
                String verb = result.getAction().getText();
                String object = result.getObject().getText();

                Triplet triplet = new Triplet<String, String, String>(subject, verb, object);
                tripletList.put(result.getSentence(), triplet);
            }
        }

        return tripletList;
    }
}
