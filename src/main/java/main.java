public class main {

    public static void main(String[] args) throws Exception {

        String queryInput = "query-3.cypher";
        String databaseInput = "play-graph.txt0.txt";
        QueryParser queryParser = new QueryParser(queryInput);
        queryParser.readCypherQuery();

        DatabaseParser databaseParser = new DatabaseParser(queryParser, databaseInput);
        databaseParser.readDatabaseGraph();
    }
}
