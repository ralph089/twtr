package edu.hm.ccwi.semantic.tagger.models;

import edu.hm.ccwi.semantic.tagger.triplet.models.Obj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Subj;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Verb;

import java.util.List;

/**
 * A tagged Sentence of a tweet.
 *
 * @author Ralph Offinger
 */
public class TaggedSentence {

    private String sentence;
    private List<Triplet<Subj, Verb, Obj>> triplets;
    private List<String> adjectives;
    private List<ProperNoun> unrelatedProperNouns;
    private List<String> unrelatedCommonNouns;

    /**
     * Instantiates a new Tagged sentence.
     *
     * @param sentence             the sentence
     * @param triplets             the triplets
     * @param adjectives           the adjectives
     * @param unrelatedProperNouns the unrelated proper nouns
     * @param unrelatedCommonNouns the unrelated common nouns
     */
    public TaggedSentence(String sentence, List<Triplet<Subj, Verb, Obj>> triplets, List<String> adjectives, List<ProperNoun> unrelatedProperNouns, List<String> unrelatedCommonNouns) {
        this.sentence = sentence;
        this.triplets = triplets;
        this.adjectives = adjectives;
        this.unrelatedProperNouns = unrelatedProperNouns;
        this.unrelatedCommonNouns = unrelatedCommonNouns;
    }

    /**
     * Gets unrelated proper nouns.
     *
     * @return the unrelated proper nouns
     */
    public List<ProperNoun> getUnrelatedProperNouns() {
        return unrelatedProperNouns;
    }

    /**
     * Gets unrelated common nouns.
     *
     * @return the unrelated common nouns
     */
    public List<String> getUnrelatedCommonNouns() {
        return unrelatedCommonNouns;
    }

    /**
     * Gets sentence.
     *
     * @return the sentence
     */
    public String getSentence() {
        return sentence;
    }

    /**
     * Gets triplets.
     *
     * @return the triplets
     */
    public List<Triplet<Subj, Verb, Obj>> getTriplets() {
        return triplets;
    }

    /**
     * Gets adjectives.
     *
     * @return the adjectives
     */
    public List<String> getAdjectives() {
        return adjectives;
    }
}
