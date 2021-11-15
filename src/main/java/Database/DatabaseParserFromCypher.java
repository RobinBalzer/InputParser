package Database;

import Query.QueryParserFromCypher;
import Settings.Settings;

import java.io.*;
import java.util.ArrayList;

@Deprecated
public class DatabaseParserFromCypher {
    String filename;
    String outputFile;
    private final ArrayList nodeList;
    private final ArrayList edgeList;
    private final String path;

    public DatabaseParserFromCypher(QueryParserFromCypher queryParser, String inputFile) {
        this.path = Settings.inputFileDirectory + inputFile;
        this.outputFile = queryParser.filename;
        this.filename = inputFile;
        this.nodeList = new ArrayList();
        this.edgeList = new ArrayList();
    }

    public void readDatabaseGraph() throws Exception {

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        String[] lineData;
        String source;
        String target;
        String label;

        while ((st = br.readLine()) != null) {
            lineData = st.split(" ");
            source = lineData[0];
            target = lineData[2];
            label = lineData[1];

            if (!nodeList.contains(source)) {
                nodeList.add(source);
            }

            if (!nodeList.contains(target)) {
                nodeList.add(target);
            }

            edgeList.add(source + ", " + target + ", " + label);
        }

        writeParsedData();

    }


    private void writeParsedData() {
        //System.out.println("writing to database data to file ...");
        String outputFilename = outputFile.split("\\.")[0];
        File output = new File(Settings.outputFileDirectory + outputFilename + ".txt");
        FileWriter out;

        try {

            out = new FileWriter(output, true);

            out.write("databaseGraph: \n");
            out.write("nodes: \n");

            // todo: write the nodes ...
            for (Object o : nodeList) {
                out.write(o + "\n");
            }

            out.write("edges: \n");
            // todo: write the edges ...
            for (Object o : edgeList) {
                out.write(o + "\n");
            }

            out.close();
            //System.out.println(" ... done!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
