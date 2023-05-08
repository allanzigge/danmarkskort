package handin2;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

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

    @Override
    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        gc.setFill(colors.get("building"));
        super.draw(gc, colors, determinant);
        gc.fill();

        System.out.println("Hej jeg er en test");
    }
}