package edu.hm.ccwi.semantic.rdf;

import edu.hm.ccwi.semantic.rdf.vocab.TWTR;
import edu.hm.ccwi.semantic.tagger.models.ProperNoun;
import edu.hm.ccwi.semantic.tagger.models.TaggedSentence;
import edu.hm.ccwi.semantic.tagger.models.TaggedTweet;
import edu.hm.ccwi.semantic.tagger.triplet.models.Triplet;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.ReasonerVocabulary;
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

        // Begin TwitterAccount tweeted Tweet
        Resource twitterAccount = createTwitterAccount(taggedTweet);

        Resource tweet = createTweet(taggedTweet);

        Property tweeted = TWTR.tweeted;
        tweeted.addProperty(RDFS.domain, twitterAccount);
        tweeted.addProperty(RDFS.range, tweet);

        twitterAccount.addProperty(tweeted, tweet);
        // End TwitterAccount tweeted Tweet

        // Begin Tweet contains POS
        Resource pos = model.createResource(TWTR.getURI() + "Tweet/" + taggedTweet.getTweet().getTweetID() + "/POS/")
                .addProperty(RDF.type, TWTR.POS);

        Property contains = TWTR.contains;
        contains.addProperty(RDFS.domain, tweet);
        contains.addProperty(RDFS.range, pos);

        tweet.addProperty(contains, pos);
        // End Tweet contains Elements

        // Begin add Elements

        for (TaggedSentence sentence : taggedTweet.getTaggedSentences()) {
            createTriplet(tweet, sentence, pos);
            createAdjectives(tweet, sentence, pos);
            createUnrelatedNouns(tweet, sentence, pos);
        }
        // End add Elements
    }

    private void createUnrelatedNouns(Resource tweet, TaggedSentence sentence, Resource pos) {
        for (ProperNoun pNounObj : sentence.getUnrelatedProperNouns()) {
            Resource pNoun = model.createResource()
                    .addProperty(RDF.type, TWTR.ProperNoun)
                    .addProperty(TWTR.word, pNounObj.getWord());
            createEntity(pNoun, pNounObj.getEntity());

            pNoun.addProperty(RDFS.subClassOf, pos);
        }

        for (String cNounStr : sentence.getUnrelatedCommonNouns()) {
            Resource cNoun = model.createResource()
                    .addProperty(RDF.type, TWTR.CommonNoun)
                    .addProperty(TWTR.word, cNounStr);

            cNoun.addProperty(RDFS.subClassOf, pos);
        }
    }

    private void createTriplet(Resource tweet, TaggedSentence sentence, Resource pos) {
        for (Triplet triplet : sentence.getTriplets()) {

            // Triplet
            Resource triple = model.createResource()
                    .addProperty(RDF.type, TWTR.Triplet)
                    .addProperty(RDFS.subClassOf, pos);

            Resource subject = model.createResource()
                    .addProperty(RDF.type, TWTR.Subject)
                    .addProperty(TWTR.word, triplet.getSubject().getWord())
                    .addProperty(RDFS.subClassOf, triple);;

            if (triplet.getObject().isProperNoun()) {
                subject.addProperty(RDF.type, TWTR.ProperNoun);
            } else if (triplet.getObject().isNoun()) {
                subject.addProperty(RDF.type, TWTR.CommonNoun);
            }

            // SVO is a Verb
            Resource verb = model.createResource()
                    .addProperty(RDF.type, TWTR.Verb)
                    .addProperty(TWTR.word, triplet.getVerb().getWord())
                    .addProperty(RDFS.subClassOf, triple);;

            // SVO is a object
            Resource object = model.createResource()
                    .addProperty(RDF.type, TWTR.Object)
                    .addProperty(TWTR.word, triplet.getObject().getWord())
                    .addProperty(RDFS.subClassOf, triple);;

            if (triplet.getObject().isProperNoun()) {
                object.addProperty(RDF.type, TWTR.ProperNoun);
            } else if (triplet.getObject().isNoun()) {
                object.addProperty(RDF.type, TWTR.CommonNoun);
            }

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

    /**
     * Uploads the model to Fuseki.
     *
     * @param serviceURI Location of the datastore
     */
    public void uploadToFuseki(String serviceURI) {
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
        accessor.putModel(model);
        //accessor.add(model);
        log.info("Uploaded data to Fuseki!");
    }

    private Model create() {

        Model rdfModel = ModelFactory.createDefaultModel();
        Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(null);
        reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel,
                ReasonerVocabulary.RDFS_SIMPLE);
        InfModel rdfsModel = ModelFactory.createInfModel(reasoner, rdfModel);

        rdfsModel.setNsPrefixes(new HashMap() {{
            put("rdf", RDF.getURI());
            put("rdfs", RDFS.getURI());
            put("xsd", XSD.NS);
            put("twtr", TWTR.getURI());
        }});

        return rdfsModel;
    }

    private void createAdjectives(Resource tweet, TaggedSentence posTaggedSentence, Resource pos) {
        // Adjectives
        for (String adjStr : posTaggedSentence.getAdjectives()) {
            Resource adjective = model.createResource()
                    .addProperty(RDF.type, TWTR.Adjective)
                    .addProperty(TWTR.word, adjStr);
            adjective.addProperty(RDFS.subClassOf, pos);
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
