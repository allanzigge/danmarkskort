package GoKort.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Colorscheme implements Serializable {
  Color scrub;

  private Color building;

  public Color meadow;
  private Color grass;
  private Color residential;
  private Color other;

  Color sand;
  Color rock;
  Color defaultNatural;
  Color coastline;

  Color landuse;
  Color forest;
  private Color water;
  Color farmland;
  public Color defaultLanduse;

  private Color highway;
  private Color motorway;
  private Color trunk;
  private Color primary;
  private Color secondary;
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

  public Color getSecondary() {
    return secondary;
}

public void setSecondary(Color secondary) {
    this.secondary = secondary;
}

public Color getPrimary() {
    return primary;
}

public void setPrimary(Color primary) {
    this.primary = primary;
}

public Color getTrunk() {
    return trunk;
}

public void setTrunk(Color trunk) {
    this.trunk = trunk;
}

public Color getMotorway() {
    return motorway;
}

public void setMotorway(Color motorway) {
    this.motorway = motorway;
}

public Color getHighway() {
    return highway;
}

public void setHighway(Color highway) {
    this.highway = highway;
}

public Color getOther() {
    return other;
}

public void setOther(Color other) {
    this.other = other;
}

public Color getResidential() {
    return residential;
}

public void setResidential(Color residential) {
    this.residential = residential;
}

public Color getGrass() {
    return grass;
}

public void setGrass(Color grass) {
    this.grass = grass;
}

public Color getBuilding() {
    return building;
}

public void setBuilding(Color building) {
    this.building = building;
}

public Color getWater() {
    return water;
}

public void setWater(Color water) {
    this.water = water;
}

// Used in View to determine what options to show the user
  public ArrayList<String> getThemes() {
    return themes;
  }

  // This method assigns a color for drawing based off of a string
  public Color get(String element) {
    switch (element) {
      case "background":
        return getWater();
      case "building":
        return getBuilding();
      case "residential":
        return getResidential();
      default:
        return getOther(); // if element does not match -return this
    }
  }

  // This method is used in Highway to assign a color based off of the type
  public Color getHighway(String element) {
    switch (element) {
      case "motorway":
        return getMotorway();
      case "trunk":
        return getTrunk();
      case "primary":
        return getPrimary();
      case "secondary":
        return getSecondary();

      case "motorway_link":
        return getMotorway();
      case "trunk_link":
        return getTrunk();
      case "primary_link":
        return getPrimary();
      case "secondary_link":
        return getSecondary();
      default:
        return highway;
    }
  }

  // This method is used in Landuse to assign a color based off of the type
  public Color getLanduse(String element) {
    switch (element) {
      case "basin":
        return getWater();
      case "reservoir":
        return getWater();
      case "salt_pond":
        return getWater();
      case "port":
        return getWater();
      case "aquaculture":
        return getWater();
      case "meadow":
        return getGrass();
      case "orchard":
        return getGrass();
      case "plant_nursery":
        return getGrass();
      case "vineyard":
        return getGrass();
      case "grass":
        return getGrass();
      case "village_green":
        return getGrass();
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
        return getWater();
      case "water":
        return getWater();
      case "wetland":
        return meadow;
      case "spring":
        return getWater();

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
        return getGrass();
      case "heath":
        return getGrass();

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

    setBuilding(Color.DARKGRAY);
    landuse = Color.DARKSEAGREEN;
    meadow = Color.DARKSEAGREEN;
    setResidential(Color.LIGHTGRAY);
    setOther(Color.GAINSBORO);
    sand = Color.rgb(255, 229, 180);
    rock = Color.rgb(172, 173, 172);
    coastline = Color.PURPLE;
    defaultNatural = Color.PINK;
    defaultLanduse = Color.rgb(222, 220, 220);
    setGrass(Color.rgb(200, 243, 205));
    forest = Color.rgb(149, 232, 158);
    setWater(Color.rgb(139, 180, 249));
    farmland = Color.rgb(254, 226, 147);
    setHighway(Color.WHITE);
    setMotorway(Color.RED);
    setTrunk(Color.ORANGE);
    setPrimary(Color.rgb(214, 157, 2));
    setSecondary(Color.LIGHTYELLOW);
  }

  // Sets all the colors needed for the colorblind mode
  private void colorblindMode() {

    setWater(Color.rgb(139, 171, 241));
    setBuilding(Color.rgb(179, 199, 247));
    landuse = Color.rgb(245, 118, 0);
    meadow = Color.rgb(207, 235, 182);
    setGrass(Color.rgb(137, 206, 0));
    setResidential(Color.rgb(217, 228, 255));
    setOther(Color.rgb(252, 201, 181));

    setHighway(Color.WHITE);
    setMotorway(Color.rgb(141, 210, 221));
    setTrunk(Color.rgb(141, 210, 221));
    setPrimary(Color.rgb(230, 48, 138));
    setSecondary(Color.WHITE);

    farmland = Color.rgb(252, 201, 181);
    forest = Color.rgb(144, 216, 178);
  }

  // Sets all the colors needed for the darkmode mode
  public void darkMode() {
    scrub = Color.rgb(42, 66, 40);
    setWater(Color.rgb(31, 29, 54));
    setBuilding(Color.rgb(134, 72, 121));
    landuse = Color.DARKGRAY;
    meadow = Color.DARKGREEN;
    setGrass(Color.DARKKHAKI);
    setResidential(Color.DARKRED);
    setOther(Color.DARKGREY);
    farmland = Color.DARKGREEN;
    forest = Color.rgb(146, 154, 171);
    setHighway(Color.rgb(233, 166, 166));
    setMotorway(Color.RED);
    setTrunk(Color.ORANGE);
    setPrimary(Color.LIGHTGOLDENRODYELLOW);
    setSecondary(Color.LIGHTYELLOW);
  }

public Object getDefaultLanduse() {
    return defaultLanduse;
}

public Object getMeadow() {
    return meadow;
}
}