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
     * Prints all Subjects which are a Common Noun.
     */
    public void printInferenceCN() {

        String queryString = String.join(System.getProperty("line.separator"),
                String.format("PREFIX twtr: <%s>", TWTR.getURI()),
                "PREFIX  rdfs:<http://www.w3.org/2000/01/rdf-schema#>",
                "SELECT ?properNoun",
                "WHERE",
                "{",
                "?tweet twtr:contains ?pos . ",
                "?element rdfs:subClassOf ?pos . ",
                "?element a twtr:ProperNoun . ",
                "?element twtr:word ?properNoun . ",
                "}"
        );


        printQueryResults(queryString);
    }

    /**
     * Prints all Subjects which are a Proper Noun.
     */
    public void printSubjectProperNouns() {

        String queryString = String.join(System.getProperty("line.separator"),
                String.format("PREFIX twtr: <%s>", TWTR.getURI()),
                "SELECT ?author ?tweetText ?subjWord",
                "WHERE",
                "{",
                "?account a twtr:TwitterAccount .",
                "?account twtr:userName ?author .",
                "?account twtr:tweeted ?tweet .",

                "?tweet twtr:tweetText ?tweetText .",
                "?tweet twtr:contains ?triplet .",

                "?triplet a twtr:Triplet .",
                "?triplet twtr:has ?subj.",

                "?subj a twtr:Subject .",
                "?subj a twtr:ProperNoun .",
                "?subj twtr:word ?subjWord .",
                "}"
        );


        printQueryResults(queryString);
    }


    /**
     * Prints all Tweets with a Subject, Verb, Object-Triple.
     */
    public void printTweetsWithTriplet() {

        String queryString = String.join(System.getProperty("line.separator"),
                String.format("PREFIX twtr: <%s>", TWTR.getURI()),
                "SELECT ?author ?tweetText ?subjWord ?verbWord ?objWord",
                "WHERE",
                "{",
                "?account a twtr:TwitterAccount .",
                "?account twtr:userName ?author .",
                "?account twtr:tweeted ?tweet .",

                "?tweet twtr:tweetText ?tweetText .",
                "?tweet twtr:contains ?triplet .",

                "?triplet a twtr:Triplet .",
                "?triplet twtr:has ?subj.",
                "?triplet twtr:has ?verb.",
                "?triplet twtr:has ?obj.",

                "?subj a twtr:Subject .",
                "?subj twtr:word ?subjWord .",

                "?verb a twtr:Verb .",
                "?verb twtr:word ?verbWord .",

                "?obj a twtr:Object .",
                "?obj twtr:word ?objWord .",
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
