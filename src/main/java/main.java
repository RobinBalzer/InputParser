import Database.DatabaseParserFromCypher;
import Parser.SPARQLParser;
import Parser.Util;
import Query.QueryParserFromCypher;
import Settings.Settings;
import Transducer.TransducerParserFromCypher;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.QueryFactory;

import java.io.IOException;
import java.util.Locale;

public class main {

    public static void main(String[] args) throws IOException {


        SPARQLParser sparqlParser = new SPARQLParser();

        sparqlParser.readQuery(Settings.inputFileDirectory + "query-3.sparql");
        Util.printList(sparqlParser.getQueryNodeList());
        Util.printList(sparqlParser.getQueryEdgeList());

        System.out.println("-----");

        SPARQLParser sparqlParser1 = new SPARQLParser();

        sparqlParser1.readQuery(Settings.inputFileDirectory + "query-5.sparql");
        Util.printList(sparqlParser1.getQueryNodeList());
        Util.printList(sparqlParser1.getQueryEdgeList());
    }
    /*
    public static void main(String[] args) throws Exception {

        String queryInput = args[0]; // "query-1.cypher";
        String transducerInput = args[1];
        String databaseInput = args[2]; // "play-graph.txt0.txt";

        QueryParserFromCypher queryParser = new QueryParserFromCypher(queryInput);
        queryParser.readCypherQuery();

        TransducerParserFromCypher transducerParser = new TransducerParserFromCypher(queryParser);

        if (transducerInput.toLowerCase(Locale.ROOT).equals("gmark")) {
            transducerParser.gMarkEditDistance();
        } else {
            transducerParser.emptyTransducer();
        }

        DatabaseParserFromCypher databaseParser = new DatabaseParserFromCypher(queryParser, databaseInput);
        databaseParser.readDatabaseGraph();
    }

     */


}
