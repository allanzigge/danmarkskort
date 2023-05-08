package handin2;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class Natural extends Way {
    private static final long serialVersionUID = -1469520736476477560L;
    String value;
    String type; 

    public Natural(ArrayList<Node> way, String type) {
        super(way);
        
        this.type = type;
    }
    
    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        if(!type.equals("cliff") && !type.equals("tree_row")) {
            gc.setFill(colors.getNatural(type));
            super.draw(gc, colors, determinant);
            gc.fill();
        }
       
        
    }
}