package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Pathfinder implements Serializable {
    private static final long serialVersionUID = 2463027208108695228L;
    MinPQ<PathNode> open;
    HashSet<Vertex> closed;
    ArrayList<PathNode> path;
    RTree rtree;
    Map<Vertex, PathNode> map;
    HashMap<Long, Vertex> vertexMap;

    double estimatedGoalCost;
    double costToNode;
    Double finalCost;
    ArrayList<ArrayList<String>> guide;
    String travelTime; 

    public Pathfinder(HashMap<Long, Vertex> vertexMap) {
        this.vertexMap = vertexMap;
        closed = new HashSet<>();
        guide = new ArrayList<>();
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
                createTextRoute(edges);
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
                createTextRoute(edges);
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

    private void createTextRoute(ArrayList<Edge> edges) {
        guide.clear();
        ArrayList<Edge> uniqueRoads = new ArrayList<>();
        ArrayList<Double> lengths = new ArrayList<>();
        ArrayList<String> tempList = new ArrayList<>();
        double tempLength = 0; //Used for counting length of each road
        double totalLength = 0;
        float timeEstimate = 0; //time in seconds

        //a, b som kendt fra y=ax+b - c,d som hjælpevariabler
        double a, b, c, d = 0; 
        Edge thisRoad;
        Edge nextRoad;

        uniqueRoads.add(edges.get(0));


        for(int i = 0; i < edges.size()-1;i++){ //When turning from thisRoad to nextRoad
            thisRoad = edges.get(i); //Peeking to the next road 
            tempLength += thisRoad.getCost(); //Counter for the total length
            totalLength += thisRoad.getCost();

            timeEstimate += (thisRoad.getCost()*111139)/ (thisRoad.getRoad().getSpeed()/3.6);

            String thisRoadName = thisRoad.getName();
            String nextRoadName = uniqueRoads.get(uniqueRoads.size()-1).getName();
            if(!(nextRoadName != null))
                nextRoadName = "Unnamed Road";
            if(!(thisRoadName != null))
                thisRoadName = "Unnamed Road";
            
            if(!nextRoadName.equals(thisRoadName)){ //If next roadnames ! equal, a shift is indicated
                tempList.clear();
                nextRoad = thisRoad;        // Save the reference for the road we turn to
                thisRoad = edges.get(i-1);  // Take the known road as thisRoad 
                float thisRoadFromLat = vertexMap.get(thisRoad.getFromID()).getLat();
                float thisRoadFromLon = vertexMap.get(thisRoad.getFromID()).getLon();
                float thisRoadToLat = vertexMap.get(thisRoad.getToID()).getLat();
                float thisRoadToLon = vertexMap.get(thisRoad.getToID()).getLon();

                float nextRoadToLat = vertexMap.get(nextRoad.getToID()).getLat();
                float nextRoadToLon = vertexMap.get(nextRoad.getToID()).getLon();
              
                //c, d helpers to determine what way the line goes
                c = thisRoadToLat - thisRoadFromLat; // Delta lat -> to-from
                d = thisRoadToLon - thisRoadFromLon; // Delta lon -> to-from

                a = (d)/(c);
                b = (thisRoadToLon) - a * (thisRoadToLat);

                tempList.add(String.valueOf(tempLength*111139));
                tempList.add(thisRoadName);

                if(0 < c && 0 < d){ 
                    //System.out.println("op, højre");
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        tempList.add("højre");
                    } else{
                        tempList.add("venstre");
                    }   
                } else if(c < 0 && d < 0){
                    //System.out.println("ned, venstre");
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        tempList.add("venstre");
                    } else{
                        tempList.add("højre");
                    }   
                } else if(0 < c && d < 0){
                    //System.out.println("ned, højre");
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        tempList.add("højre");
                    } else{
                        tempList.add("venstre");
                    }   
                } else if(c < 0 && 0 < d){
                    //System.out.println("op, venstre");
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        tempList.add("venstre");
                    } else{
                        tempList.add("højre");
                    }   
                } 

               //Koordinater i kryds
                /* System.out.println(thisRoadFromLat +" "+ thisRoadFromLon);
                System.out.println(thisRoadToLat +" "+ thisRoadToLon);
                System.out.println(nextRoadToLat +" "+ thisRoadToLon + "\n"); */

                uniqueRoads.add(thisRoad);
                
                tempLength = 0; 
                
                guide.add(new ArrayList<>(tempList));
                tempList.clear();
            }
        }

        if(3600 < timeEstimate){
            travelTime = Float.toString( (float) Math.floor(timeEstimate/3600)) + " time(r) " + Float.toString((float) Math.ceil((timeEstimate%3600)/60)) + " minutter";
        } else{
            travelTime = Float.toString((float) Math.ceil((timeEstimate%3600)/60)) + " minutter";
        }
        System.out.println(timeEstimate);
        System.out.println("traveltime: " + travelTime);

        //forsæt [0] m af [1]. Drej derefter til [2]
        for(int i = 0; i < guide.size()-1;i++){
            //System.out.println((i));
            //System.out.println("Fortsaet " +  Math.round(Float.parseFloat(guide.get(i).get(0))) +"m af " +guide.get(i).get(1) + " og drej derefter til " + guide.get(i).get(2));
        }        
        System.out.println("Laengde: " + Math.round(.5 + totalLength*114900));
    }

    public ArrayList<ArrayList<String>> getTextRoute(){
        return guide;
    }

    public String getTime(){
        return travelTime;
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