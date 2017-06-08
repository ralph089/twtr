package edu.hm.ccwi.semantic.tagger.triplet.models;

/**
 * Created by ralph on 08.06.17.
 */
public interface SubjObj {

    public boolean isProperNoun();

    public void setProperNoun();

    public void setEntity(String entity);

    public String getWord();

    public String getEntity();
}
