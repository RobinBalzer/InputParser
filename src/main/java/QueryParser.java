import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class QueryParser {
    private String path;
    public String filename;
    private ArrayList nodeList;
    private ArrayList edgeList;
    private int tempNodeCounter;

    public QueryParser(String inputFile) {
        this.path = "src/main/resources/input/" + inputFile;
        this.filename = inputFile;
        this.nodeList = new ArrayList();
        this.edgeList = new ArrayList();
        this.tempNodeCounter = 0;
    }

    public void readCypherQuery() throws Exception {
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();
            String[] words;

            // this filters out everything but the pattern
            words = line.split("MATCH | RETURN ");
            String pattern = words[1];
            // System.out.println(pattern);

            // this gives us the initial and final node and its edges for the query
            // and every edge in between arbitrary nodes (all nodes between initial and final node)
            String[] edges = pattern.split("\\(\\)");

            // System.out.println(edges[0] + " \n" + edges[1] + " \n" + edges[2] + " \n" + edges[3]);


            String label;
            String sourceNode;
            String sourceNodeId;
            String targetNode;
            String targetNodeId;
            String lastCreatedTempNode = "";
            String lastCreatedTempNodeId = "";

            for (String tempStr : edges) {
                String stringBuilder = "";

                // check whether nodes are present ...
                // sourceNode
                if (!isSourceNodePresent(tempStr)) {
                    sourceNodeId = lastCreatedTempNodeId;
                    sourceNode = lastCreatedTempNode;
                } else {
                    sourceNodeId = tempStr.split("\\(|\\)")[1];
                    sourceNode = sourceNodeId + ", true, false";
                }
                // targetNode
                if (!isTargetNodePresent(tempStr)) {
                    int id = 2 + tempNodeCounter;
                    targetNodeId = "x" + id;
                    tempNodeCounter++;

                    targetNode = targetNodeId + ", false, false";
                } else {
                    // we've found a targetNode, however this is only the case if it is final (i.e. the end of the query)
                    targetNodeId = tempStr.split("-[>]?\\(|\\)")[1];
                    targetNode = targetNodeId + ", false, true";
                }

                lastCreatedTempNode = targetNode;
                lastCreatedTempNodeId = targetNodeId;

                if (tempStr.contains("<")) {
                    // negative edge found
                    label = "-" + tempStr.split("p|]")[1];
                    // System.out.println("negative edge with label " + label);
                } else if (tempStr.contains(">")) {
                    // positive edge found
                    label = tempStr.split("p|]")[1];
                    // System.out.println("positive edge with label " + label);
                } else {
                    // error (or undirected edge found)
                    label = null;
                    // System.out.println("Error processing string " + tempStr);
                }

                stringBuilder = sourceNodeId + ", " + targetNodeId + ", " + label;
                // add the built string that represents an edge to the edgeList
                edgeList.add(stringBuilder);

                // add the sourceNode if it is not present already
                if (!nodeList.contains(sourceNode)) {
                    // System.out.println(sourceNode);
                    nodeList.add(sourceNode);
                } else {
                    // System.out.println("found source node");
                }

                // add the targetNode if it is not present already
                if (!nodeList.contains(targetNode)) {
                    // System.out.println(targetNode);
                    nodeList.add(targetNode);
                } else {
                    // System.out.println("found target node");
                }
            }
            writeParsedData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param tempStr
     * @return true if the tempStr contains a sourceNode
     */
    private boolean isSourceNodePresent(String tempStr) {
        String[] a = tempStr.split("\\[");
        return a[0].contains("(");

    }

    /**
     * @param tempStr
     * @return true if the tempStr contains a targetNode
     */
    private boolean isTargetNodePresent(String tempStr) {
        String[] a = tempStr.split("\\[");
        return a[1].contains("(");
    }

    private void writeParsedData() throws Exception {

        System.out.println("writing query data to file ...");
        String outputFilename = filename.split("\\.")[0];
        File output = new File("src/main/resources/output/" + outputFilename + ".txt");
        FileWriter out;

        try {
            out = new FileWriter(output, false);

            out.write("name: " + filename + "\n");
            out.write("queryGraph: \n");
            out.write("nodes: \n");

            Iterator it = nodeList.iterator();
            while (it.hasNext()) {
                out.write(it.next() + "\n");
            }

            out.write("edges: \n");
            Iterator it2 = edgeList.iterator();
            while (it2.hasNext()) {
                out.write(it2.next() + "\n");
            }

            out.close();
            System.out.println(" ... done!");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
