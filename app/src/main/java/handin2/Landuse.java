package handin2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import java.util.ArrayList;

public class Landuse extends Way {
    private static final long serialVersionUID = -8734915653076753419L;
    Paint paint;
    String land;
    String type; 

    public Landuse(ArrayList<Node> way, String type) {
        super(way);
        this.type = type;
    }

    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        gc.setFill(colors.getLanduse(type));
        super.draw(gc, colors, determinant);
        if(type.equals("military") || type.equals("port")) {}
        else gc.fill();
    }
}