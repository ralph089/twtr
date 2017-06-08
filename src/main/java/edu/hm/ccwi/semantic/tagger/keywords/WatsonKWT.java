package edu.hm.ccwi.semantic.tagger.keywords;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import edu.hm.ccwi.semantic.tagger.WatsonNLP;

import java.util.ArrayList;
import java.util.List;

/**
 * Watson Keyword Extraction.
 * <p>
 * https://www.ibm.com/watson/developercloud/doc/natural-language-understanding/entity-types.html
 */
public class WatsonKWT extends KeywordTagger {

    @Override
    public List<String> identifyKeywords(String tweetText) {
        logger.info("Starting Keyword Extraction for Tweet: " + tweetText);
        List<String> keywords = new ArrayList<>();

        for (KeywordsResult result : getKWTResults(tweetText)) {
            logger.info(String.format("Keyword found: %s", result.getText()));
            keywords.add(result.getText()) ;
        }
        return keywords;
    }

    private List<KeywordsResult> getKWTResults(String tweetText) {
        return WatsonNLP.getInstance().getResults(tweetText).getKeywords();
    }
}
