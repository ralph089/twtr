package edu.hm.ccwi.semantic.tagger;

import edu.hm.ccwi.semantic.parser.RelationalEntry;
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
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * The Stanford POS Tagger.
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
     * Reuse StanfordCoreNLP Pipeline from SemanticTagger.
     *
     * @param tripletTagger the triplet tagger
     * @param nerTagger     the ner tagger
     */
    public StanfordTagger(TripletTagger tripletTagger, NERTagger nerTagger) {
        super(tripletTagger, nerTagger);
        pipeline = StanfordNLP.getInstance().getPipeline();
    }

    @Override
    protected List<TaggedSentence> tagTwitterText(RelationalEntry entry) {

        List<TaggedSentence> posTaggedSentences = new ArrayList<TaggedSentence>();

        Annotation document = new Annotation(entry.getTweetText());

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {

            logger.debug(String.format("Analyzing sentence: ", sentence.toString()));

            Tree t = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            TreebankLanguagePack languagePack = new PennTreebankLanguagePack();
            GrammaticalStructure structure = languagePack.grammaticalStructureFactory().newGrammaticalStructure(t);
            Collection<TypedDependency> typedDependencies = structure.typedDependenciesCollapsed();

            // Part of Speech
            ArrayList<String> properNounList = new ArrayList<String>();
            ArrayList<String> nounList = new ArrayList<String>();
            ArrayList<String> adjectiveList = new ArrayList<String>();

            // Finds all nouns, proper nouns and adjectiveList in the sentence.
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                logger.debug(String.format("Found token: %s, word: %s, ne: %s", word, pos, ne));

                if (!isHashTagOrMention(word)) {
                    if (PROPERNOUN_TYPE.contains(pos) && !properNounList.contains(word)) {
                        properNounList.add(word);
                    } else if (NOUN_TYPE.contains(pos) && !nounList.contains(word)) {
                        nounList.add(word);
                    } else if (ADJECTIVE_TYPE.contains(pos) && !adjectiveList.contains(word)) {
                        adjectiveList.add(word);
                    }
                }
            }

            List<Triplet<Subj, Verb, Obj>> tripletList = tripletTagger.tagSemantics(entry);

            for (Triplet triplet : tripletList) {

                // Set Subject Proper Noun and Entity
                if (properNounList.contains(triplet.getSubject().getWord())) {
                    triplet.getSubject().setProperNoun(true, tagEntity(triplet.getSubject().getWord()));
                } else if (nounList.contains(triplet.getSubject().getWord())) {
                    triplet.getSubject().setNoun(true);
                }

                // Set Object Proper Noun and Entity
                if (properNounList.contains(triplet.getObject().getWord())) {
                    triplet.getObject().setProperNoun(true, tagEntity(triplet.getSubject().getWord()));
                } else if (nounList.contains(triplet.getObject().getWord())) {
                    triplet.getObject().setNoun(true);
                }

                // Remove unrelated Nouns
                if (properNounList.contains(triplet.getSubject().getWord())) {
                    properNounList.remove(triplet.getSubject().getWord());
                }
                if (nounList.contains(triplet.getSubject().getWord())) {
                    nounList.remove(triplet.getSubject().getWord());
                }
                if (properNounList.contains(triplet.getObject().getWord())) {
                    properNounList.remove(triplet.getObject().getWord());
                }
                if (nounList.contains(triplet.getObject().getWord())) {
                    nounList.remove(triplet.getObject().getWord());
                }
            }

            TaggedSentence posTaggedSentence = new TaggedSentence(sentence.toString(), tripletList, adjectiveList, properNounList, nounList);
            posTaggedSentences.add(posTaggedSentence);
        }
        return posTaggedSentences;

    }

    @Override
    protected String tagEntity(String entityName) {
        String category = nerTagger.identifyNER(entityName);
        logger.info(entityName + ": Category: " + category);
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


