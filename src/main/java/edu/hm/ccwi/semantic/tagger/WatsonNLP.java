package edu.hm.ccwi.semantic.tagger;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Watson NLP Service.
 *
 * @author Ralph Offinger
 */
public class WatsonNLP {

    private static WatsonNLP instance;
    private NaturalLanguageUnderstanding service;
    private Map<String, AnalysisResults> tweetList = new HashMap<>();

    private WatsonNLP() {
        Properties prop = new Properties();

        try {
            InputStream input = new FileInputStream("src/main/resources/cred/bluemix.properties");
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                prop.getProperty("username"),
                prop.getProperty("password")
        );
    }

    /**
     * Gets the singleton instance.
     *
     * @return the instance
     */
    public static WatsonNLP getInstance() {
        if (WatsonNLP.instance == null) {
            WatsonNLP.instance = new WatsonNLP();
        }
        return WatsonNLP.instance;
    }

    /**
     * Gets the analysis result by querying the Watson NLU Service.
     * <p>
     * If there are multiple tweets with the same text (e. g. Retweets), the tweet won't get analyzed again to reduce Watson NLU queries.
     *
     * @param tweetText the tweet text
     * @return the results
     */
    public AnalysisResults getResults(String tweetText) {
        if (tweetList.containsKey(tweetText)) {
            return tweetList.get(tweetText);
        } else {
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
                            .language("en")
                            .build();

            AnalysisResults result = service.analyze(parameters).execute();
            tweetList.put(tweetText, result);
            return result;
        }
    }
}
