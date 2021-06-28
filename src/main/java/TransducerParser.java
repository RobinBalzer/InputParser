import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TransducerParser {
    String inputFile;
    String outputFile;
    private ArrayList nodeList;
    private ArrayList edgeList;
    private String path;

    public TransducerParser(QueryParser queryParser) {
        // constructor that provides the edit distance transducer for gMark data.
        // very specific, only use this for experiment/test purposes

        this.outputFile = queryParser.filename;
        this.nodeList = new ArrayList();
        this.edgeList = new ArrayList();

    }

    public TransducerParser(QueryParser queryParser, String inputFile) {
        // handling of a transducer file ...
    }

    public void gMarkEditDistance() {
        String stringBuilder = "";
        // add transducerNode t0
        nodeList.add("t0, true, true");

        // add all edges as self-loops
        edgeList.add("t0, t0, 0, 0, 0"); // transducer doesn't change the label
        edgeList.add("t0, t0, 0, 1, 9");
        edgeList.add("t0, t0, 0, 2, 7");
        edgeList.add("t0, t0, 0, 3, 9");

        edgeList.add("t0, t0, 1, 0, 9");
        edgeList.add("t0, t0, 1, 1, 0"); // transducer doesn't change the label
        edgeList.add("t0, t0, 1, 2, 7");
        edgeList.add("t0, t0, 1, 3, 9");

        edgeList.add("t0, t0, 2, 0, 7");
        edgeList.add("t0, t0, 2, 1, 7");
        edgeList.add("t0, t0, 2, 2, 0"); // transducer doesn't change the label
        edgeList.add("t0, t0, 2, 3, 8");

        edgeList.add("t0, t0, 3, 0, 9");
        edgeList.add("t0, t0, 3, 1, 9");
        edgeList.add("t0, t0, 3, 2, 8");
        edgeList.add("t0, t0, 3, 3, 0"); // transducer doesn't change the label

        // add their negative counterparts.
        // we need this, since else we won't find fitting forward edges when the query explains a backwards edge.
        // e.g. edge (x0) -[ -b]-> (x0) would not result in a found edge with label b.
        edgeList.add("t0, t0, -0, -1, 9");
        edgeList.add("t0, t0, -0, -2, 7");
        edgeList.add("t0, t0, -0, -3, 9");

        edgeList.add("t0, t0, -1, -0, 9");
        edgeList.add("t0, t0, -1, -2, 7");
        edgeList.add("t0, t0, -1, -3, 9");

        edgeList.add("t0, t0, -2, -0, 7");
        edgeList.add("t0, t0, -2, -1, 7");
        edgeList.add("t0, t0, -2, -3, 8");

        edgeList.add("t0, t0, -3, -0, 9");
        edgeList.add("t0, t0, -3, -1, 9");
        edgeList.add("t0, t0, -3, -2, 8");


        writeParsedData();
    }

    public void emptyTransducer() {
        writeEmptyTransducerData();
    }

    private void writeParsedData() {

        //System.out.println("writing the transducer data to file ...");
        String outputFilename = outputFile.split("\\.")[0];
        File output = new File(Settings.outputFileDirectory + outputFilename + ".txt");
        FileWriter out;

        try {
            out = new FileWriter(output, true);
            out.write("transducerGraph: \n");

            // write the nodes ...
            for (Object o : nodeList) {
                out.write(o + "\n");
            }

            // write the edges ...
            out.write("edges: \n");
            for (Object o : edgeList) {
                out.write(o + "\n");
            }

            out.close();
            //System.out.println(" ... done!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeEmptyTransducerData() {
        String outputFilename = outputFile.split("\\.")[0];
        File output = new File(Settings.outputFileDirectory + outputFilename + ".txt");
        FileWriter out;

        try {
            out = new FileWriter(output, true);
            out.write("transducerGraph: \n");

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
