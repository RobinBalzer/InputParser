package Parser;

import java.util.ArrayList;

public class Util {
    public static String nodeBuilder(String nodeName, boolean initialState, boolean finalState) {
        return (nodeName + ", " + initialState + ", " + finalState);
    }

    public static String classicEdgeBuilder(String sourceNodeName, String targetNodeName, String label) {
        return (sourceNodeName + ", " + targetNodeName + ", " + label);
    }

    public static String transducedEdgeBuilder(String sourceNodeName, String targetNodeName, String incomingTransducerLabel, String outgoingTransducerLabel, int cost) {
        return (sourceNodeName + ", " + targetNodeName + ", " + incomingTransducerLabel + ", " + outgoingTransducerLabel + ", " + cost);
    }

    public static void printList (ArrayList list) {
        for (Object i : list){
            System.out.println(i);
        }
    }
    public static String negateLabelString(String string) {
        return "-" + string;
    }
}
