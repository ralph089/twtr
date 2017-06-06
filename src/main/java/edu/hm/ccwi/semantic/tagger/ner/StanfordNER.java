package edu.hm.ccwi.semantic.tagger.ner;


import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

/**
 * Standford Named Entity Tagger
 *
 * Tags an Entity as Location, as Person or as Organization
 *
 * @author Ralph Offinger
 */
public class StanfordNER extends NERTagger {

    /**
     * The Classifier.
     */
    CRFClassifier<CoreLabel> classifier;

    /**
     * Instantiates a new Stanford Named Entity Tagger.
     */
    public StanfordNER() {
        String serializedClassifier = "src/main/resources/nlp/english_16.all.3class.distsim.crf.ser.gz";
        classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
    }

    @Override
    public String identifyNER(String word, String tweetText) {
        logger.info("Starting NER for Tweet: " + tweetText);
        List<List<CoreLabel>> classify = classifier.classify(word);
        for (List<CoreLabel> coreLabels : classify) {
            for (CoreLabel coreLabel : coreLabels) {
                String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
                if (!"O".equals(category)) {
                    logger.info(String.format("Entity found: %s is a %s.", word, category));
                    return category;
                }
            }
        }
        logger.info(String.format("Entity unknown: %s", word));
        return "";
    }

    public String identifyNER(String word) {
        return identifyNER(word, "");
    }
}

