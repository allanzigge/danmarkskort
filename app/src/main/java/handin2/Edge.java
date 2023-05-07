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

    public Edge(Long fromID, Long toID, Highway road, Boolean isDrivable, Boolean isBikeable, double cost, ArrayList<Node> path) {
        super(path);
        this.fromID = fromID;
        this.toID = toID;
        this.road = road;
        this.isDrivable = isDrivable;
        this.isBikeable = isBikeable;
        this.cost = cost;
    }

   
    
}
