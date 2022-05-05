package Parser.Transducer;

import java.util.ArrayList;

public class TransducerParser {

    private ArrayList nodeList;
    private ArrayList edgeList;

    public TransducerParser() {
        nodeList = new ArrayList();
        edgeList = new ArrayList();
    }

    /**
     * handcrafted edit distance transducer for the gMark data.
     *
     */
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

        // negative edges, where the transducer doesnt change the label, are missing.
        edgeList.add("t0, t0, -0, -0, 0");
        edgeList.add("t0, t0, -1, -1, 0");
        edgeList.add("t0, t0, -2, -2, 0");
        edgeList.add("t0, t0, -3, -3, 0");

        // edges that handle the recursion
        // Note: they need to contain some kind of weight, else the Dijkstra won't terminate.
        edgeList.add("t0, t0, 0_rec_, 0, 0");
        edgeList.add("t0, t0, 1_rec_, 1, 0");
        edgeList.add("t0, t0, 2_rec_, 2, 0");
        edgeList.add("t0, t0, 3_rec_, 3, 0");
        // and their negative counterparts...
        edgeList.add("t0, t0, -0_rec_, -0, 0");
        edgeList.add("t0, t0, -1_rec_, -1, 0");
        edgeList.add("t0, t0, -2_rec_, -2, 0");
        edgeList.add("t0, t0, -3_rec_, -3, 0");

    }

    public void emptyTransducer() {
        nodeList = new ArrayList();
        edgeList = new ArrayList();
    }

    public ArrayList getNodeList() {
        return nodeList;
    }

    public ArrayList getEdgeList() {
        return edgeList;
    }
}
