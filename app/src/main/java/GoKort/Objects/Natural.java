package GoKort.Objects;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

import GoKort.GUI.Colorscheme;

public class Natural extends Way {
    private static final long serialVersionUID = -1469520736476477560L;
    String value;
    String type;

    public Natural(ArrayList<Node> way, String type) {
        super(way);

        this.type = type;
    }

    //Sets the fill settings depending on the natural type
    //Then uses the super class draw method to create the path
    //ans draws the natural path with fill()
    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        gc.setFill(colors.getNatural(type));
        super.draw(gc, colors, determinant);
        gc.fill();
    }
}