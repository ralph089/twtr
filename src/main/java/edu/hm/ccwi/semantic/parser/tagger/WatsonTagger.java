package edu.hm.ccwi.semantic.parser.tagger;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import edu.hm.ccwi.semantic.parser.relational.RelationalEntry;
import edu.hm.ccwi.semantic.parser.tagger.model.TaggedTweet;
import edu.hm.ccwi.semantic.parser.tagger.model.Triplet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * The IBM Watson Natural Language Understanding Tagger.
 *
 * Uses the Natural Language Understanding API.
 * Processes text for advanced analysis, like Part-Of-Speech Tagging (POS Tagging).
 */
public class WatsonTagger implements Tagger {


    private TaggedTweet taggedTweet;
    private List<Triplet<String, String, String>> tripletList;

    public TaggedTweet tagTweet(RelationalEntry entry) {
        if (entry.getTweetText() != null) {
            if (entry.getTweetText() != "" && !entry.getTweetText().isEmpty()) {

                Properties prop = getProperties();

                NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                        NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                        prop.getProperty("username"),
                        prop.getProperty("password")
                );

                HashMap<String, Triplet> analyzedTweet = new HashMap<>();

                SemanticRolesOptions options = new SemanticRolesOptions.Builder()
                        .limit(5)
                        .keywords(true)
                        .build();

                Features features = new Features.Builder().semanticRoles(options).build();
                AnalyzeOptions parameters =
                        new AnalyzeOptions
                                .Builder()
                                .text(entry.getTweetText())
                                .features(features)
                                .returnAnalyzedText(true)
                                .build();

                AnalysisResults results = service.analyze(parameters).execute();

                for (SemanticRolesResult result : results.getSemanticRoles()) {
                    if (result.getSubject() != null && result.getAction() != null && result.getObject() != null) {
                        String subject = result.getSubject().getText();
                        String verb = result.getAction().getText();
                        String object = result.getObject().getText();

                        Triplet triplet = new Triplet<String, String, String>(subject, verb, object);
                        analyzedTweet.put(result.getSentence(), triplet);
                    }
                }
                taggedTweet = new TaggedTweet(entry.getTweet_id(), analyzedTweet, this.getClass().getSimpleName());
            }
        }
        return taggedTweet;
    }

    private Properties getProperties() {
        Properties prop = new Properties();

        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("credentials/bluemix.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
