package edu.hm.ccwi.semantic.tagger.triplet;

import edu.hm.ccwi.semantic.tagger.StanfordNLP;
import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Extracts {@link Triplet} with OpenIE.
 *
 * @author Ralph Offinger
 */
public class OpenIETripletTagger extends TripletTagger {

    private final StanfordCoreNLP pipeline;

    /**
     * Instantiates a new Stanford triplet tagger.
     */
    public OpenIETripletTagger() {
        super();
        pipeline = StanfordNLP.getInstance().getPipeline();
    }

    @Override
    public List<Triplet<Subj, Verb, Obj>> tagTriplet(String tweetSentence) {
        Annotation document = new Annotation(tweetSentence);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<Triplet<Subj, Verb, Obj>> tripletList = new ArrayList<>();

        for (CoreMap sentence : sentences) {
            Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            for (RelationTriple triple : triples) {

                logger.info(String.format("Found Triplet (using lemma): Subject: %s, Verb: %s, Object: %s",
                        triple.subjectLemmaGloss(),
                        triple.relationLemmaGloss(),
                        triple.objectLemmaGloss()));

                Triplet triplet = new Triplet<Subj, Verb, Obj>(
                        new Subj(triple.subjectLemmaGloss()),
                        new Verb(triple.relationLemmaGloss()),
                        new Obj(triple.objectLemmaGloss()));

                if (!tripletList.contains(triplet)) {
                    tripletList.add(triplet);
                }
            }
        }
        return tripletList;
    }
}
