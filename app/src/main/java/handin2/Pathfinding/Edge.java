package handin2.Pathfinding;

import java.util.ArrayList;

import handin2.Objects.Highway;
import handin2.Objects.Node;
import handin2.Objects.Way;

//This class models the "path" between two vertices. An edge in graph so to speak.
public class Edge extends Way {
    private static final long serialVersionUID = 6218782255583747084L;
    Long fromID;
    Long toID;
    Double cost;
    Highway road;
    Way path;
    Boolean isDrivable;
    Boolean isBikeable;
    
    public Edge(Long fromID, Long toID, Highway road, boolean isDriveable, boolean isBikeable, double cost,
            ArrayList<Node> path) {
        super(path);
        this.fromID = fromID;
        this.toID = toID;
        this.road = road;
        this.isDrivable = isDriveable;
        this.isBikeable = isBikeable;
        this.cost = cost;
    }

    public String getName(){
        return road.getName();
    }

    public double getCost(){
        return cost;
    }

    public Long getFromID(){
        return fromID;
    }
   
    public Long getToID(){
        return toID;
    }

    public Highway getRoad(){
        return road;
    }
   
    
}