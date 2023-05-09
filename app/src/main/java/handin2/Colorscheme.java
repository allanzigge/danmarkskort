package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Colorscheme implements Serializable {
  Color scrub;

  Color building;

  Color meadow;
  Color grass;
  Color residential;
  Color other;

  Color sand;
  Color rock;
  Color defaultNatural;
  Color coastline;

  Color landuse;
  Color forest;
  Color water;
  Color farmland;
  Color defaultLanduse;

  Color highway;
  Color motorway;
  Color trunk;
  Color primary;
  Color secondary;
  Color island;

  ArrayList<String> themes;

  public Colorscheme() {
    defaultMode();
    // Allows for new themes to be added automatically via View
    // For each theme added below, a new option is added to the GUI
    this.themes = new ArrayList<>();
    themes.add("Default Mode");
    themes.add("Dark Mode");
    themes.add("Color Blind");
  }

  // Used in View to determine what options to show the user
  public ArrayList<String> getThemes() {
    return themes;
  }

  // This method assigns a color for drawing based off of a string
  public Color get(String element) {
    switch (element) {
      case "background":
        return water;
      case "building":
        return building;
      case "residential":
        return residential;
      default:
        return other; // if element does not match -return this
    }
  }

  // This method is used in Highway to assign a color based off of the type
  public Color getHighway(String element) {
    switch (element) {
      case "motorway":
        return motorway;
      case "trunk":
        return trunk;
      case "primary":
        return primary;
      case "secondary":
        return secondary;

      case "motorway_link":
        return motorway;
      case "trunk_link":
        return trunk;
      case "primary_link":
        return primary;
      case "secondary_link":
        return secondary;
      default:
        return highway;
    }
  }

  // This method is used in Landuse to assign a color based off of the type
  public Color getLanduse(String element) {
    switch (element) {
      case "basin":
        return water;
      case "reservoir":
        return water;
      case "salt_pond":
        return water;
      case "port":
        return water;
      case "aquaculture":
        return water;
      case "meadow":
        return grass;
      case "orchard":
        return grass;
      case "plant_nursery":
        return grass;
      case "vineyard":
        return grass;
      case "grass":
        return grass;
      case "village_green":
        return grass;
      case "forest":
        return forest;
      case "cemetery":
        return forest;
      case "farmyard":
        return farmland;
      case "greenhouse_horticulture":
        return farmland;
      case "farmland":
        return farmland;
      case "residential":
        return defaultLanduse;
      default:
        return defaultLanduse;
    }
  }

  // This method is used in Natural to assign a color based off of the type
  public Color getNatural(String element) {
    switch (element) {
      case "bay":
        return water;
      case "water":
        return water;
      case "wetland":
        return meadow;
      case "spring":
        return water;

      case "beach":
        return sand;
      case "shoal":
        return sand;
      case "shingle":
        return sand;
      case "sand":
        return sand;

      case "wood":
        return forest;
      case "tree_row":
        return forest;

      case "shrubbery":
        return forest;
      case "scrub":
        return forest;

      case "cliff":
        return rock;
      case "bare_rock":
        return rock;
      case "rock":
        return rock;
      case "scree":
        return rock;

      case "island":
        return farmland;

      case "coastline":
        return coastline;

      case "grassland":
        return grass;
      case "heath":
        return grass;

      default:
        return defaultNatural;
    }
  }

  // This method allows View to change theme based off of user input
  public void setColorscheme(String theme) {
    if (theme.equals("Default Mode")) {
      defaultMode();
    }
    if (theme.equals("Dark Mode")) {
      darkMode();
    }
    if (theme.equals("Color Blind")) {
      colorblindMode();
    }
  }

  // Sets all the colors needed for the default mode
  public void defaultMode() {

    building = Color.DARKGRAY;
    landuse = Color.DARKSEAGREEN;
    meadow = Color.DARKSEAGREEN;
    residential = Color.LIGHTGRAY;
    other = Color.GAINSBORO;
    sand = Color.rgb(255, 229, 180);
    rock = Color.rgb(172, 173, 172);
    coastline = Color.PURPLE;
    defaultNatural = Color.PINK;
    defaultLanduse = Color.rgb(222, 220, 220);
    grass = Color.rgb(200, 243, 205);
    forest = Color.rgb(149, 232, 158);
    water = Color.rgb(139, 180, 249);
    farmland = Color.rgb(254, 226, 147);
    highway = Color.WHITE;
    motorway = Color.RED;
    trunk = Color.ORANGE;
    primary = Color.rgb(214, 157, 2);
    secondary = Color.LIGHTYELLOW;
  }

  // Sets all the colors needed for the colorblind mode
  private void colorblindMode() {

    water = Color.rgb(139, 171, 241);
    building = Color.rgb(179, 199, 247);
    landuse = Color.rgb(245, 118, 0);
    meadow = Color.rgb(207, 235, 182);
    grass = Color.rgb(137, 206, 0);
    residential = Color.rgb(217, 228, 255);
    other = Color.rgb(252, 201, 181);

    highway = Color.WHITE;
    motorway = Color.rgb(141, 210, 221);
    trunk = Color.rgb(141, 210, 221);
    primary = Color.rgb(230, 48, 138);
    secondary = Color.WHITE;

    farmland = Color.rgb(252, 201, 181);
    forest = Color.rgb(144, 216, 178);
  }

  // Sets all the colors needed for the darkmode mode
  public void darkMode() {
    scrub = Color.rgb(42, 66, 40);
    water = Color.rgb(31, 29, 54);
    building = Color.rgb(134, 72, 121);
    landuse = Color.DARKGRAY;
    meadow = Color.DARKGREEN;
    grass = Color.DARKKHAKI;
    residential = Color.DARKRED;
    other = Color.DARKGREY;
    farmland = Color.DARKGREEN;
    forest = Color.rgb(146, 154, 171);
    highway = Color.rgb(233, 166, 166);
    motorway = Color.RED;
    trunk = Color.ORANGE;
    primary = Color.LIGHTGOLDENRODYELLOW;
    secondary = Color.LIGHTYELLOW;
  }
}