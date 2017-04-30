package edu.hm.ccwi.semantic.tagger;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * The Stanford NLP Service.
 *
 * @author Ralph Offinger
 */
public class StanfordNLP {

    private static final String ANNOTATOR = "annotators";
    private static final String PROPERTIES = "tokenize, ssplit, pos, lemma, ner, parse, dcoref, natlog, openie";
    private static StanfordNLP instance;
    private final StanfordCoreNLP pipeline;

    private StanfordNLP() {
        Properties properties = new Properties();
        properties.put(ANNOTATOR, PROPERTIES);

        properties.put("tokenize.language", "en");

        //properties.put("openie.triple.strict", "true");

        properties.put("quiet", true);
        properties.put("threads", "8");

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
