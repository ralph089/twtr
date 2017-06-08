package edu.hm.ccwi.semantic.tagger;

import edu.hm.ccwi.semantic.commons.utils.Combinations;
import edu.hm.ccwi.semantic.commons.utils.TweetTextFilter;
import edu.hm.ccwi.semantic.parser.RelationalEntry;
import edu.hm.ccwi.semantic.tagger.keywords.KeywordTagger;
import edu.hm.ccwi.semantic.tagger.models.ProperNoun;
import edu.hm.ccwi.semantic.tagger.models.TaggedSentence;
import edu.hm.ccwi.semantic.tagger.ner.NERTagger;
import edu.hm.ccwi.semantic.tagger.triplet.TripletTagger;
import edu.hm.ccwi.semantic.tagger.triplet.models.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;


/**
 * The Stanford Tagger.
 * <p>
 * Uses Stanford NLP for POS-Tagging.
 *
 * @author Ralph Offinger
 */
public class StanfordTagger extends Tagger {

    private static final List<String> PROPERNOUN_TYPE = Arrays.asList("NNP", "NNPS");
    private static final List<String> NOUN_TYPE = Arrays.asList("NN", "NNS");
    private static final List<String> ADJECTIVE_TYPE = Arrays.asList("JJ", "JJR", "JJS");

    private final StanfordCoreNLP pipeline;

    private String tweet;

    /**
     * Instantiates a new Stanford tagger.
     * <p>
     *
     * @param tripletTagger the Triplet-tagger
     * @param nerTagger     the NER-tagger
     * @param keywordTagger the keyword tagger
     */
    public StanfordTagger(TripletTagger tripletTagger, NERTagger nerTagger, KeywordTagger keywordTagger) {
        super(tripletTagger, nerTagger, keywordTagger);
        pipeline = StanfordNLP.getInstance().getPipeline();
    }

    @Override
    protected List<TaggedSentence> tagSentences(RelationalEntry entry) {

        List<TaggedSentence> posTaggedSentences = new ArrayList<>();

        tweet = TweetTextFilter.clearTweet(entry.getTweetText());

        Annotation document = new Annotation(tweet);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<Triplet<Subj, Verb, Obj>> tripletList = tripletTagger.tagTriplet(tweet);

        for (CoreMap sentence : sentences) {

            List<Triplet<Subj, Verb, Obj>> tripletsInSentence = new ArrayList<>();

            for (Triplet<Subj, Verb, Obj> triplet : tripletList) {
                if (triplet.getSentence().equals(sentence.toString())) {
                    tripletsInSentence.add(triplet);
                }
            }

            logger.info(String.format("Analyzing sentence: %s", sentence.toString()));

            // Part of Speech
            HashSet<String> properNounList = new HashSet<>();
            HashSet<String> nounList = new HashSet<>();
            HashSet<String> adjectiveList = new HashSet<>();

            // Finds all nouns, proper nouns and adjective in the sentence.
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);

                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                if (!isHashTagOrMention(word)) {
                    if (PROPERNOUN_TYPE.contains(pos)) {
                        logger.info(String.format("Adding Proper Noun: %s", word));
                        properNounList.add(word);
                    } else if (NOUN_TYPE.contains(pos)) {
                        logger.info(String.format("Adding Common Noun: %s", word));
                        nounList.add(word);
                    } else if (ADJECTIVE_TYPE.contains(pos) && !adjectiveList.contains(word)) {
                        logger.info(String.format("Adding Adjective: %s", word));
                        adjectiveList.add(word);
                    }
                }
            }

            HashSet<String> unrelatedProperNouns = new HashSet<>(properNounList);
            HashSet<String> unrelatedNouns = new HashSet<>(nounList);

            for (Triplet triplet : tripletsInSentence) {

                Subj subject = triplet.getSubject();
                Obj obj = triplet.getObject();

                if (!tagSubjObj(properNounList, unrelatedProperNouns, subject)) {
                    tagSubjObj(nounList, unrelatedNouns, subject);
                }

                if (!tagSubjObj(properNounList, unrelatedProperNouns, obj)) {
                    tagSubjObj(nounList, unrelatedNouns, obj);
                }
            }

            logger.info("Unrelated Proper Noun List: " + unrelatedProperNouns);
            logger.info("Unrelated Common Noun List: " + unrelatedNouns);

            HashSet<ProperNoun> unRelatedPropNounWithEntity = new HashSet<>();
            for (String properNoun : unrelatedProperNouns) {
                unRelatedPropNounWithEntity.add(new ProperNoun(properNoun, tagEntity(properNoun)));
            }

            TaggedSentence posTaggedSentence = new TaggedSentence(sentence.toString(), tripletsInSentence, adjectiveList, unRelatedPropNounWithEntity, unrelatedNouns);
            posTaggedSentences.add(posTaggedSentence);
        }
        return posTaggedSentences;

    }

    private boolean tagSubjObj(HashSet<String> related, HashSet<String> unrelated, SubjObj subjobj) {

        // Add each word from the subject to a Hashset
        boolean found = false;

        // Tweet: Donald Trump is President of America
        // Proper Noun List: America, Donald, Trump, President
        // Subject: Donald Trump
        HashSet<String> objectWords = new HashSet<String>(Arrays.asList(subjobj.getWord().split("\\s+")));

        // Possible Proper Nouns: Donald, Trump
        List<String> possibleProperNouns = new ArrayList<>();

        for (String properNoun : related) {
            if (objectWords.contains(properNoun)) {

                if (!subjobj.isProperNoun()) {
                    possibleProperNouns.add(properNoun);
                }
                if (unrelated.contains(properNoun)) {
                    unrelated.remove(properNoun);
                }
            }
        }

        logger.info("Possible Proper Nouns: " + possibleProperNouns);
        if (possibleProperNouns.size() > 0) {
            subjobj.setProperNoun();
            found = true;

            List<LinkedList<String>> combinations = new Combinations(possibleProperNouns).getCombinations();

            outer:
            for (LinkedList<String> combination : combinations) {

                StringBuilder word = new StringBuilder();

                for (String possibleProperNoun : combination) {
                    word.append(possibleProperNoun);
                    String entity = tagEntity(word.toString());
                    if (entity != null && !Objects.equals(entity, "") && Objects.equals(subjobj.getEntity(), "")) {
                        subjobj.setEntity(entity);
                        break outer;
                    }
                    word.append(" ");
                }
            }
        }
        return found;
    }


    @Override
    protected String tagEntity(String entityName) {
        String category = nerTagger.findNamedEntity(entityName, tweet);
        logger.debug("NER: " + entityName + ": Category: " + category);
        return category;
    }

    private boolean isHashTagOrMention(String word) {
        return word.startsWith("@") || word.startsWith("#");
    }
}


