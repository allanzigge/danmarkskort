
package handin2;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;

public class Position {
    double startScale, factor, zoomedAtX, zoomedAtY;
    double totalX, totalY;
    double currentX, currentY;
    double newScale;
    double xPosition, yPosition, latPosition, lonPosition;
    double canvasMaxX, canvasMinX, canvasMaxY, canvasMinY;

    double canvasWidth, canvasHeight;
    double maxLat, minLat, maxLon, minLon;
    double latFactor, lonFactor;

    float[] canvasBoarder;
    double panY, panX, zoomOut;

    Position(float startScale, Canvas canvas, Model model) {
        this.startScale = startScale;
        this.factor = 1.0f;
        this.zoomedAtX = 0;
        this.zoomedAtY = 0;

        this.canvasHeight = canvas.getHeight();
        this.canvasWidth = canvas.getWidth();

        this.maxLat = model.maxlat;
        this.minLat = model.minlat;
        this.minLon = model.minlon;

        // calculates max lon drawn on canvas on start.
        double latLonFactor = 0.56F;

        double latDif = maxLat - minLat;
        double lonDif = maxLon - minLon;

        double lonDifInLat = (maxLon - minLon) * latLonFactor;
        double canvasFactor = canvasWidth / canvasHeight;

        double lonNotOnMap = lonDifInLat - (latDif * canvasFactor);
        double lonDifOnMap = lonDif - (lonNotOnMap / latLonFactor);

        maxLon = minLon + lonDifOnMap;

        // used to tranlate y and x cords to lat and lon cords
        this.latFactor = -(maxLat - minLat) / canvasHeight;
        this.lonFactor = (maxLon - minLon) / canvasWidth;

        this.canvasBoarder = new float[4];
    }

    // updates the canvas when user expands the size of the window
    public void setnewCanvas(Canvas canvas) {
        this.canvasHeight = canvas.getHeight();
        this.canvasWidth = canvas.getWidth();
    }

    // updates the factor when zoomed, which is used to calculate mouse- and canvas
    // position
    public void setNewScale(float newScale) {
        this.newScale = newScale;
        setFactor();
    }

    public void setFactor() {
        this.factor = newScale / startScale;
    }

    // tells the current mouseposition on the canvas
    public void setPosition(double x, double y) {
        this.currentX = x;
        this.currentY = y;
        setMousePosition();
    }

    // updates the total x and y position of the mouse.
    public void setMousePosition() {
        xPosition = (currentX - zoomedAtX) * factor + totalX;
        yPosition = (currentY - zoomedAtY) * factor + totalY;

        LatLonMousePosition();
    }

    // translate the x and y postion of the mouse to lat and lat
    public void LatLonMousePosition() {
        latPosition = maxLat + (latFactor * yPosition);
        lonPosition = minLon + (lonFactor * xPosition);

    }

    // keeps track of how much the canvas has been dragged
    public void dragged(Double x, Double y) {
        totalX -= x * factor;
        totalY -= y * factor;
    }

    // Updates when zoom. Sets the total x and y to the the total position of the
    // mouse
    public void mouseZoom(double x, double y) {
        totalX = xPosition;
        totalY = yPosition;
        zoomedAtX = x;
        zoomedAtY = y;
    }

    // set the canvas border to search for in the r-tree
    public void setCanvas() {
        canvasMinX = (0 - zoomedAtX) * factor + totalX;
        canvasMaxX = (canvasWidth - zoomedAtX) * factor + totalX;
        canvasMaxY = (0 - zoomedAtY) * factor + totalY;
        canvasMinY = (canvasHeight - zoomedAtY) * factor + totalY;
    }

    // returns the border in lat and lon.
    public float[] getCanvas() {
        canvasBoarder[0] = (float) (maxLat + (latFactor * canvasMinY));
        canvasBoarder[1] = (float) (maxLat + (latFactor * canvasMaxY));
        canvasBoarder[2] = (float) (minLon + (lonFactor * canvasMinX));
        canvasBoarder[3] = (float) (minLon + (lonFactor * canvasMaxX));

        return canvasBoarder;
    }

    // used in debugger to show the redbox when freezing frame
    public Way getCanvasOutline() {
        var listnode = new ArrayList<Node>();
        listnode.add(new Node((float) canvasBoarder[0], (float) canvasBoarder[2], 0));
        listnode.add(new Node((float) canvasBoarder[0], (float) canvasBoarder[3], 2));
        listnode.add(new Node((float) canvasBoarder[1], (float) canvasBoarder[3], 3));
        listnode.add(new Node((float) canvasBoarder[1], (float) canvasBoarder[2], 6));
        listnode.add(new Node((float) canvasBoarder[0], (float) canvasBoarder[2], 0));

        Way way = new Way(listnode);

        return way;
    }

    // Used to for calculating the pan when finding an address. Used for both
    // searching, and finding route
    public void findPosition(Node searchFromNode, Node searchToNode) {
        Float maxLat = searchFromNode.getLat();
        Float minLat = searchFromNode.getLat();
        Float minLon = searchFromNode.getLon();
        Float maxLon = searchFromNode.getLon();

        if (searchToNode != null) {
            if (searchToNode.getLat() > maxLat) {
                maxLat = searchToNode.getLat();

            } else {
                minLat = searchToNode.getLat();
            }
            if (searchToNode.getLon() > maxLon) {
                maxLon = searchToNode.getLon();
            } else {
                minLon = searchToNode.getLon();
            }
        }

        Float middleLat = (minLat + maxLat) / 2;
        Float middleLon = (minLon + maxLon) / 2;

        panPosition(middleLat, middleLon);
    }

    private void panPosition(double middleLat, double middleLon) {
        double minLat = canvasBoarder[0];
        double maxLat = canvasBoarder[1];
        double minLon = canvasBoarder[2];
        double maxLon = canvasBoarder[3];

        double canvasMiddleLat = (minLat + maxLat) / 2;
        double canvasMiddleLon = (minLon + maxLon) / 2;

        double difLat = (canvasMiddleLat - middleLat);
        double difLon = (canvasMiddleLon - middleLon);

        panY = (difLat / latFactor) / factor;
        panX = (difLon / lonFactor) / factor;

        dragged(panX, panY);
        setCanvas();

    }
}