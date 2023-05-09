package handin2.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import java.util.ArrayList;

import handin2.GUI.Colorscheme;

public class Landuse extends Way {
    private static final long serialVersionUID = -8734915653076753419L;
    Paint paint;
    String land;
    String type; 

    public Landuse(ArrayList<Node> way, String type) {
        super(way);
        this.type = type;
    }

    //Sets the fill settings depending on the landuse type
    //Then uses the super class draw method to create the path
    //ans draws the landuse path with fill()
    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        gc.setFill(colors.getLanduse(type));
        super.draw(gc, colors, determinant);
        gc.fill();
    }
}