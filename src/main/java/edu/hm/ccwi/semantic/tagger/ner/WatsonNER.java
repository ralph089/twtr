package edu.hm.ccwi.semantic.tagger.ner;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import edu.hm.ccwi.semantic.tagger.WatsonNLP;

import java.util.List;

/**
 * Watson Named Entity Tagger.
 * <p>
 * More information here:
 * https://www.ibm.com/watson/developercloud/doc/natural-language-understanding/entity-types.html
 */
public class WatsonNER extends NERTagger {

    @Override
    String identifyNE(String word, String tweetText) {

        for (EntitiesResult result : getNERResults(tweetText)) {
            logger.debug(String.format("Watson results: %s ", result.getText()));
            if (result.getText().equals(word)) {
                logger.info(String.format("Entity found: %s is a %s.", word, result.getType()));
                return result.getType();
            }
        }
        logger.info(String.format("Entity unknown: %s", word));
        return "";
    }

    private List<EntitiesResult> getNERResults(String tweetText) {
        return WatsonNLP.getInstance().getResults(tweetText).getEntities();
    }
}
