import java.util.Locale;

public class main {

    public static void main(String[] args) throws Exception {

        // todo (later or next project): transducer input on args[1], move databaseInput to args[2]
        String queryInput = args[0]; // "query-1.cypher";
        String transducerInput = args[1];
        String databaseInput = args[2]; // "play-graph.txt0.txt";

        QueryParser queryParser = new QueryParser(queryInput);
        queryParser.readCypherQuery();

        TransducerParser transducerParser = new TransducerParser(queryParser);

        if (transducerInput.toLowerCase(Locale.ROOT).equals("gmark")) {
            transducerParser.gMarkEditDistance();
        } else {
            transducerParser.emptyTransducer();
        }

        DatabaseParser databaseParser = new DatabaseParser(queryParser, databaseInput);
        databaseParser.readDatabaseGraph();
    }
}
