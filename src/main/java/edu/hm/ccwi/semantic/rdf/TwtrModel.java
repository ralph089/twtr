package edu.hm.ccwi.semantic.rdf;

import edu.hm.ccwi.semantic.rdf.vocab.TWTR;
import edu.hm.ccwi.semantic.tagger.models.ProperNoun;
import edu.hm.ccwi.semantic.tagger.models.TaggedSentence;
import edu.hm.ccwi.semantic.tagger.models.TaggedTweet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.HashMap;


/**
 * The TWTR Model, a Jena RDF graph.
 *
 * @author Ralph Offinger
 */
public class TwtrModel {

    private static final Logger log = LoggerFactory.getLogger(TwtrModel.class);
    private Model model;

    /**
     * Instantiates a new TWTR Model.
     */
    public TwtrModel() {
        this.model = create();
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Add a {@link TaggedTweet} to the TWTR Model.
     *
     * @param taggedTweet the tagged tweet
     */
    public void addTweet(TaggedTweet taggedTweet) {

        // TwitterAccount tweeted Tweet
        Resource twitterAccount = createTwitterAccount(taggedTweet);

        Resource tweet = createTweet(taggedTweet);

        twitterAccount.addProperty(TWTR.tweeted, tweet);

        // Tweet mentions a Triplet and contains Part-of-Speech

        for (TaggedSentence sentence : taggedTweet.getTaggedSentences()) {
            createTriplet(tweet, sentence);
            createAdjectives(tweet, sentence);
            createUnrelatedNouns(tweet, sentence);
        }
    }

    private void createUnrelatedNouns(Resource tweet, TaggedSentence sentence) {
        for (ProperNoun pNounObj : sentence.getUnrelatedProperNouns()) {
            Resource pNoun = model.createResource()
                    .addProperty(RDF.type, TWTR.ProperNoun)
                    .addProperty(TWTR.word, pNounObj.getWord());
            createEntity(pNoun, pNounObj.getEntity());
            tweet.addProperty(TWTR.contains, pNoun);
        }

        for (String cNounStr : sentence.getUnrelatedCommonNouns()) {
            Resource cNoun = model.createResource()
                    .addProperty(RDF.type, TWTR.ProperNoun)
                    .addProperty(TWTR.word, cNounStr);
            tweet.addProperty(TWTR.contains, cNoun);
        }
    }

    private void createTriplet(Resource tweet, TaggedSentence sentence) {
        for (Triplet triplet : sentence.getTriplets()) {

            Resource triple = model.createResource()
                    .addProperty(RDF.type, TWTR.Triplet);

            // Create Subject
            Resource subject = model.createResource()
                    .addProperty(RDF.type, TWTR.Subject)
                    .addProperty(TWTR.word, triplet.getSubject().getWord());

            if (triplet.getObject().isProperNoun()) {
                subject.addProperty(TWTR.is, TWTR.ProperNoun);
            } else if (triplet.getObject().isNoun()) {
                subject.addProperty(TWTR.is, TWTR.CommonNoun);
            }

            // Create Verb
            Resource verb = model.createResource()
                    .addProperty(RDF.type, TWTR.Verb)
                    .addProperty(TWTR.word, triplet.getVerb().getWord());

            // Create Object
            Resource object = model.createResource()
                    .addProperty(RDF.type, TWTR.Object)
                    .addProperty(TWTR.word, triplet.getObject().getWord());

            if (triplet.getObject().isProperNoun()) {
                object.addProperty(TWTR.is, TWTR.ProperNoun);
            } else if (triplet.getObject().isNoun()) {
                object.addProperty(TWTR.is, TWTR.CommonNoun);
            }

            // Create Links
            tweet.addProperty(TWTR.contains, triple);
            triple.addProperty(TWTR.has, subject);
            triple.addProperty(TWTR.has, verb);
            triple.addProperty(TWTR.has, object);
        }
    }

    /**
     * Output of the Model as RDF/XML.
     *
     * @param out output, e. g. System.out
     */
    public void write(OutputStream out) {
        model.write(out, "RDF/XML");
    }

    private Model create() {

        Model rdfModel = ModelFactory.createDefaultModel();
        //rdfModel.read("vocab/twtr.ttl") ;

        rdfModel.setNsPrefixes(new HashMap() {{
            put("rdf", RDF.getURI());
            put("rdfs", RDFS.getURI());
            put("xsd", XSD.NS);
            put("twtr", TWTR.getURI());
        }});

        return rdfModel;
    }

    private void createAdjectives(Resource tweet, TaggedSentence posTaggedSentence) {
        // Adjectives
        for (String adjStr : posTaggedSentence.getAdjectives()) {
            Resource adjective = model.createResource()
                    .addProperty(RDF.type, TWTR.Adjective)
                    .addProperty(TWTR.word, adjStr);
            tweet.addProperty(TWTR.contains, adjective);
        }
    }

    private Resource createTweet(TaggedTweet taggedTweet) {
        return model.createResource(TWTR.getURI() + "Tweet/" + taggedTweet.getTweet().getTweetID())
                .addProperty(RDF.type, TWTR.Tweet)
                .addProperty(TWTR.tweetID, model.createTypedLiteral(taggedTweet.getTweet().getTweetID()))
                .addProperty(TWTR.tweetText, model.createTypedLiteral(taggedTweet.getTweet().getTweetText()));
    }

    private Resource createTwitterAccount(TaggedTweet taggedTweet) {
        Resource user = model.createResource(TWTR.getURI() + taggedTweet.getTweet().getTwitterUser().getUserID())
                .addProperty(RDF.type, TWTR.TwitterAccount)
                .addProperty(TWTR.userName, model.createTypedLiteral(taggedTweet.getTweet().getTwitterUser().getUserName()))
                .addProperty(TWTR.userID, model.createTypedLiteral(taggedTweet.getTweet().getTwitterUser().getUserID()))
                .addProperty(TWTR.followerCount, model.createTypedLiteral(taggedTweet.getTweet().getTwitterUser().getFollowerCount()))
                .addProperty(TWTR.userDescription, model.createTypedLiteral(taggedTweet.getTweet().getTwitterUser().getUserDescription()));

        createEntity(user, taggedTweet.getTweet().getTwitterUser().getType());

        return user;
    }

    private void createEntity(Resource res, String type) {
        if (type != null && res != null) {
            switch (type) {
                case "PERSON":
                    res.addProperty(RDF.type, TWTR.Person);
                    break;
                case "ORGANIZATION":
                    res.addProperty(RDF.type, TWTR.Organisation);
                    break;
                case "LOCATION":
                    res.addProperty(RDF.type, TWTR.Place);
                    break;
            }
        }
    }
}
