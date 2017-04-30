package edu.hm.ccwi.semantic.tagger.models;

/**
 * A Proper Noun.
 *
 * @author Ralph Offinger
 */
public class ProperNoun {
    private String word;
    private String entity;

    /**
     * Instantiates a new Proper noun.
     *
     * @param word   the word
     * @param entity the entity
     */
    public ProperNoun(String word, String entity) {
        this.word = word;
        this.entity = entity;
    }

    /**
     * Gets word.
     *
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public String getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProperNoun that = (ProperNoun) o;

        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }
}
