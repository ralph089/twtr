package edu.hm.ccwi.semantic.tagger.ner;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import edu.hm.ccwi.semantic.tagger.WatsonNLP;

import java.util.List;

/**
 * Watson Named Entity Tagger.
 * <p>
 * https://www.ibm.com/watson/developercloud/doc/natural-language-understanding/entity-types.html
 */
public class WatsonNER extends NERTagger {

    @Override
    public String identifyNER(String word, String tweetText) {
        logger.info("Starting NER for Tweet: " + tweetText);
        List<EntitiesResult> results = getNERResults(tweetText);

        for (EntitiesResult result : getNERResults(tweetText)) {
            if (result.getText().equals(word)) {
                if (result.getType().equals("Organization") ||
                        result.getType().equals("Person") ||
                        result.getType().equals("Location")) {
                    logger.info(String.format("Entity found: %s is a %s.", word, result.getType()));
                    return result.getType();
                }
            }
        }
        logger.info(String.format("Entity unknown: %s", word));
        return "";
    }

    private List<EntitiesResult> getNERResults(String tweetText) {
        return WatsonNLP.getInstance().getResults(tweetText).getEntities();
    }
}
