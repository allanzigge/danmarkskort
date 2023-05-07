package handin2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import javafx.scene.paint.Color;

import org.junit.jupiter.api.Test;

public class ColorschemeTest {

    @Test
    public void getThemesTest() {

            Colorscheme clsm = new Colorscheme();
            
            ArrayList<String> expected = new ArrayList<String>();
            expected.add("Default Mode");
            expected.add("Dark Mode");
            expected.add("Color Blind");
            ArrayList<String> actual = clsm.getThemes();

            assertEquals(expected, actual);
    }

    @Test
    public void defaulModeTest() {

            Colorscheme clsm = new Colorscheme();
            clsm.defaultMode();

            assertEquals(clsm.water, Color.rgb(139, 180, 249));
            assertEquals(clsm.building, Color.DARKGRAY);
            assertEquals(clsm.defaultLanduse, Color.rgb(222, 220, 220));
            assertEquals(clsm.meadow, Color.DARKSEAGREEN);
            assertEquals(clsm.grass, Color.rgb(200, 243, 205));
            assertEquals(clsm.residential, Color.LIGHTGRAY);
            assertEquals(clsm.other, Color.GAINSBORO);
            assertEquals(clsm.highway, Color.WHITE);
            assertEquals(clsm.motorway, Color.RED);
            assertEquals(clsm.trunk, Color.ORANGE);
            assertEquals(clsm.primary, Color.rgb(214, 157, 2));
            assertEquals(clsm.secondary, Color.LIGHTYELLOW);
    }


    @Test
    public void getHighwayColorTest() {

            Colorscheme clsm = new Colorscheme();

            assertEquals(Color.WHITE, clsm.getHighway("highway"));
            assertEquals(Color.RED, clsm.getHighway("motorway"));
            assertEquals(Color.ORANGE, clsm.getHighway("trunk"));
            assertEquals(Color.rgb(214, 157, 2), clsm.getHighway("primary"));
            assertEquals(Color.LIGHTYELLOW, clsm.getHighway("secondary"));
    }

    @Test
    public void setColorschemeTest0() {

            Colorscheme clsm = new Colorscheme();
            clsm.setColorscheme("Default Mode");

            assertEquals(clsm.water, Color.rgb(139, 180, 249));
    }

    @Test
    public void setColorschemeTest1() {

            Colorscheme clsm = new Colorscheme();
            clsm.setColorscheme("Dark Mode");
            assertEquals(clsm.water, Color.rgb(31, 29, 54));
    }

    @Test
    public void setColorschemeTest2() {

            Colorscheme clsm = new Colorscheme();
            clsm.setColorscheme("Color Blind");
            assertEquals(clsm.water, Color.BLACK);
    }
}
