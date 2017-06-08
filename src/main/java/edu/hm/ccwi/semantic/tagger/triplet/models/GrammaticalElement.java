package edu.hm.ccwi.semantic.tagger.triplet.models;

/**
 * A Grammatical Element like a Subject, a Verb or a Object.
 *
 * @author Ralph Offinger
 */
public class GrammaticalElement {
    /**
     * The Word.
     */
    String word;

    /**
     * Instantiates a new Semantic role.
     *
     * @param word the word
     */
    public GrammaticalElement(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }

}
