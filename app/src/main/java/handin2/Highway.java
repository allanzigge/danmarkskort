package handin2;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class Highway extends Way {
    private static final long serialVersionUID = 6151634051727665976L;
    String roadType;
    boolean isOneWay;
    boolean bikeable;
    boolean driveable;
    String roadName = "Unnamed Road";
    int maxSpeed;
    String wayId;

    float thickness;

    public Highway(ArrayList<Node> way, String maxSpeed, String roadName,
            String roadType, boolean isOneWay, String wayId) {
        super(way);
        this.isOneWay = isOneWay;
        this.wayId = wayId;
        this.roadType = roadType;
        if(roadName != null){
            this.roadName = roadName;
        }
        this.bikeable = true;
        this.driveable = false;
        setProperty();
        try {
            if (maxSpeed != null)
                this.maxSpeed = Integer.parseInt(maxSpeed);
        } catch (Exception e) {
            System.out
                    .println("could not parse " + maxSpeed + " to an interger. Therefore max speed is set to default");
        }

    }

    private void setProperty() {
        if (roadType.equals("motorway") || roadType.equals("motorway_link")) {
            this.maxSpeed = 110;
            this.thickness = 4;
            this.driveable = true;
            this.bikeable = false;
        } else if (roadType.equals("trunk") || roadType.equals("trunk_link")) {
            this.maxSpeed = 90;
            this.thickness = 4;
            this.driveable = true;
            this.bikeable = false;
        }

        else if (roadType.equals("primary") || roadType.equals("primary_link")) {
            this.maxSpeed = 80;
            this.thickness = 3f;
            this.driveable = true;

        } else if (roadType.equals("secondary") || roadType.equals("secondary_link")) {
            this.maxSpeed = 80;
            this.thickness = 2.5f;
            this.driveable = true;
        } else if (roadType.equals("tertiary") || roadType.equals("tertiary_link")) {
            this.maxSpeed = 80;
            this.thickness = 2f;
            this.driveable = true;
        } else if (roadType.equals("unclassified")) {
            this.maxSpeed = 50;
            this.thickness = 1f;
            this.driveable = true;
        } else if (roadType.equals("residential")) {
            this.maxSpeed = 50;
            this.thickness = 1f;
            this.driveable = true;
        } else {
            this.maxSpeed = 50;
            this.thickness = 1f;
        }
    }

    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        if (determinant < 2.e9 && determinant > 2.e6) {
            gc.setLineWidth(thickness / Math.sqrt(determinant));
        } else if (determinant > 2.e9)
            gc.setLineWidth(thickness / Math.sqrt(2.e9));
        else {
            gc.setLineWidth(thickness / Math.sqrt(2.e6));
        }
        gc.setStroke(colors.getHighway(roadType));
        super.draw(gc, colors, determinant);
        gc.stroke();
    }

    public String getName(){
        return roadName;
    }

    public int getSpeed(){
        return maxSpeed;
    }

    public String getWayId() {
        return wayId;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public boolean isDriveable() {
        return driveable;
    }

    public boolean isBikeable() {
        return bikeable;
    }

    @Override
    public String toString() {
        if (roadName != null) {
            return roadName;
        } else {
            return "Unnamed road or path";
        }

    }
}