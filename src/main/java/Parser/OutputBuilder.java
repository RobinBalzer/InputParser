package Parser;

import Parser.Database.DatabaseParser;
import Parser.Query.SPARQLParser;
import Parser.Transducer.TransducerParser;
import Settings.Settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// at the end of the parse process, we call the FileWriter that outputs us one .txt file
public class OutputBuilder {
    SPARQLParser queryParser;
    TransducerParser transducerParser;
    DatabaseParser databaseParser;

    public OutputBuilder(SPARQLParser queryParser, TransducerParser transducerParser, DatabaseParser databaseParser) {
        this.queryParser = queryParser;
        this.transducerParser = transducerParser;
        this.databaseParser = databaseParser;

    }

    public void writeToFile() throws IOException {
        File output = new File(Settings.outputFileDirectory + Settings.fileName + ".txt");
        FileWriter out = new FileWriter(output, false);

        out.write("name: " + Settings.fileName + "\n");

        writeQuery(out);

        writeTransducer(out);

        writeDatabase(out);

        out.close();

    }

    private void writeQuery(FileWriter out) {

        try {

            out.write("queryGraph: \n");
            out.write("nodes: \n");

            for (Object value : queryParser.getQueryNodeList()) {
                out.write(value + "\n");
            }

            out.write("edges: \n");
            for (Object o : queryParser.getQueryEdgeList()) {
                out.write(o + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeTransducer(FileWriter out) {

        try {

            out.write("transducerGraph: \n");

            // write the nodes ...
            // todo: handling for "nodes:" missing. Check github issue ...
            for (Object item : transducerParser.getNodeList()) {
                out.write(item + "\n");
            }

            // write the edges ...
            out.write("edges: \n");
            for (Object value : transducerParser.getEdgeList()) {
                out.write(value + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeDatabase(FileWriter out) {

        try {

            out.write("databaseGraph: \n");
            out.write("nodes: \n");

            for (Object o : databaseParser.getNodeList()) {
                out.write(o + "\n");
            }

            out.write("edges: \n");
            for (Object o : databaseParser.getEdgeList()) {
                out.write(o + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
