package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;
/* 
This class is accountable for holding the current 
color for each type of element   
*/

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

  public Colorscheme() { // DEFAULT COLORS
    defaultMode();
    this.themes = new ArrayList<>();
    themes.add("Default Mode");
    themes.add("Dark Mode");
    themes.add("Color Blind");
  }

  public ArrayList<String> getThemes() {
    return themes;
  }

  public Color get(String element) { // GETTER
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

  public Color getNatural(String element) {
    switch (element) {
      case "bay":
        return water;
      case "water":
        return water;
      case "wetland":
        return meadow;
      case "strait":
        return water;
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

  public void defaultMode() {

    building = Color.DARKGRAY;
    landuse = Color.DARKSEAGREEN;
    meadow = Color.DARKSEAGREEN;
    residential = Color.LIGHTGRAY;
    other = Color.GAINSBORO;

    // Natural
    sand = Color.rgb(255, 229, 180);
    rock = Color.rgb(172, 173, 172);
    coastline = Color.PURPLE;
    // island = Color.rgb(254, 247, 224);
    defaultNatural = Color.PINK;

    // Landuse
    defaultLanduse = Color.rgb(222, 220, 220);
    grass = Color.rgb(200, 243, 205);
    forest = Color.rgb(149, 232, 158);
    water = Color.rgb(139, 180, 249);
    farmland = Color.rgb(254, 226, 147);

    // highways
    highway = Color.WHITE;
    motorway = Color.RED;
    trunk = Color.ORANGE;
    primary = Color.rgb(214, 157, 2);
    secondary = Color.LIGHTYELLOW;
  }

  private void colorblindMode() {
    

    water = Color.BLACK;
    building = Color.DARKGRAY;
    landuse = Color.rgb(155, 164, 181);
    meadow = Color.rgb(57, 72, 103);
    grass = Color.rgb(66, 66, 66);
    residential = Color.rgb(43, 43, 43);
    other = Color.rgb(8, 8, 8);

    highway = Color.WHITE;
    motorway = Color.WHITE;
    trunk = Color.WHITE;
    primary = Color.WHITE;
    secondary = Color.WHITE;

    farmland = Color.rgb(146, 154, 171);
    forest = Color.rgb(146, 154, 171);
  }

  public void darkMode() {
    scrub = Color.rgb(42, 66, 40);
    //black
    water = Color.rgb(31, 29, 54);
    //violet
    building = Color.rgb(134, 72, 121);
    landuse = Color.DARKGRAY;
    meadow = Color.DARKGREEN;
    grass = Color.DARKKHAKI;
    residential = Color.DARKRED;
    other = Color.DARKGREY;
    farmland = Color.DARKGREEN;
    forest = Color.rgb(146, 154, 171);
    //light peach
    highway = Color.rgb(233, 166, 166);
    motorway = Color.RED;
    trunk = Color.ORANGE;
    primary = Color.LIGHTGOLDENRODYELLOW;
    secondary = Color.LIGHTYELLOW;
  }

  // public void bloody() {
  //   scrub = Color.rgb(247, 7, 7);
  //   water = Color.rgb(145, 70, 70);
  //   building = Color.rgb(247, 156, 156);
  //   landuse = Color.rgb(214, 43, 43);
  //   highway = Color.rgb(110, 36, 36);
  //   meadow = Color.rgb(242, 97, 97);
  //   grass = Color.rgb(227, 2, 2);
  //   residential = Color.rgb(179, 64, 64);
  //   other = Color.rgb(107, 0, 0);

  //   highway = Color.rgb(110, 36, 36);
  //   motorway = Color.RED;
  //   trunk = Color.ORANGE;
  //   primary = Color.LIGHTGOLDENRODYELLOW;
  //   secondary = Color.LIGHTYELLOW;
  // }

}