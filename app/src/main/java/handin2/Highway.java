package handin2;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class Highway extends Way {
    private static final long serialVersionUID = 6151634051727665976L;
    String roadType;
    boolean isOneWay;
    boolean isBikeable;
    boolean isDriveable;
    String roadName;
    int maxSpeed;
    String wayId;

    float thickness;

    public Highway(ArrayList<Node> way, boolean isBikeable, Boolean isDriveable, String maxSpeed, String roadName,
            String roadType, boolean isOneWay, String wayId) {
        super(way);
        this.isOneWay = isOneWay;
        this.wayId = wayId;
        this.roadType = roadType;
        this.roadName = roadName;
        this.isBikeable = true;
        this.isDriveable = false;
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
            this.isDriveable = true;
            this.isBikeable = false;
        }else if (roadType.equals("trunk") || roadType.equals("trunk_link")) {
            this.maxSpeed = 90;
            this.thickness = 4;
            this.isDriveable = true;
            this.isBikeable = false;
        }

        else if (roadType.equals("primary") || roadType.equals("primary_link")) {
            this.maxSpeed = 80;
            this.thickness = 3f;
            this.isDriveable = true;

        }
        else if (roadType.equals("secondary") || roadType.equals("secondary_link")) {
            this.maxSpeed = 80;
            this.thickness = 2.5f;
            this.isDriveable = true;
        }
        else if (roadType.equals("tertiary") || roadType.equals("tertiary_link"))  {
            this.maxSpeed = 80;
            this.thickness = 2f;
            this.isDriveable = true;
        }
        else if (roadType.equals("unclassified")) {
            this.maxSpeed = 50;
            this.thickness = 1f;
            this.isDriveable = true;
        }
        else if (roadType.equals("residential")) {
            this.maxSpeed = 50;
            this.thickness = 1f;
            this.isDriveable = true;
        }
        else {
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



    public String getWayId() {
        return wayId;
    }

    public boolean isOneWay() {
        return isOneWay;
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