import Database.DatabaseParserFromCypher;
import Query.QueryParserFromCypher;
import Transducer.TransducerParserFromCypher;

import java.util.Locale;

public class main {

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
}
