package edu.hm.ccwi.semantic.tagger;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * The type Stanford nlp.
 */
public class StanfordNLP {

    private static final String ANNOTATOR = "annotators";
    private static final String PROPERTIES = "tokenize, ssplit, pos, lemma, ner, parse, dcoref";
    private static StanfordNLP instance;
    private final StanfordCoreNLP pipeline;

    private StanfordNLP() {
        Properties properties = new Properties();
        properties.put(ANNOTATOR, PROPERTIES);
        properties.put("quiet", true);
        properties.put("pos.model", "nlp/gate-EN-twitter.model");

        pipeline = new StanfordCoreNLP(properties);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static StanfordNLP getInstance() {
        if (StanfordNLP.instance == null) {
            StanfordNLP.instance = new StanfordNLP();
        }
        return StanfordNLP.instance;
    }

    /**
     * Gets pipeline.
     *
     * @return the pipeline
     */
    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }
}
