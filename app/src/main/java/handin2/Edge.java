package handin2;

import java.util.ArrayList;

public class Edge extends Way {
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
   
    
}