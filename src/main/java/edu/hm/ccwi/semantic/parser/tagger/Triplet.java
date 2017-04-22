package edu.hm.ccwi.semantic.parser.tagger;

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
        this.subject = subject;
        this.verb = verb;
        this.object = object;
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