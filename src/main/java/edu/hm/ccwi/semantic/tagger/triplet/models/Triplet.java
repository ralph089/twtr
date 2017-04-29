package edu.hm.ccwi.semantic.tagger.triplet.models;

import org.apache.commons.lang3.Validate;


/**
 * A Triplet of a Subject-Verb-Object.
 *
 * @param <S> the type parameter
 * @param <V> the type parameter
 * @param <O> the type parameter
 * @author Max Auch
 */
public class Triplet<S, V, O> {

    private final Subj subject;
    private final Verb verb;
    private final Obj object;

    /**
     * Instantiates a new SVO-Triplet.
     *
     * @param subject the subject
     * @param verb    the verb
     * @param object  the object
     */
    public Triplet(Subj subject, Verb verb, Obj object) {
        this.subject = Validate.notNull(subject, "Empty Subject not allowed");
        this.verb = Validate.notNull(verb, "Empty Verb not allowed!");
        this.object = Validate.notNull(object, "Empty Object not allowed!");
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public Subj getSubject() {
        return subject;
    }

    /**
     * Gets verb.
     *
     * @return the verb
     */
    public Verb getVerb() {
        return verb;
    }

    /**
     * Gets object.
     *
     * @return the object
     */
    public Obj getObject() {
        return object;
    }

    @Override
    public String toString() {
        return String.format("Subjekt: %s, Verb: %s, Objekt: %s", subject, verb, object);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;

        if (!subject.equals(triplet.subject)) return false;
        if (!verb.equals(triplet.verb)) return false;
        return object.equals(triplet.object);
    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + verb.hashCode();
        result = 31 * result + object.hashCode();
        return result;
    }
}