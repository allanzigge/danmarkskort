package GoKort.Objects;

import java.io.Serializable;

public class Node implements Serializable {
    float lat, lon;
    long nodeID;

    public Node(float lat, float lon, long nodeID) { 
        this.lat = lat;
        this.lon = lon;
        this.nodeID = nodeID;
    }

    public float getLat(){
        return lat;
    }

    public float getLon(){
        return lon;
    }

    public long getID(){
        return nodeID;
    }
    
    public boolean isNodeEqual(Node node) {
        if(this.nodeID == node.getID())
            return true;
        else 
            return false;
    }

    //Used the "afstandformel" to determine the distance between two points in a 2 dimensional space
    public double distToVertex(Node other) {
        double dist = (Math.pow(Math.abs((lat-(other.lat))),2.0) + Math.pow(Math.abs(((lon-other.lon)*0.56)),2.0));
        return Math.sqrt(dist);
    }

    @Override
    public String toString() {
        return Long.toString(nodeID);
    }

}