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
        map.put(from, start);

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
                    if (edge.isDrivable) {
                        if ((!closed.contains(vertexMap.get(edge.toID)))) {
                            PathNode nextNode;
                            if (map.keySet().contains(vertexMap.get(edge.toID))) {
                                nextNode = map.get(vertexMap.get(edge.toID));
                            } else {
                                nextNode = new PathNode(vertexMap.get(edge.toID));
                            }
                            double newCost = next.costToNode + (edge.cost / edge.road.maxSpeed);
                            map.put(vertexMap.get(edge.toID), nextNode);

                            if (newCost < nextNode.costToNode) {
                                nextNode.edgeTo = edge;
                                nextNode.setPrevious(next);
                                nextNode.setCostToNode(newCost);
                                nextNode.setFinalCost(newCost + (distCalc(nextNode, to) / 70.0));
                                open.insert(nextNode);

                            }
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
        map.put(from, start);

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
                        if (map.keySet().contains(vertexMap.get(edge.toID))) {
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

        public void setPrevious(PathNode node) {
            this.previousNode = node;
        }

        public void setCostToNode(double cost) {
            this.costToNode = cost;
        }

        public void setFinalCost(double cost) {
            this.finalCost = cost;
        }

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
}