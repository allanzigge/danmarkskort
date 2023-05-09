package handin2.Pathfinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import handin2.DataStructures.MinPQ;
import handin2.DataStructures.RTree;
import handin2.Objects.Node;

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
    private String travelTime; 
    private String travelLength;
    double travelSpeed;
    String travelType;                                       //String for cal. traveltime for bike/walk
    // This is our pathfinding implementation, based on the A* [A star] algorithm \\
    
    public Pathfinder(HashMap<Long, Vertex> vertexMap) {
        this.vertexMap = vertexMap; 
        closed = new HashSet<>();   
        guide = new ArrayList<>();
        travelType = "default"; 
    }

    public String getTravelLength() {
        return travelLength;
    }

    public void setTravelLength(String travelLength) {
        this.travelLength = travelLength;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    //method for finding path by car - difference is the expected speed 
    public ArrayList<Edge> findPathCar(Vertex from, Vertex to) {
        open = new MinPQ<>();                               //PQ for storing and sorting neighbor-nodes
        HashMap<Vertex, PathNode> map = new HashMap<>();    //Map for making link between Vertex and Pathnode
        closed.clear();
        PathNode start = new PathNode(from, null, 0.0, distCalc(from, to));
        open.insert(start);                                 //Start the pathfinding by inserting the first node in the PQ
        map.put(from, start);

        while (open.size() > 0) {                           //Notice we explore all options, however might not be as fast as first path
            PathNode next = open.delMin();                  //Explode the neighbors for the cheapest option so far
            closed.add(next.originalNode);

            if (next.originalNode.getID() == to.getID()) {    //If this is the goal:
                ArrayList<Node> path = new ArrayList<>();
                ArrayList<Edge> edges = new ArrayList<>();
                PathNode current = next;
                while (current != null) {                   //Lets backtrack the path
                    path.add(0, current.originalNode);      
                    if (current.edgeTo != null) {           //Add edges untill we reach the end of the actual road
                        edges.add(0, current.edgeTo);
                    }
                    current = current.previousNode;         //Get the previous node
                }
                createTextRoute(edges);                     //Text description
                return edges;
            } else {                                        //If not goal:
                for (Edge edge : next.originalNode.neigbors) { //Explore each nieghbor(option) from the current node
                    if (edge.isDrivable) { 
                        if ((!closed.contains(vertexMap.get(edge.toID)))) {     //Only necessary to explore un-explored nodes
                            PathNode nextNode;
                            if (map.keySet().contains(vertexMap.get(edge.toID))) {
                                nextNode = map.get(vertexMap.get(edge.toID));   //If we know a way to this node, get the node
                            } else {
                                nextNode = new PathNode(vertexMap.get(edge.toID)); // Else create a new one
                            }
                                                            //Calculate the cost to get from the current node to the next
                            double newCost = next.costToNode + (edge.cost / edge.road.getSpeed()); //Notice the speed limit affects the cost
                            map.put(vertexMap.get(edge.toID), nextNode);    //Save this node

                            if (newCost < nextNode.costToNode) {    //IF the calculated cost is cheaper than the already known path
                                nextNode.edgeTo = edge;             // - then update the new path and costs 
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
    //method for finding path by bike - difference is the expected speed 
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

            if (next.originalNode.getID() == to.getID()) {
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
                        double newCost = (next.costToNode + (edge.cost)); //Notice here no speed limit affects the cost 

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
        ArrayList<String> tempList = new ArrayList<>();
        double tempLength = 0;                              //Used for counting length of each road
        double totalLength = 0;
        float timeEstimate = 0;                             //Time in seconds
        String retning = new String();

        //a, b as we know from 2d lines: y=ax+b  
        //c,d are helping variables
        double a, b, c, d = 0; 
        Edge thisRoad;
        Edge nextRoad;

        uniqueRoads.add(edges.get(0));                      //First add the starting edge to the list of unq. names

        for(int i = 0; i < edges.size()-1;i++){             //When turning from thisRoad to nextRoad
            thisRoad = edges.get(i);                        //Peeking to the next road 
            totalLength += thisRoad.getCost();              //Calculation total length
                                                            //Calculation of this edge's cost in seconds
            
            if(!travelType.equals("bike") && !travelType.equals("walk"))
                travelSpeed = thisRoad.getRoad().getSpeed()/3.6;// In m/s
            timeEstimate += (thisRoad.getCost()*114500)/ (travelSpeed*0.9);

            String thisRoadName = thisRoad.getName();
            String nextRoadName = uniqueRoads.get(uniqueRoads.size()-1).getName();

            if(!(nextRoadName != null))                     //Unfortunately, we have had to add this section to make sure
                nextRoadName = "Unnamed Road";              //no roadNames are nullpointers
            if(!(thisRoadName != null))
                thisRoadName = "Unnamed Road";
            
            if(!nextRoadName.equals(thisRoadName)){         //A change in roadname indicated a turn:
                tempList.clear();
                nextRoad = thisRoad;                        // Save the reference for the road we turn to
                thisRoad = edges.get(i-1);                  // Take the known road as thisRoad 
                float thisRoadFromLat = vertexMap.get(thisRoad.getFromID()).getLat();
                float thisRoadFromLon = vertexMap.get(thisRoad.getFromID()).getLon();
                float thisRoadToLat = vertexMap.get(thisRoad.getToID()).getLat();
                float thisRoadToLon = vertexMap.get(thisRoad.getToID()).getLon();

                float nextRoadToLat = vertexMap.get(nextRoad.getToID()).getLat();
                float nextRoadToLon = vertexMap.get(nextRoad.getToID()).getLon();
              
                //c, d helpers to determine which way the line goes
                c = thisRoadToLat - thisRoadFromLat;        // Delta lat for the current road -> to-from
                d = thisRoadToLon - thisRoadFromLon;        // Delta lon for the current road -> to-from

                a = (d)/(c);                                // Gradient of the virtual line of the current road
                b = (thisRoadToLon) - a * (thisRoadToLat);  // From 2d line: y = ax + b

                if(0 < c && 0 < d){                                 // Line going up and right
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        retning = "højre";
                    } else{
                        retning = "venstre";
                    }   
                } else if(c < 0 && d < 0){                          // Line going down and left
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        retning = "venstre";
                    } else{
                        retning = "højre";
                    }   
                } else if(0 < c && d < 0){                          // Line going down and right
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        retning = "højre";
                    } else{
                        retning = "venstre";
                    }   
                } else if(c < 0 && 0 < d){                          // Line going up and left
                    if( (a * (nextRoadToLat) + b) < nextRoadToLon){
                        retning = "venstre";
                    } else{
                        retning = "højre";
                    }   
                } 

               //Coordinates for the intersection Only needed for debugging
                /* System.out.println(thisRoadFromLat +" "+ thisRoadFromLon);
                System.out.println(thisRoadToLat +" "+ thisRoadToLon);
                System.out.println(nextRoadToLat +" "+ thisRoadToLon + "\n"); */
                
                String rutevejledning = "Fortsaet " +  (int)Math.round(tempLength*111139)+"m af " + nextRoadName + " og drej derefter til " + retning;
                tempList.add(rutevejledning);
                tempList.add(retning);
                
                guide.add(new ArrayList<>(tempList));               //Deep copying the information stored
                tempList.clear();
                uniqueRoads.add(thisRoad);                          // Now adding this road to the list of uniqes
                tempLength = 0;                                     //Reset counter for the next part of the route
            }
            tempLength += thisRoad.getCost();                       
        }

        if(3600 < timeEstimate){                                    //Formating the text description 
            setTravelTime(Float.toString( (float) Math.floor(timeEstimate/3600)) + " t " + Float.toString((float) Math.ceil((timeEstimate%3600)/60)) + " m");
        } else{
            setTravelTime(Float.toString((float) Math.ceil((timeEstimate%3600)/60)) + " m");
        }
       
        
       totalLength = (.5 + totalLength*114500)/1000;                // Format to kilometres 
       totalLength = Math.round(totalLength * 10) / 10;             // One decimal number
       setTravelLength("Længde: " + totalLength + " km"); //Tweaked value a bit to make length estimate a bit more precise
       travelType = "default";
    }

    public ArrayList<ArrayList<String>> getTextRoute(){
        return guide;
    }

    public String getTime(){
        return getTravelTime();
    }

    public void setType(String type){
        if(type != null){
            travelType = type;
            if(type.equals("bike"))
            travelSpeed = 4.1;                              // Avg. speed for bike ~15km/t
        if(type.equals("walk"))
            travelSpeed = 1.3;                              // Avg. speed for walk ~ 5km/t
        }
    }

    public double distCalc(Node from, Node to) {
        double dist = (Math.pow(Math.abs(from.getLat() - (to.getLat())), 2.0)
                + Math.pow(Math.abs((from.getLon() - to.getLon()) * 0.56), 2.0));
        return Math.sqrt(dist);
    }

    public double distCalc(PathNode from, PathNode to) {
        double dist = (Math.pow(Math.abs(from.originalNode.getLat() - (to.originalNode.getLat())), 2.0)
                + Math.pow(Math.abs((from.originalNode.getLon() - to.originalNode.getLon()) * 0.56), 2.0));
        return Math.sqrt(dist);
    }

    public double distCalc(PathNode from, Node to) {
        double dist = (Math.pow(Math.abs(from.originalNode.getLat() - (to.getLat())), 2.0)
                + Math.pow(Math.abs((from.originalNode.getLon() - to.getLon()) * 0.56), 2.0));
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