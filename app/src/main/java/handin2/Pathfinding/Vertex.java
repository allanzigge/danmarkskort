package handin2.Pathfinding;
import java.util.*;

import handin2.Objects.Node;


public class Vertex extends Node{
    double finalCost;
    ArrayList<Edge> neigbors;

    
    public Vertex(Node node){
        super(node.getLat(), node.getLon(), node.getID());
        neigbors = new ArrayList<>();
    }
        
    public ArrayList<Edge> getNeigbors() {
        return neigbors;
    }


    public void addNeigbor(Edge vertex){

        neigbors.add(vertex);
    }

    @Override
    public String toString() {
        String n = " ";
        for(Edge e : neigbors) {
            n = n + e;
            System.out.print(e);
        }
        System.out.println("test virker");
        return super.toString() + " v "+ n;
    }
    

    
}
