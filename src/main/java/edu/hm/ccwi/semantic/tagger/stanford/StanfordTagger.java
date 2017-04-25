package edu.hm.ccwi.semantic.tagger.stanford;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import edu.hm.ccwi.semantic.commons.twitter.TwitterUser;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.tagger.Tagger;
import edu.hm.ccwi.semantic.tagger.Triplet;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;


/**
 * The Stanford Tagger.
 * <p>
 * Uses the Stanford Core API for semantic tagging (subject, verb, object).
 *
 * @author Max Auch, Ralph Offinger
 */
public class StanfordTagger extends Tagger {

    private Properties properties;
    private StanfordCoreNLP pipeline;
    private Annotation document;

    /**
     * Instantiates a new Stanford tagger.
     *
     * @param limit the limit
     */
    public StanfordTagger(int limit) {
        super(limit);

        properties = new Properties();
        properties.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        properties.put("quiet", true);
        properties.put("pos.model", "nlp/gate-EN-twitter.model");
        pipeline = new StanfordCoreNLP(properties);
    }

    private static void validateText(RelationalEntry entry) {
        if (entry.getTweetText().charAt(entry.getTweetText().length() - 1) != '.') {
            entry.setTweetText(entry.getTweetText() + ".");
        }
    }

    @Override
    protected HashMap<String, Triplet> tagSemantics(RelationalEntry entry, AnalysisResults results) {

        HashMap<String, Triplet> analyzedTweet = null;
        if (entry.getTweetText() != null) {
            if (entry.getTweetText() != "" && !entry.getTweetText().isEmpty()) {

                validateText(entry);

                this.document = new Annotation(entry.getTweetText());
                this.pipeline.annotate(document);

                List<CoreMap> sentences = this.document.get(SentencesAnnotation.class);

                analyzedTweet = new HashMap<>();

                TwitterUser twitterUser = new TwitterUser(entry.getFollower_count(), entry.getUserId(), entry.getUsername(), entry.getUserDescription());

                for (CoreMap sentence : sentences) {
                    Tree t = sentence.get(TreeAnnotation.class);

                    TreebankLanguagePack languagePack = new PennTreebankLanguagePack();
                    GrammaticalStructure structure = languagePack.grammaticalStructureFactory()
                            .newGrammaticalStructure(t);
                    Collection<TypedDependency> typedDependencies = structure.typedDependenciesCollapsed();
                    List<TypedDependency> sPOList = new ArrayList<>();

                    for (TypedDependency td : typedDependencies) {
                        if (td.reln().equals(EnglishGrammaticalRelations.SUBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.NOMINAL_SUBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.CLAUSAL_SUBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.CONTROLLING_SUBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.CLAUSAL_PASSIVE_SUBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.NOMINAL_PASSIVE_SUBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.OBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.DIRECT_OBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.INDIRECT_OBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT)
                                || td.reln().equals(EnglishGrammaticalRelations.PHRASAL_VERB_PARTICLE)) {
                            sPOList.add(td);
                        }
                    }

                    for (TypedDependency td1 : sPOList) {
                        Boolean tripleFound = false;
                        if (td1.reln().equals(EnglishGrammaticalRelations.SUBJECT)
                                || td1.reln().equals(EnglishGrammaticalRelations.NOMINAL_SUBJECT)
                                || td1.reln().equals(EnglishGrammaticalRelations.CLAUSAL_SUBJECT)
                                || td1.reln().equals(EnglishGrammaticalRelations.CONTROLLING_SUBJECT)
                                || td1.reln().equals(EnglishGrammaticalRelations.CLAUSAL_PASSIVE_SUBJECT)
                                || td1.reln().equals(EnglishGrammaticalRelations.NOMINAL_PASSIVE_SUBJECT)) {
                            for (TypedDependency td2 : sPOList) {
                                if ((td2.reln().equals(EnglishGrammaticalRelations.OBJECT)
                                        || td2.reln().equals(EnglishGrammaticalRelations.DIRECT_OBJECT)
                                        || td2.reln().equals(EnglishGrammaticalRelations.INDIRECT_OBJECT)
                                        || td2.reln().equals(EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT))
                                        && td1.gov() == td2.gov()) {
                                    Triplet triplet = null;
                                    for (TypedDependency td3 : sPOList) {
                                        if (td3.reln().equals(EnglishGrammaticalRelations.PHRASAL_VERB_PARTICLE)
                                                && td1.gov() == td3.gov()) {
                                            tripleFound = true;
                                            triplet = new Triplet<>(td1.dep().value(),
                                                    td2.gov().value() + " " + td3.dep().value(), td2.dep().value());
                                        }
                                    }
                                    if (!tripleFound) {
                                        triplet = new Triplet<>(td1.dep().value(), td2.gov().value(),
                                                td2.dep().value());
                                    }
                                    analyzedTweet.put(sentence.toString(), triplet);
                                }
                            }
                        }
                    }
                }
            }
        }
        return analyzedTweet;
    }
}


