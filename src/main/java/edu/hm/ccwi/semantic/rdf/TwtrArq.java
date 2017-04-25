package edu.hm.ccwi.semantic.rdf;

import edu.hm.ccwi.semantic.rdf.vocab.TWTR;
import org.apache.jena.query.*;

/**
 * Querys TwtrModels.
 * <p>
 * ARQ is a query engine for Jena that supports the SPARQL RDF Query language.
 */
public class TwtrArq {

    private TwtrModel twtrModel;

    /**
     * Instantiates a new Twtr arq.
     *
     * @param twtrModel the twtr model
     */
    public TwtrArq(TwtrModel twtrModel) {
        this.twtrModel = twtrModel;
    }

    /**
     * Prints all Subjects, Verbs, and Objects of a Tweet.
     */
    public void printSemantics() {

        String queryString = String.join(System.getProperty("line.separator"),
                String.format("PREFIX twtr: <%s>", TWTR.getURI()),
                "SELECT ?author ?tweetText ?subjText ?verbText ?objText",
                "WHERE",
                "{",
                "?subject a twtr:TwitterAccount .",
                "?subject twtr:userName ?author .",
                "?subject twtr:tweeted ?tweet .",

                "?tweet twtr:tweetText ?tweetText .",
                "?tweet twtr:mentions ?s .",
                "?tweet twtr:mentions ?v .",
                "?tweet twtr:mentions ?o .",

                "?s a twtr:Subject .",
                "?s twtr:text ?subjText .",

                "?v a twtr:Verb .",
                "?v twtr:text ?verbText .",

                "?o a twtr:Object .",
                "?o twtr:text ?objText .",
                "}"
        );



        printQueryResults(queryString);
    }

    /**
     * Prints all Tweets with a Proper Noun.
     */
    public void printTweetsWithProperNoun() {

        String queryString = String.join(System.getProperty("line.separator"),
                String.format("PREFIX twtr: <%s>", TWTR.getURI()),
                "SELECT ?author ?tweetText ?propNounText",
                "WHERE",
                "{",
                "?subject a twtr:TwitterAccount .",
                "?subject twtr:userName ?author .",
                "?subject twtr:tweeted ?tweet .",

                "?tweet twtr:tweetText ?tweetText .",

                "?tweet twtr:contains ?propn .",

                "?propn a twtr:ProperNoun .",
                "?propn twtr:isA twtr:Organisation .",
                "?propn twtr:word ?propNounText .",
                "}"
        );

        printQueryResults(queryString);
    }

    private void printQueryResults(String q) {
        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query, twtrModel.getModel());
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(results, twtrModel.getModel());
        } finally {
            qexec.close();
        }
    }
}
