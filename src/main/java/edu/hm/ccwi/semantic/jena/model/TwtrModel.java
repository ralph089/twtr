package edu.hm.ccwi.semantic.jena.model;

import edu.hm.ccwi.semantic.jena.vocabulary.TWTR;
import edu.hm.ccwi.semantic.parser.tagger.TaggedTweet;
import edu.hm.ccwi.semantic.parser.tagger.Triplet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.io.OutputStream;
import java.util.HashMap;


/**
 * The Jena TWTR Model.
 *
 * @author Ralph Offinger
 */
public class TwtrModel {

    private Model model;

    /**
     * Instantiates a new TWTR-model.
     */
    public TwtrModel() {
        this.model = create();
    }

    /**
     * Add a {@link TaggedTweet} to the TWTR-Model.
     *
     * @param taggedTweet the tagged tweet
     */
    public void addTweet(TaggedTweet taggedTweet) {
        Resource twitterAccount = createTwitterAccount(taggedTweet);
        Resource tweet = createTweet(taggedTweet);

        twitterAccount.addProperty(TWTR.tweeted, tweet);

        taggedTweet.getAnalysedTweet().forEach((sentence, triplet) -> {
            createSVO(tweet, triplet);
        });
    }

    /**
     * Output of the RDF-Graph.
     *
     * @param out the out
     */
    public void write(OutputStream out) {
        model.write(out, "RDF/XML");
    }

    private Model create() {

        Model rdfModel = ModelFactory.createDefaultModel();

        rdfModel.setNsPrefixes(new HashMap() {{
            put("rdf", RDF.getURI());
            put("rdfs", RDFS.getURI());
            put("xsd", XSD.NS);
            put("twtr", TWTR.getURI());
        }});

        return rdfModel;
    }

    private void createSVO(Resource tweet, Triplet triplet) {
        Resource subject = model.createResource(TWTR.getURI() + toCamelCase(triplet.getSubject().toString()))
                .addProperty(RDF.type, TWTR.TweetSubject);

        Property verb = model.createProperty(TWTR.getURI() + toCamelCase(triplet.getVerb().toString()));

        Resource object = model.createResource(TWTR.getURI() + toCamelCase(triplet.getObject().toString()))
                .addProperty(RDF.type, TWTR.TweetObject);

        Statement triple = ResourceFactory.createStatement(subject, verb, object);
        model.add(triple);

        tweet.addProperty(TWTR.mentions, subject);
    }

    private Resource createTweet(TaggedTweet taggedTweet) {
        return model.createResource(TWTR.getURI() + "Tweet/" + taggedTweet.getTweetID())
                .addProperty(RDF.type, TWTR.Tweet)
                .addProperty(TWTR.tweetID, model.createTypedLiteral(taggedTweet.getTweetID()))
                .addProperty(TWTR.tweetText, model.createTypedLiteral(taggedTweet.getTweetText()));
    }

    private Resource createTwitterAccount(TaggedTweet taggedTweet) {
        return model.createResource(TWTR.getURI() + toCamelCase(taggedTweet.getTwitterUser().getUserName()))
                .addProperty(RDF.type, TWTR.TwitterAccount)
                .addProperty(TWTR.userName, model.createTypedLiteral(taggedTweet.getTwitterUser().getUserName()))
                .addProperty(TWTR.userID, model.createTypedLiteral(taggedTweet.getTwitterUser().getUserID()))
                .addProperty(TWTR.followerCount, model.createTypedLiteral(taggedTweet.getTwitterUser().getFollowerCount()))
                .addProperty(TWTR.userDescription, model.createTypedLiteral(taggedTweet.getTwitterUser().getUserDescription()));
    }

    private String toCamelCase(String s) {
        return StringUtils.remove(WordUtils.capitalizeFully(s, ' '), " ");
    }
}
