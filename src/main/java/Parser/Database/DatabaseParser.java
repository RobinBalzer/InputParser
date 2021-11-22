package Parser.Database;

import Settings.Settings;

import java.io.*;
import java.util.ArrayList;

public class DatabaseParser {
    private final ArrayList nodeList;
    private final ArrayList edgeList;

    public DatabaseParser(){
        this.edgeList = new ArrayList();
        this.nodeList = new ArrayList();
    }

    public void readDatabase() throws IOException {
        File file = new File(Settings.inputFileDirectory + Settings.inputDatabaseFile);
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
    }

    public ArrayList getNodeList() {
        return nodeList;
    }

    public ArrayList getEdgeList() {
        return edgeList;
    }
}
