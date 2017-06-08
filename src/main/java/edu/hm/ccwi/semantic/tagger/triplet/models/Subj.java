package edu.hm.ccwi.semantic.tagger.triplet.models;

/**
 * Created by ralph on 27.04.17.
 */
public class Subj extends GrammaticalElement implements SubjObj {

    /**
     * The Is proper noun.
     */
    private boolean isProperNoun;
    /**
     * The Is noun.
     */
    private boolean isNoun;

    private String entity = "";

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
     */
    public void setProperNoun() {
        isProperNoun = true;
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
     */
    public void setNoun() {
        isNoun = true;
    }

    @Override
    public String toString() {
        return "Subj{"
                + "word='" + word + '\''
                + ", isProperNoun=" + isProperNoun
                + ", isNoun=" + isNoun
                + '}';
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
