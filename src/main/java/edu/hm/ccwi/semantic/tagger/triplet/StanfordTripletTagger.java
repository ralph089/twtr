package edu.hm.ccwi.semantic.tagger.triplet;

import edu.hm.ccwi.semantic.tagger.StanfordNLP;
import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Extracts {@link Triplet} with Stanford-NLP.
 *
 * @author Max Auch, Ralph Offinger
 */
public class StanfordTripletTagger extends TripletTagger {

    private final StanfordCoreNLP pipeline;
    private final Logger logger = LoggerFactory.getLogger(StanfordTripletTagger.class);

    /**
     * Instantiates a new Stanford triplet tagger.
     */
    public StanfordTripletTagger() {
        super();
        pipeline = StanfordNLP.getInstance().getPipeline();
    }

    /**
     * Gets pipeline.
     *
     * @return the pipeline
     */
    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }

    @Override
    public List<Triplet<Subj, Verb, Obj>> tagSemantics(String tweetText) {
        Annotation document = new Annotation(tweetText);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<Triplet<Subj, Verb, Obj>> tripletList = new ArrayList<>();

        for (CoreMap sentence : sentences) {
            Tree t = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

            TreebankLanguagePack languagePack = new PennTreebankLanguagePack();
            GrammaticalStructure structure = languagePack.grammaticalStructureFactory()
                    .newGrammaticalStructure(t);
            Collection<TypedDependency> typedDependencies = structure.typedDependenciesCollapsed();
            List<TypedDependency> sPOList = new ArrayList<TypedDependency>();

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
            analyzeSPOList(sPOList, tripletList);
        }
        return tripletList;
    }

    private void analyzeSPOList(List<TypedDependency> sPOList, List<Triplet<Subj, Verb, Obj>> tripletList) {

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
                        for (TypedDependency td3 : sPOList) {
                            if (td3.reln().equals(EnglishGrammaticalRelations.PHRASAL_VERB_PARTICLE)
                                    && td1.gov() == td3.gov()) {
                                tripleFound = true;
                                tripletList.add(new Triplet<Subj, Verb, Obj>(
                                        new Subj(td1.dep().value()),
                                        new Verb(td2.gov().value() + " " + td3.dep().value()),
                                        new Obj(td2.dep().value())));
                            }
                        }
                        if (!tripleFound) {
                            tripletList.add(new Triplet<Subj, Verb, Obj>(
                                    new Subj(td1.dep().value()),
                                    new Verb(td2.gov().value()),
                                    new Obj(td2.dep().value()))
                            );
                        }
                    }
                }
            }
        }
    }
}
