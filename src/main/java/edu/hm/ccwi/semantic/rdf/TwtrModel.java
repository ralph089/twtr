package edu.hm.ccwi.semantic.rdf;

import edu.hm.ccwi.semantic.commons.twitter.TaggedTweet;
import edu.hm.ccwi.semantic.rdf.vocab.TWTR;
import edu.hm.ccwi.semantic.tagger.Triplet;
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
        Resource twitterAccount = createTwitterAccount(taggedTweet);
        Resource tweet = createTweet(taggedTweet);

        twitterAccount.addProperty(TWTR.tweeted, tweet);

        taggedTweet.getTriplet().forEach((sentence, triplet) -> {
            createSVO(tweet, triplet);
        });

        if (taggedTweet.getProperNoun() != null) {
            Resource properNoun = model.createResource()
                    .addProperty(RDF.type, TWTR.ProperNoun)
                    .addProperty(TWTR.word, taggedTweet.getProperNoun().getEntity());

            switch (taggedTweet.getProperNoun().getType()) {
                case "Person":
                    properNoun.addProperty(TWTR.isA, TWTR.Person);
                    break;
                case "Company":
                    properNoun.addProperty(TWTR.isA, TWTR.Organisation);
                    break;
                case "Location":
                    properNoun.addProperty(TWTR.isA, TWTR.Place);
                    break;
                default:
                    properNoun.addProperty(TWTR.isA, TWTR.Person);
                    break;
            }

            tweet.addProperty(TWTR.contains, properNoun);
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
        //InfModel rdfModel = ModelFactory.createRDFSModel(rdfModel1);

        rdfModel.setNsPrefixes(new HashMap() {{
            put("rdf", RDF.getURI());
            put("rdfs", RDFS.getURI());
            put("xsd", XSD.NS);
            put("twtr", TWTR.getURI());
        }});

        return rdfModel;
    }

    private void createSVO(Resource tweet, Triplet triplet) {
        Resource subject = model.createResource()
                .addProperty(RDF.type, TWTR.Subject)
                .addProperty(TWTR.text, triplet.getSubject().toString());

        Resource verb = model.createResource()
                .addProperty(RDF.type, TWTR.Verb)
                .addProperty(TWTR.text, triplet.getVerb().toString());

        Resource object = model.createResource()
                .addProperty(RDF.type, TWTR.Object)
                .addProperty(TWTR.text, triplet.getObject().toString());

        tweet.addProperty(TWTR.mentions, subject);
        tweet.addProperty(TWTR.mentions, verb);
        tweet.addProperty(TWTR.mentions, object);
    }

    private Resource createTweet(TaggedTweet taggedTweet) {
        return model.createResource(TWTR.getURI() + "Tweet/" + taggedTweet.getTweetID())
                .addProperty(RDF.type, TWTR.Tweet)
                .addProperty(TWTR.tweetID, model.createTypedLiteral(taggedTweet.getTweetID()))
                .addProperty(TWTR.tweetText, model.createTypedLiteral(taggedTweet.getTweetText()));
    }

    private Resource createTwitterAccount(TaggedTweet taggedTweet) {
        return model.createResource(TWTR.getURI() + taggedTweet.getTwitterUser().getUserID())
                .addProperty(RDF.type, TWTR.TwitterAccount)
                .addProperty(TWTR.userName, model.createTypedLiteral(taggedTweet.getTwitterUser().getUserName()))
                .addProperty(TWTR.userID, model.createTypedLiteral(taggedTweet.getTwitterUser().getUserID()))
                .addProperty(TWTR.followerCount, model.createTypedLiteral(taggedTweet.getTwitterUser().getFollowerCount()))
                .addProperty(TWTR.userDescription, model.createTypedLiteral(taggedTweet.getTwitterUser().getUserDescription()));
    }
}
