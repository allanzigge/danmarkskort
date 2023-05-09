package GoKort.Objects;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

import GoKort.GUI.Colorscheme;

public class Building extends Way {
    private static final long serialVersionUID = 3755214644074009041L;
    String buildingType;

    public Building(ArrayList<Node> way) {
        super(way);
    }

    public Building(ArrayList<Node> way, String v) {
        super(way);
        this.buildingType = v;
    }

    //The draw method is overwritten so that paths that are buildings get drawn using fill()
    //Used in the View redraw() method
    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        gc.setFill(colors.get("building"));
        super.draw(gc, colors, determinant);
        gc.fill();
    }
}