package edu.hm.ccwi.semantic.tagger.ner;


import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Standford Named Entity
 *
 * @author Ralph Offinger
 */
public class StanfordNER extends NERTagger {

    /**
     * The Classifier.
     */
    CRFClassifier<CoreLabel> classifier;

    /**
     * Instantiates a new Stanford ner.
     */
    public StanfordNER() {
        String serializedClassifier = "nlp/english_14.all.3class.distsim.crf.ser.gz";
        classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
    }

    @Override
    public String identifyNER(String text) {
        List<List<CoreLabel>> classify = classifier.classify(text);
        for (List<CoreLabel> coreLabels : classify) {
            for (CoreLabel coreLabel : coreLabels) {
                String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
                if (!"O".equals(category)) {
                    return category;
                }
            }
        }
        return "";
    }
}

