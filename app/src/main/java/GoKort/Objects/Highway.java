package GoKort.Objects;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

import GoKort.GUI.Colorscheme;

public class Highway extends Way {
    private static final long serialVersionUID = 6151634051727665976L;
    String roadType;
    boolean isOneWay;
    boolean bikeable;
    boolean driveable;
    String roadName = "Unnamed Road";
    int maxSpeed;
    String wayId;

    private float thickness;

    public Highway(ArrayList<Node> way, String maxSpeed, String roadName,
            String roadType, boolean isOneWay, String wayId) {
        super(way);
        this.isOneWay = isOneWay;
        this.wayId = wayId;
        this.roadType = roadType;
        if(roadName != null) {

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

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    //sets the Highway object's fields needed for pathfinding depending on the OSM tags
    private void setProperty() {
        if (roadType.equals("motorway") || roadType.equals("motorway_link")) {
            this.maxSpeed = 110;
            this.setThickness(4);
            this.driveable = true;
            this.bikeable = false;
        } else if (roadType.equals("trunk") || roadType.equals("trunk_link")) {
            this.maxSpeed = 90;
            this.setThickness(4);
            this.driveable = true;
            this.bikeable = false;
        }

        else if (roadType.equals("primary") || roadType.equals("primary_link")) {
            this.maxSpeed = 80;
            this.setThickness(3f);
            this.driveable = true;

        } else if (roadType.equals("secondary") || roadType.equals("secondary_link")) {
            this.maxSpeed = 80;
            this.setThickness(2.5f);
            this.driveable = true;
        } else if (roadType.equals("tertiary") || roadType.equals("tertiary_link")) {
            this.maxSpeed = 80;
            this.setThickness(2f);
            this.driveable = true;
        } else if (roadType.equals("unclassified")) {
            this.maxSpeed = 50;
            this.setThickness(1f);
            this.driveable = true;
        } else if (roadType.equals("residential")) {
            this.maxSpeed = 50;
            this.setThickness(1f);
            this.driveable = true;
        } else {
            this.maxSpeed = 50;
            this.setThickness(1f);
        }
    }

    //Sets the line width settings depending on the determinant
    //and draws the highway path with stroke() instead of just making the path
    //Used in the View redraw() method
    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        if (determinant < 2.e9 && determinant > 2.e6) {
            gc.setLineWidth(getThickness() / Math.sqrt(determinant));
        } else if (determinant > 2.e9)
            gc.setLineWidth(getThickness() / Math.sqrt(2.e9));
        else {
            gc.setLineWidth(getThickness() / Math.sqrt(2.e6));
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