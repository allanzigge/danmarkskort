package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Collections;

public class Pathfinding implements Serializable {
    ArrayList<PathNode> open;
    HashSet<PathNode> closed;
    ArrayList<PathNode> path;
    Graph graph;

    double estimatedGoalCost;
    double costToNode;
    Double finalCost;

    public Pathfinding() {
        open = new ArrayList<>();
        closed = new HashSet<>(); // this is all the nodes wich we have searched neighbor paths
        path = new ArrayList<>(); // not nessesary?
    }

    // Helper class
    private class PathNode implements Serializable, Comparable<PathNode> {
        Node originalNode;
        PathNode previousNode;
        double costToNode;
        double finalCost;
        ArrayList<Node> neighbors;

        public PathNode(Node node, ArrayList<Node> neighbor) {
            originalNode = node;
            this.neighbors = neighbor;
        }

        public PathNode(Node node) {
            originalNode = node;
            neighbors = new ArrayList<>();
        }

        public ArrayList<Node> getNeighbors() {
            return this.neighbors;
        }

        public PathNode getPrevious() {
            return this.previousNode;
        }

        public void setPrevious(PathNode node) {
            this.previousNode = node;
        }

        public double getFinalCost() {
            return this.finalCost;
        }

        public double getLat() {
            return originalNode.getLat();
        }

        public double getLon() {
            return originalNode.getLon();
        }

        @Override
        public int compareTo(PathNode that) {
            return Double.compare(this.finalCost, that.finalCost);
        }

    }
    // End of helper class

    private void findPath(PathNode s, PathNode e) {
        // open.clear(); DELETE OPENLIST WHEN DONE
        closed.clear();
        path.clear();
        PathNode current = s;// new PathNode(s); //First vertex is start
        PathNode goal = e;// new PathNode(e); //Goal vertex
        PathNode neighbor; // Temp vertex

        while (current != goal) {
            for (Node node : current.getNeighbors()) { // Check all currents neighbors
                neighbor = new PathNode(node);

                // LÆG PRI FOR CURRENT TIL COST TO NODE
                costToNode = calLengt(current, neighbor);
                finalCost = costToNode + calLengt(neighbor, goal);

                if (open.contains(neighbor)) { // Add to open || Update open
                    if (finalCost < neighbor.getFinalCost())
                        neighbor.costToNode = costToNode; // Update costToNode
                    neighbor.setPrevious(current); // Update previous
                } else {
                    neighbor.setPrevious(current);
                    open.add(neighbor);
                }
            }
            Collections.sort(open);
            current = open.get(0);
        }
        closed.add(current);
        Collections.sort(open);
        ;
        current = open.get(0);
        System.out.println(current.toString());
    }

    private double calLengt(PathNode from, PathNode to) {
        double dLat = from.getLat() - to.getLat();
        double dLon = from.getLon() - to.getLon();
        return Math.sqrt(Math.pow(dLat, 2) + Math.pow(dLon, 2));
    }

    public void findList() {
        ArrayList<PathNode> verticies = new ArrayList<>();

        Node n1 = new Node(1, 1, 0);
        Node n2 = new Node(1, 2, 1);
        Node n3 = new Node(1, 3, 2);
        Node n4 = new Node(1, 4, 3);
        Node n5 = new Node(1, 5, 4);
        Node n6 = new Node(2, 1, 5);
        Node n7 = new Node(3, 2, 6);
        Node n8 = new Node(3, 3, 7);
        Node n9 = new Node(4, 4, 8);
        Node n10 = new Node(3, 5, 9);

        PathNode pn1 = new PathNode(n1, (ArrayList<Node>) Arrays.asList(n2, n6));
        PathNode pn2 = new PathNode(n2, (ArrayList<Node>) Arrays.asList(n3));
        PathNode pn3 = new PathNode(n3, (ArrayList<Node>) Arrays.asList(n4));
        PathNode pn4 = new PathNode(n4, (ArrayList<Node>) Arrays.asList(n5));
        PathNode pn5 = new PathNode(n5, (ArrayList<Node>) Arrays.asList(n10));
        PathNode pn6 = new PathNode(n6, (ArrayList<Node>) Arrays.asList(n7));
        PathNode pn7 = new PathNode(n7, (ArrayList<Node>) Arrays.asList(n8));
        PathNode pn8 = new PathNode(n8, (ArrayList<Node>) Arrays.asList(n9));
        PathNode pn9 = new PathNode(n9, (ArrayList<Node>) Arrays.asList(n10));
        PathNode pn10 = new PathNode(n10);

        findPath(pn1, pn10);

    }
}