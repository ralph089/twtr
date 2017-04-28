package edu.hm.ccwi.semantic.tagger;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;

import java.io.IOException;
import java.util.Properties;

/**
 * The type Watson nlp.
 */
public class WatsonNLP {

    private static WatsonNLP instance;
    private NaturalLanguageUnderstanding service;

    private WatsonNLP() {
        Properties prop = new Properties();

        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("cred/bluemix.properties"));
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
     * Gets instance.
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
     * Gets service.
     *
     * @return the service
     */
    public NaturalLanguageUnderstanding getService() {
        return service;
    }
}
