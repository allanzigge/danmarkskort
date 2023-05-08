package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Relation implements Serializable {
    private static final long serialVersionUID = 7980625162624571129L;
    ArrayList<List<Node>> outers;
    ArrayList<List<Node>> inners;
    String v;
    ArrayList<Node> relationTrace;

    public Relation(ArrayList<List<Node>> outerList, ArrayList<List<Node>> innerList, String v) {
        this.outers = outerList;
        this.inners = innerList;

        this.v = v;

        this.relationTrace = createWayOuter(outerList);
        if (relationTrace.size() != 0) {
            relationTrace.addAll(createWayInner(innerList, relationTrace.get(relationTrace.size() - 1)));
        }
    }

    // Creates one joined Way out of the list of list<Node> outerlist
    public ArrayList<Node> createWayOuter(ArrayList<List<Node>> outerList) {
        ArrayList<Node> listOfNodes = new ArrayList<>();

        listOfNodes.addAll(outerList.get(0));
        outerList.remove(0);

        Node firstNode = listOfNodes.get(0);
        Node lastNode = listOfNodes.get(listOfNodes.size() - 1);

        // checks if it is an island, if so then makes a reursive call and adds a node
        // to trace back to the island
        if (firstNode.isNodeEqual(lastNode) && !outerList.isEmpty()) {
            listOfNodes.addAll(createWayOuter(outerList));
            listOfNodes.add(lastNode);
        }

        while (!outerList.isEmpty()) {
            int size = outerList.size();
            int counter = 0;

            // runs through every list and checks if it mathces the trace in different ways
            for (List<Node> nodeList : outerList) {
                Node currentFirst = nodeList.get(0);
                Node currentLast = nodeList.get(nodeList.size() - 1);

                // if the way closes itself like a small island. Adds goes back to the trace
                if (currentFirst.isNodeEqual(currentLast)) {
                    listOfNodes.addAll(nodeList);
                    listOfNodes.add(lastNode);
                    outerList.remove(nodeList);
                    break;
                }

                // the the first node mathces the last node in our trace
                if (currentFirst.isNodeEqual(lastNode)) {
                    listOfNodes.addAll(nodeList);
                    outerList.remove(nodeList);
                    break;

                }
                // the last node matches the trace end point, then it reverses the list.
                else if (currentLast.isNodeEqual(lastNode)) {
                    for (Node node : rev(nodeList)) {
                        listOfNodes.add(node);
                    }
                    outerList.remove(nodeList);
                    break;
                }

                // the first node in the list matches the first node in the trace, then it
                // reverse the list,
                // and sets it as the new start point in the trace
                else if (currentFirst.isNodeEqual(firstNode)) {
                    var temp1 = listOfNodes;
                    listOfNodes = rev(nodeList);
                    listOfNodes.addAll(temp1);
                    outerList.remove(nodeList);
                    break;
                }
                counter++;
            }

            // updates the knew start and end point in the trace.
            firstNode = listOfNodes.get(0);
            lastNode = listOfNodes.get(listOfNodes.size() - 1);

            // checks if the trace closes itself like an island, makes a recursive call, and
            // adds a node to trace back to.
            if (firstNode.isNodeEqual(lastNode) && !outerList.isEmpty()) {
                listOfNodes.addAll(createWayOuter(outerList));
                listOfNodes.add(lastNode);
            }

            // checks if it went through the whole list without adding to the trace. Should
            // only happen if something went wrong
            if (size == counter) {
                // listOfNodes.addAll(createWayOuter(outerList));
                System.out.println(v);
                System.out.println(lastNode);
                listOfNodes.clear();
                outerList.clear();
            }
        }
        return listOfNodes;
    }

    // saves the last node in outerway, and adds it to every inner realtion added to
    // way, so every inner relation draws a "line-to" to the last node every time
    public ArrayList<Node> createWayInner(ArrayList<List<Node>> innerList, Node lastOuter) {
        ArrayList<Node> listOfNodes = new ArrayList<>();
        Node lastOuterNode = lastOuter;

        while (!innerList.isEmpty()) {

            for (List<Node> nodeList : innerList) {
                for (Node node : nodeList) {
                    listOfNodes.add(node);
                }
                innerList.remove(nodeList);
                listOfNodes.add(lastOuterNode);
                break;
            }
        }
        return listOfNodes;
    }

    public ArrayList<Node> getRelationTrace() {
        return relationTrace;
    }

    // Reverses the list of nodes
    public ArrayList<Node> rev(List<Node> ndlst) {
        ArrayList<Node> ndlst2 = new ArrayList<Node>();
        for (int i = 1; i <= ndlst.size(); i++) {
            ndlst2.add(ndlst.get(ndlst.size() - i));
        }
        return ndlst2;
    }
}
