package Parser.Query;

import Parser.Util;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// todo: third bracket handling...

/**
 * note that this is not a complete sparql parser; it only handles the data that gMark outputs.
 */
public class SPARQLParser {
    ArrayList queryNodeList = new ArrayList();
    ArrayList queryEdgeList = new ArrayList();

    /**
     *
     * reads the query and decides how it is handled. there are two kinds of queries that gMark produces:
     * nonrecursive: PREFIX : <http://example.org/gmark/> SELECT DISTINCT ?x0 ?x1 WHERE {  {  ?x0 ((^:p3/:p3/^:p3/:p3)) ?x1 . } }
     * recursive: PREFIX : <http://example.org/gmark/> SELECT DISTINCT ?x0 ?x1 WHERE {  {  ?x0 (((:p1/^:p1/:p1/^:p1))){,3} ?x1 . } }
     */
    public void readQuery(String filePath) throws Exception {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        if (line.contains("(((")) {
            // found a recursive query!
            processRecursiveQuery(filePath);
        } else if (line.contains("((")) {
            // found a non recursive query.
            processNonRecursiveQuery(filePath);
        } else {
            throw new Exception("unexpected query format!");
        }


    }

    /**
     * Beware: This is capable of producing infinite runs of the approximateQueries application.
     * It happens when a recursion is added with cost 0. Then the Dijkstra algorithm won't terminate, since there is always a new edge with cost 0.
     * @param filePath
     * @throws IOException
     */
    private void processRecursiveQuery(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        String[] query = line.split("WHERE");
        String pattern = query[1];

        // add initial node
        queryNodeList.add(Util.nodeBuilder("x0", true, false));
        // add final node
        queryNodeList.add(Util.nodeBuilder("x1", false, true));

        // consists of all label information of the pattern
        String[] patternParts = StringUtils.split(StringUtils.substringBetween(pattern, "?x0 (((", ")))"), "/");

        String currentStartingNodeIndex = "x0";
        String currentEndingNodeIndex = "x1";
        String firstPatternExpression = "";

        int indexCounter = 1;
        int patternSize = patternParts.length;

        if (patternParts.length > 1) {
            // pattern consists of more than one label

            for (String i : patternParts) {
                String label = i.split("p")[1];

                // if edge negated -> adjust
                if (i.contains("^")) {
                    label = Util.negateLabelString(label);
                }

                // store the first label for the recursive edge that gets added at the end.
                if (firstPatternExpression.equals("")) {
                    firstPatternExpression = label;
                }

                // if it's the last element: endingNoded = x1
                if (patternSize == 1) {
                    currentEndingNodeIndex = "x1";

                } else {
                    // endingNode = a new node.
                    indexCounter = ++indexCounter;
                    currentEndingNodeIndex = "x" + indexCounter;

                }

                queryEdgeList.add(Util.classicEdgeBuilder(currentStartingNodeIndex, currentEndingNodeIndex, label));
                currentStartingNodeIndex = currentEndingNodeIndex;
                patternSize--;

                // todo: refactor this into a more readable version...
                // add the sourceNode if it is not present already
                if (!queryNodeList.contains(currentStartingNodeIndex + ", " + "false, false") && !currentStartingNodeIndex.equals("x0") && !currentStartingNodeIndex.equals("x1")) {
                    // System.out.println(sourceNode);
                    queryNodeList.add(currentStartingNodeIndex + ", " + "false, false");
                } else {
                    // System.out.println("found source node");
                }

                // add the targetNode if it is not present already
                if (!queryNodeList.contains(currentEndingNodeIndex + ", " + "false, false") && !currentEndingNodeIndex.equals("x1") && !currentEndingNodeIndex.equals("x0")) {
                    // System.out.println(targetNode);
                    queryNodeList.add(currentEndingNodeIndex + ", " + "false, false");
                } else {
                    // System.out.println("found target node");
                }
            }

            // add recursive edge; here: from the final state to the second state with first label of the pattern.
            queryEdgeList.add(Util.classicEdgeBuilder("x1", "x2", firstPatternExpression + "_rec_"));

        } else {
            // pattern consists of only one label between initial and final node
            // find out the label
            String label = patternParts[0].split("p")[1];
            // if edge negated -> adjust
            if (patternParts[0].contains("^")) {
                label = Util.negateLabelString(label);
            }
            queryEdgeList.add(Util.classicEdgeBuilder(currentStartingNodeIndex, currentEndingNodeIndex, label));

            // add the recursive edge; here: edge from final state to itself.
            queryEdgeList.add(Util.classicEdgeBuilder(currentEndingNodeIndex, currentEndingNodeIndex, label));
        }

    }

    private void processNonRecursiveQuery(String filePath) throws IOException {

        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        String[] query = line.split("WHERE");
        String pattern = query[1];
//        System.out.println("whole query: " + line);
//        System.out.println("pattern:     " + pattern);

        // add initial node
        queryNodeList.add(Util.nodeBuilder("x0", true, false));
        // add final node
        queryNodeList.add(Util.nodeBuilder("x1", false, true));

        // consists of all label information of the pattern
        String[] patternParts = StringUtils.split(StringUtils.substringBetween(pattern, "?x0 ((", ")) ?x1"), "/");

//        System.out.print("pattern parts: ");
//        for (String i : patternParts) {
//            System.out.print(i + ", ");
//        }
//        System.out.println();

        String currentStartingNodeIndex = "x0";
        String currentEndingNodeIndex = "x1";

        int indexCounter = 1;
        int patternSize = patternParts.length;

        if (patternParts.length > 1) {
            // pattern consists of more than one label

            for (String i : patternParts) {
                String label = i.split("p")[1];

                // if edge negated -> adjust
                if (i.contains("^")) {
                    label = Util.negateLabelString(label);
                }

                // if it's the last element: endingNoded = x1
                if (patternSize == 1) {
                    currentEndingNodeIndex = "x1";

                } else {
                    // endingNode = a new node.
                    indexCounter = ++indexCounter;
                    currentEndingNodeIndex = "x" + indexCounter;

                }

                queryEdgeList.add(Util.classicEdgeBuilder(currentStartingNodeIndex, currentEndingNodeIndex, label));
                currentStartingNodeIndex = currentEndingNodeIndex;
                patternSize--;

                // todo: refactor this into a more readable version...
                // add the sourceNode if it is not present already
                if (!queryNodeList.contains(currentStartingNodeIndex + ", " + "false, false") && !currentStartingNodeIndex.equals("x0") && !currentStartingNodeIndex.equals("x1")) {
                    // System.out.println(sourceNode);
                    queryNodeList.add(currentStartingNodeIndex + ", " + "false, false");
                } else {
                    // System.out.println("found source node");
                }

                // add the targetNode if it is not present already
                if (!queryNodeList.contains(currentEndingNodeIndex + ", " + "false, false") && !currentEndingNodeIndex.equals("x1") && !currentEndingNodeIndex.equals("x0")) {
                    // System.out.println(targetNode);
                    queryNodeList.add(currentEndingNodeIndex + ", " + "false, false");
                } else {
                    // System.out.println("found target node");
                }
            }

        } else {
            // pattern consists of only one label between initial and final node
            // find out the label
            String label = patternParts[0].split("p")[1];
            // if edge negated -> adjust
            if (patternParts[0].contains("^")) {
                label = Util.negateLabelString(label);
            }
            queryEdgeList.add(Util.classicEdgeBuilder(currentStartingNodeIndex, currentEndingNodeIndex, label));
        }
    }

    public ArrayList getQueryNodeList() {
        return queryNodeList;
    }

    public ArrayList getQueryEdgeList() {
        return queryEdgeList;
    }
}
