package handin2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import javafx.scene.paint.Color;

import org.junit.jupiter.api.Test;

import handin2.GUI.Colorscheme;

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

            assertEquals(clsm.getWater(), Color.rgb(139, 180, 249));
            assertEquals(clsm.getBuilding(), Color.DARKGRAY);
            assertEquals(clsm.getDefaultLanduse(), Color.rgb(222, 220, 220));
            assertEquals(clsm.getMeadow(), Color.DARKSEAGREEN);
            assertEquals(clsm.getGrass(), Color.rgb(200, 243, 205));
            assertEquals(clsm.getResidential(), Color.LIGHTGRAY);
            assertEquals(clsm.getOther(), Color.GAINSBORO);
            assertEquals(clsm.getHighway(), Color.WHITE);
            assertEquals(clsm.getMotorway(), Color.RED);
            assertEquals(clsm.getTrunk(), Color.ORANGE);
            assertEquals(clsm.getPrimary(), Color.rgb(214, 157, 2));
            assertEquals(clsm.getSecondary(), Color.LIGHTYELLOW);
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

            assertEquals(clsm.getWater(), Color.rgb(139, 180, 249));
    }

    @Test
    public void setColorschemeTest1() {

            Colorscheme clsm = new Colorscheme();
            clsm.setColorscheme("Dark Mode");
            assertEquals(clsm.getWater(), Color.rgb(31, 29, 54));
    }

    @Test
    public void setColorschemeTest2() {

            Colorscheme clsm = new Colorscheme();
            clsm.setColorscheme("Color Blind");
            assertEquals(clsm.getWater(), Color.BLACK);
    }
}
