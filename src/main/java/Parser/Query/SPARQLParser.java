package Parser.Query;

import Parser.Util;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// PREFIX : <http://example.org/gmark/> SELECT DISTINCT ?x0 ?x1 WHERE {  {  ?x0 ((^:p3/:p3/^:p3/:p3)) ?x1 . } }
public class SPARQLParser {
    ArrayList queryNodeList = new ArrayList();
    ArrayList queryEdgeList = new ArrayList();


    public void readQuery(String filePath) throws IOException {

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

        String currentStartingNode = "x0";
        String currentEndingNode = "x1";
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
                    System.out.println("last iteration");
                    currentEndingNode = "x1";

                } else {
                    // endingNode = a new node.
                    indexCounter = ++indexCounter;
                    currentEndingNode = "x" + indexCounter;

                }

                queryEdgeList.add(Util.classicEdgeBuilder(currentStartingNode, currentEndingNode, label));
                currentStartingNode = currentEndingNode;
                patternSize--;
            }

        } else {
            // pattern consists of only one label between initial and final node
            // find out the label
            String label = patternParts[0].split("p")[1];
            // if edge negated -> adjust
            if (patternParts[0].contains("^")) {
                label = Util.negateLabelString(label);
            }
            queryEdgeList.add(Util.classicEdgeBuilder(currentStartingNode, currentEndingNode, label));
        }
    }

    public ArrayList getQueryNodeList() {
        return queryNodeList;
    }

    public ArrayList getQueryEdgeList() {
        return queryEdgeList;
    }
}
