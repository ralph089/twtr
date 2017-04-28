package edu.hm.ccwi.semantic.tagger.triplet.models;

/**
 * Created by ralph on 27.04.17.
 */
public class Subj extends GrammaticalElement {

    /**
     * The Is proper noun.
     */
    boolean isProperNoun;
    /**
     * The Is noun.
     */
    boolean isNoun;

    private String entity;

    /**
     * Instantiates a new Subj.
     *
     * @param word the word
     */
    public Subj(String word) {
        super(word);
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Is proper noun boolean.
     *
     * @return the boolean
     */
    public boolean isProperNoun() {
        return isProperNoun;
    }

    /**
     * Sets proper noun.
     *
     * @param properNoun the proper noun
     * @param entity     the entity
     */
    public void setProperNoun(boolean properNoun, String entity) {

        isProperNoun = properNoun;
        this.entity = entity;
    }

    /**
     * Is noun boolean.
     *
     * @return the boolean
     */
    public boolean isNoun() {
        return isNoun;
    }

    /**
     * Sets noun.
     *
     * @param noun the noun
     */
    public void setNoun(boolean noun) {
        isNoun = noun;
    }

    @Override
    public String toString() {
        return "Subj{" +
                "word='" + word + '\'' +
                ", isProperNoun=" + isProperNoun +
                ", isNoun=" + isNoun +
                '}';
    }
}
