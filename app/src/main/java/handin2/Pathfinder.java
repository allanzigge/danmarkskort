package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Pathfinder implements Serializable {
    MinPQ<PathNode> open;
    HashSet<Vertex> closed;
    ArrayList<PathNode> path;
    RTree rtree;
    Map<Vertex, PathNode> map;
    HashMap<Long, Vertex> vertexMap;

    double estimatedGoalCost;
    double costToNode;
    Double finalCost;

    public Pathfinder(HashMap<Long, Vertex> vertexMap) {
        this.vertexMap = vertexMap;
        closed = new HashSet<>();
    }

    public ArrayList<Edge> findPathCar(Vertex from, Vertex to) {
        open = new MinPQ<>();
        HashMap<Vertex, PathNode> map = new HashMap<>();
        closed.clear();
        PathNode start = new PathNode(from, null, 0.0, distCalc(from, to));
        open.insert(start);
        map.put(from,start);

        while (open.size() > 0) {
            PathNode next = open.delMin();
            closed.add(next.originalNode);

            if (next.originalNode.nodeID == to.nodeID) {
                ArrayList<Node> path = new ArrayList<>();
                ArrayList<Edge> edges = new ArrayList<>();
                PathNode current = next;
                while (current != null) {
                    path.add(0, current.originalNode);
                    if (current.edgeTo != null) {
                        edges.add(0, current.edgeTo);
                    }
                    current = current.previousNode;
                }
                return edges;
            } else {
                for (Edge edge : next.originalNode.neigbors) {
                    if ((!closed.contains(vertexMap.get(edge.toID))) && edge.isDrivable) {
                        PathNode nextNode; 
                        if(map.keySet().contains(vertexMap.get(edge.toID))) {
                            nextNode = map.get(vertexMap.get(edge.toID));
                        } else {
                            nextNode = new PathNode(vertexMap.get(edge.toID));
                        }
                        double newCost = next.costToNode + (edge.cost/edge.road.maxSpeed);
                        map.put(vertexMap.get(edge.toID), nextNode);

                        if (newCost < nextNode.costToNode) {
                            nextNode.edgeTo = edge;
                            nextNode.setPrevious(next);
                            nextNode.setCostToNode(newCost);
                            nextNode.setFinalCost(newCost + (distCalc(nextNode, to)/70.0));
                            open.insert(nextNode);

                        }
                    } 
                }

            }
        }
        throw new IllegalStateException("No route found");

    }

    public ArrayList<Edge> findPathBike(Vertex from, Vertex to) {
        open = new MinPQ<>();
        HashMap<Vertex, PathNode> map = new HashMap<>();
        closed.clear();
        PathNode start = new PathNode(from, null, 0.0, distCalc(from, to));
        open.insert(start);
        map.put(from,start);

        while (open.size() > 0) {
            PathNode next = open.delMin();
            closed.add(next.originalNode);

            if (next.originalNode.nodeID == to.nodeID) {
                ArrayList<Node> path = new ArrayList<>();
                ArrayList<Edge> edges = new ArrayList<>();
                PathNode current = next;
                while (current != null) {
                    path.add(0, current.originalNode);
                    if (current.edgeTo != null) {
                        edges.add(0, current.edgeTo);
                    }
                    current = current.previousNode;
                }
                return edges;
            } else {
                for (Edge edge : next.originalNode.neigbors) {
                    if ((!closed.contains(vertexMap.get(edge.toID))) && edge.isBikeable) {
                        PathNode nextNode;
                        if(map.keySet().contains(vertexMap.get(edge.toID))) {
                            nextNode = map.get(vertexMap.get(edge.toID));
                        } else {
                            nextNode = new PathNode(vertexMap.get(edge.toID));
                        }
                        double newCost = (next.costToNode + (edge.cost));

                        if (newCost < nextNode.costToNode) {
                            nextNode.edgeTo = edge;
                            nextNode.setPrevious(next);
                            nextNode.setCostToNode(newCost);
                            nextNode.setFinalCost(newCost + (distCalc(nextNode, to)));
                            open.insert(nextNode);

                        }
                    } 
                }

            }
        }
        throw new IllegalStateException("No route found");

    }

    public double distCalc(Node from, Node to) {
        double dist = (Math.pow(Math.abs(from.getLat() - (to.getLat())), 2.0)
                + Math.pow(Math.abs((from.lon - to.lon) * 0.56), 2.0));
        return Math.sqrt(dist);
    }

    public double distCalc(PathNode from, PathNode to) {
        double dist = (Math.pow(Math.abs(from.originalNode.lat - (to.originalNode.lat)), 2.0)
                + Math.pow(Math.abs((from.originalNode.lon - to.originalNode.lon) * 0.56), 2.0));
        return Math.sqrt(dist);
    }

    public double distCalc(PathNode from, Node to) {
        double dist = (Math.pow(Math.abs(from.originalNode.lat - (to.lat)), 2.0)
                + Math.pow(Math.abs((from.originalNode.lon - to.lon) * 0.56), 2.0));
        return Math.sqrt(dist);
    }

    // Helper class
    private class PathNode implements Serializable, Comparable<PathNode> {
        Vertex originalNode;
        PathNode previousNode;
        Edge edgeTo;
        double costToNode;
        double finalCost;

        public PathNode(Vertex node) {
            this.originalNode = node;
            this.previousNode = null;
            this.costToNode = Double.MAX_VALUE;
            this.finalCost = Double.MAX_VALUE;
        }

        public PathNode(Vertex node, PathNode previousNode, double costToNode, double finalCost) {
            this.originalNode = node;
            this.previousNode = previousNode;
            this.costToNode = costToNode;
            this.finalCost = finalCost;
        }

        public PathNode getPrevious() {
            return previousNode;
        }

        public void setPrevious(PathNode node) {
            this.previousNode = node;
        }

        // public double getFinalCost(){
        // return this.finalCost;
        // }

        public void setCostToNode(double cost) {
            this.costToNode = cost;
        }

        public void setFinalCost(double cost) {
            this.finalCost = cost;
        }

        // public double getLat(){
        // return originalNode.getLat();
        // }

        // public double getLon(){
        // return originalNode.getLon();
        // }

        @Override
        public int compareTo(PathNode that) {
            if (finalCost > that.finalCost) {
                return 1;
            } else if (finalCost < that.finalCost) {
                return -1;
            } else {
                return 0;
            }
        }

    }
    // End of helper class

    // private void findPath(PathNode s, PathNode e){
    // PathNode start = new PathNode(from, )

    // // //open.clear(); DELETE OPENLIST WHEN DONE
    // // closed.clear();
    // // path.clear();
    // // PathNode current = s;//new PathNode(s); //First vertex is start
    // // PathNode goal = e;//new PathNode(e); //Goal vertex
    // // PathNode neighbor; //Temp vertex

    // while(current != goal){
    // for(Node node: current.getNeighbors()){ //Check all currents neighbors
    // neighbor = new PathNode(node);

    // //LÆG PRI FOR CURRENT TIL COST TO NODE
    // costToNode = calLengt(current, neighbor);
    // finalCost = costToNode + calLengt(neighbor, goal);

    // if(open.contains(neighbor)){ // Add to open || Update open
    // if(finalCost < neighbor.getFinalCost())
    // neighbor.costToNode = costToNode; //Update costToNode
    // neighbor.setPrevious(current); //Update previous
    // } else {
    // neighbor.setPrevious(current);
    // open.add(neighbor);
    // }
    // }
    // Collections.sort(open);
    // current = open.get(0);
    // }
    // closed.add(current);
    // Collections.sort(open);;
    // current = open.get(0);
    // System.out.println(current.toString());
    // }

    // private double calLengt(PathNode from, PathNode to) {
    // double dLat = from.getLat()-to.getLat();
    // double dLon = from.getLon()-to.getLon();
    // return Math.sqrt(Math.pow(dLat, 2) + Math.pow(dLon, 2)) ;
    // }

    // public void findList(){
    // ArrayList<PathNode> verticies = new ArrayList<>();

    // Node n1 = new Node(1,1,0);
    // Node n2 = new Node(1,2,1);
    // Node n3 = new Node(1,3,2);
    // Node n4 = new Node(1,4,3);
    // Node n5 = new Node(1,5,4);
    // Node n6 = new Node(2,1,5);
    // Node n7 = new Node(3,2,6);
    // Node n8 = new Node(3,3,7);
    // Node n9 = new Node(4,4,8);
    // Node n10 = new Node(3,5,9);

    // PathNode pn1 = new PathNode(n1, (ArrayList<Node>) Arrays.asList(n2, n6));
    // PathNode pn2 = new PathNode(n2, (ArrayList<Node>) Arrays.asList(n3));
    // PathNode pn3 = new PathNode(n3, (ArrayList<Node>) Arrays.asList(n4));
    // PathNode pn4 = new PathNode(n4, (ArrayList<Node>) Arrays.asList(n5));
    // PathNode pn5 = new PathNode(n5, (ArrayList<Node>) Arrays.asList(n10));
    // PathNode pn6 = new PathNode(n6, (ArrayList<Node>) Arrays.asList(n7));
    // PathNode pn7 = new PathNode(n7, (ArrayList<Node>) Arrays.asList(n8));
    // PathNode pn8 = new PathNode(n8, (ArrayList<Node>) Arrays.asList(n9));
    // PathNode pn9 = new PathNode(n9, (ArrayList<Node>) Arrays.asList(n10));
    // PathNode pn10 = new PathNode(n10);

    // findPath(pn1, pn10);

    // }
}