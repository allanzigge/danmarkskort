package handin2;
import java.util.*;

public class Vertex extends Node{
    double finalCost;
    // Node node;
    ArrayList<Edge> neigbors;

    
    public Vertex(Node node){
        super(node.lat, node.lon, node.nodeID);
        // this.node = node;
        neigbors = new ArrayList<>();
    }
        
    public ArrayList<Edge> getNeigbors() {
        return neigbors;
    }

    public Long getVertexID() {
        return nodeID;
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
