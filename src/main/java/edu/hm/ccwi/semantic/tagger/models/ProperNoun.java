package edu.hm.ccwi.semantic.tagger.models;

/**
 * Created by ralph on 28.04.17.
 */
public class ProperNoun {
    private String word;
    private String entity;

    public ProperNoun(String word, String entity) {
        this.word = word;
        this.entity = entity;
    }

    public String getWord() {
        return word;
    }

    public String getEntity() {
        return entity;
    }
}
