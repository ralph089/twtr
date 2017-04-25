package edu.hm.ccwi.semantic.tagger;

import org.apache.commons.lang3.Validate;

/**
 * A Triplet of a Subject-Verb-Object.
 *
 * @param <T> the Subject
 * @param <U> the Verb
 * @param <V> the Object
 * @author Max Auch
 */
public class Triplet<T, U, V> {

    private final T subject;
    private final U verb;
    private final V object;

    /**
     * Instantiates a new SVO-Triplet.
     *
     * @param subject the subject
     * @param verb    the verb
     * @param object  the object
     */
    public Triplet(T subject, U verb, V object) {
        this.subject = Validate.notNull(subject, "Empty Subject not allowed");
        this.verb = Validate.notNull(verb, "Empty Verb not allowed!");
        this.object = Validate.notNull(object, "Empty Object not allowed!");
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public T getSubject() {
        return subject;
    }

    /**
     * Gets the verb.
     *
     * @return the verb
     */
    public U getVerb() {
        return verb;
    }

    /**
     * Gets the object.
     *
     * @return the object
     */
    public V getObject() {
        return object;
    }

    @Override
    public String toString() {
        return String.format("Subjekt: %s, Verb: %s, Objekt: %s", subject, verb, object);
    }
}