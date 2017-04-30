package edu.hm.ccwi.semantic.tagger;

import edu.hm.ccwi.semantic.commons.utils.TweetTextFilter;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.tagger.models.ProperNoun;
import edu.hm.ccwi.semantic.tagger.models.TaggedSentence;
import edu.hm.ccwi.semantic.tagger.ner.NERTagger;
import edu.hm.ccwi.semantic.tagger.triplet.TripletTagger;
import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


/**
 * The Stanford Tagger.
 *
 * Uses Stanford NLP for POS-Tagging.
 *
 * @author Ralph Offinger
 */
public class StanfordTagger extends Tagger {

    private static final List<String> PROPERNOUN_TYPE = Arrays.asList("NNP", "NNPS");
    private static final List<String> NOUN_TYPE = Arrays.asList("NN", "NNS");
    private static final List<String> ADJECTIVE_TYPE = Arrays.asList("JJ", "JJR", "JJS");

    private final StanfordCoreNLP pipeline;

    /**
     * Instantiates a new Stanford tagger.
     * <p>
     *
     * @param tripletTagger the Triplet-tagger
     * @param nerTagger     the NER-tagger
     */
    public StanfordTagger(TripletTagger tripletTagger, NERTagger nerTagger) {
        super(tripletTagger, nerTagger);
        pipeline = StanfordNLP.getInstance().getPipeline();
    }

    @Override
    protected List<TaggedSentence> tagSentences(RelationalEntry entry) {

        List<TaggedSentence> posTaggedSentences = new ArrayList<>();
        String text = TweetTextFilter.clearTweet(entry.getTweetText());
        logger.info(String.format("Filtered Tweet to: %s", text));

        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {

            logger.debug(String.format("Analyzing sentence: ", sentence.toString()));

            // Part of Speech
            HashSet<String> properNounList = new HashSet<>();
            HashSet<String> nounList = new HashSet<>();
            HashSet<String> adjectiveList = new HashSet<>();

            List<Triplet<Subj, Verb, Obj>> tripletList = tripletTagger.tagTriplet(sentence.toString());

            // Finds all nouns, proper nouns and adjective in the sentence.
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tagSentences of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                if (!isHashTagOrMention(word)) {
                    if (PROPERNOUN_TYPE.contains(pos)) {
                        logger.debug(String.format("Adding Proper Noun: %s", word));
                        properNounList.add(word);
                    } else if (NOUN_TYPE.contains(pos)) {
                        logger.debug(String.format("Adding Common Noun: %s", word));
                        nounList.add(word);
                    } else if (ADJECTIVE_TYPE.contains(pos) && !adjectiveList.contains(word)) {
                        logger.debug(String.format("Adding Adjective: %s", word));
                        adjectiveList.add(word);
                    }
                }
            }

            HashSet<String> unrelatedProperNouns = new HashSet<>(properNounList);
            HashSet<String> unrelatedNouns = new HashSet<>(nounList);

            for (Triplet triplet : tripletList) {
                if (properNounList.contains(triplet.getSubject().getWord())) {
                    triplet.getSubject().setProperNoun(true, triplet.getSubject().getWord());
                    if (unrelatedProperNouns.contains(triplet.getSubject().getWord())) {
                        unrelatedProperNouns.remove(triplet.getSubject().getWord());
                    }
                } else if (nounList.contains(triplet.getSubject().getWord())) {
                    triplet.getSubject().setNoun(true);
                    if (unrelatedNouns.contains(triplet.getSubject().getWord())) {
                        unrelatedNouns.remove(triplet.getSubject().getWord());
                    }
                }

                if (properNounList.contains(triplet.getObject().getWord())) {
                    triplet.getObject().setProperNoun(true, triplet.getObject().getWord());
                    if (unrelatedProperNouns.contains(triplet.getObject().getWord())) {
                        unrelatedProperNouns.remove(triplet.getObject().getWord());
                    }
                } else if (nounList.contains(triplet.getObject().getWord())) {
                    triplet.getObject().setNoun(true);
                    if (unrelatedNouns.contains(triplet.getObject().getWord())) {
                        unrelatedNouns.remove(triplet.getObject().getWord());
                    }
                }
            }

            logger.debug("Unrelated Proper Noun List: " + unrelatedProperNouns);
            logger.debug("Unrelated Common Noun List: " + unrelatedNouns);

            HashSet<ProperNoun> unRelatedPropNounWithEntity = new HashSet<>();
            for (String properNoun : unrelatedProperNouns) {
                unRelatedPropNounWithEntity.add(new ProperNoun(properNoun, tagEntity(properNoun)));
            }

            TaggedSentence posTaggedSentence = new TaggedSentence(sentence.toString(), tripletList, adjectiveList, unRelatedPropNounWithEntity, unrelatedNouns);
            posTaggedSentences.add(posTaggedSentence);
        }
        return posTaggedSentences;

    }

    @Override
    protected String tagEntity(String entityName) {
        String category = nerTagger.identifyNER(entityName);
        logger.debug("NER: " + entityName + ": Category: " + category);
        return category;
    }

    private boolean isHashTagOrMention(String word) {
        if (word.startsWith("@") || word.startsWith("#")) {
            return true;
        } else {
            return false;
        }
    }
}


