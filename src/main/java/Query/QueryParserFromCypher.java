package Query;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import Settings.Settings;

@Deprecated // this doesnt work for any kind of recursion!
public class QueryParserFromCypher {
    private final String path;
    public String filename;
    private final ArrayList nodeList;
    private final ArrayList edgeList;
    private int tempNodeCounter;

    public QueryParserFromCypher(String inputFile) {
        this.path = Settings.inputFileDirectory + inputFile;
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

            String[] edges;
            boolean singleEdgeQuery;

            // find out how many edges are in the query
            String tempString = pattern.replaceAll("\\[|\\]", "");
            int size = pattern.length() - tempString.length();
            int amountOfEdges = size / 2;

            // special case where we only have one edge
            if (amountOfEdges == 1) {
                singleEdgeQuery = true;
                edges = new String[1];
                edges[0] = words[1];
                /*
                String[] tempEdges = pattern.split("\\(..\\)");
                // the first element of tempEdges is "", so we have to dump that.
                edges = Arrays.copyOfRange(tempEdges, 1, tempEdges.length);


                 */
            } else {
                // this gives us the initial and final node and its edges for the query
                // and every edge in between arbitrary nodes (all nodes between initial and final node)
                singleEdgeQuery = false;
                edges = pattern.split("\\(\\)");
            }

            String label;
            String sourceNode;
            String sourceNodeId;
            String targetNode;
            String targetNodeId;
            String lastCreatedTempNode = "";
            String lastCreatedTempNodeId = "";

            for (String tempStr : edges) {
                String stringBuilder = "";
                boolean recursiveEdge = tempStr.contains("*");

                // check whether nodes are present ...
                // sourceNode
                if (!isSourceNodePresent(tempStr)) {
                    sourceNodeId = lastCreatedTempNodeId;
                    sourceNode = lastCreatedTempNode;
                } else {
                    // contrary to the targetNode here we do not have to distinguish between single and multi edge queries.

                    // we now check whether the edge is recursive
                    sourceNodeId = tempStr.split("\\(|\\)")[1];
                    if (!recursiveEdge) {
                        sourceNode = sourceNodeId + ", true, false";
                    } else {
                        // recursive query!
                        // assign the states for the source node accordingly

                        if (!singleEdgeQuery) {
                            sourceNode = sourceNodeId + ", true, false";
                        } else {
                            // if we have a single edge query, we simply have one node (initial and final) with a self loop.
                            sourceNode = sourceNodeId + ", true, true";
                        }
                    }
                }
                // targetNode
                if (!isTargetNodePresent(tempStr)) {
                    if (!recursiveEdge) {
                        int id = 2 + tempNodeCounter;
                        targetNodeId = "x" + id;
                        tempNodeCounter++;
                        targetNode = targetNodeId + ", false, false";
                    } else {
                        // recursive edge ...
                        targetNodeId = sourceNodeId;
                        targetNode = sourceNode;
                    }
                } else {
                    if (!recursiveEdge) {
                        // we've found a targetNode, however this is only the case if it is final (i.e. the end of the query)

                        if (!singleEdgeQuery) {
                            targetNodeId = tempStr.split("-[>]?\\(|\\)")[1];
                        } else {
                            // special case: single edge query
                            // e.g. (x0)-[:p1]->(x1) .split("\\(|\\)"); // splitting at every round bracket
                            // [0] = "", [1] = "x0", [2] = "-[:p1]->", [3] = "x1", [4] = "";
                            // to get the targetNodeId we have to take [3]!
                            targetNodeId = tempStr.split("\\(|\\)")[3];
                        }
                        targetNode = targetNodeId + ", false, true";
                    } else {
                        // recursive edge ...
                        targetNodeId = sourceNodeId;
                        targetNode = sourceNode;

                    }
                }
                lastCreatedTempNode = targetNode;
                lastCreatedTempNodeId = targetNodeId;

                if (tempStr.contains("<")) {
                    // negative edge found
                    label = "-" + tempStr.split("p|]")[1];
                    label = label.replace("*", ""); // remove recursive symbol
                    // System.out.println("negative edge with label " + label);
                } else if (tempStr.contains(">")) {
                    // positive edge found
                    label = tempStr.split("p|]")[1];
                    label = label.replace("*", ""); // remove recursive symbol
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

        //System.out.println("writing query data to file ...");
        String outputFilename = filename.split("\\.")[0];
        File output = new File(Settings.outputFileDirectory + outputFilename + ".txt");
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
            //System.out.println(" ... done!");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
