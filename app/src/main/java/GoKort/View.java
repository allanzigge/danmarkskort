package GoKort;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import GoKort.GUI.*;
import GoKort.Objects.Highway;
import GoKort.Objects.Node;
import GoKort.Objects.Way;
import GoKort.Pathfinding.Address;
import GoKort.Pathfinding.Edge;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class View {
    double canvasHeighScale = 1;
    double canvasWidthScale = 1;

    Canvas canvas = new Canvas(640, 480);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Affine trans = new Affine();
    Scalebar scalebar;
    Colorscheme colors;
    Model model;
    Position position;
    Layout layout = new Layout();
    Address TSTadr;
    Scene scene;

    MenuItem fileChoserMenu;
    CheckMenuItem showDebugger;
    CheckMenuItem freezeFrame;
    Label mousePositionLabel = new Label();
    Label redrawTimerLabel = new Label();

    ArrayList<Address> POI = new ArrayList<Address>();
    Address[] routeAdrs = new Address[2];
    ArrayList<Highway> currentRoute = new ArrayList<Highway>();
    String routeInstrux = "";

    Node searchFromNode = null;
    Node searchToNode = null;
    Way path;
    String transportType = "car";

    ImageView serachLoopImage;
    ImageView findRouteImage;
    ImageView turnLeftImage;
    ImageView turnRightImage;

    Button favoritesButton;
    Button searchMenu;
    Button findRouteMenu;
    Button favoritesButton2;
    Button routeDescriptionButton;
    Button swapButton;
    Button favoriteMenu;
    Button settingsMenu;
    Button clearFavoritesButton;
    Button clearFindRouteButton;
    Button walkButton;
    Button bikeButton;
    Button carButton;
    Button routeDescriptionCloseButton;
    Button copyButton;

    Label scaleLabel;
    Label nearestRoad;
    Rectangle favoritBackgound;

    TextField searchTextField;
    TextField findRouteFromTextField;
    TextField findRouteToTextField;

    VBox routeDescriptionInstruction;
    VBox searchResultFromVBox;
    VBox searchResultToVBox;
    VBox searchResultVBox;

    VBox favoritListVBox;
    HBox routeInfoHBox;
    HBox routeDescriptionHhox;

    ScrollPane routeDescriptionScrollpane;

    StackPane searchStackpan;
    StackPane settingStackPane;
    StackPane favoriteStackPane;
    StackPane routeDescriptionStackPane;
    StackPane findRouteStackPane;
    StackPane debuggerOverlay;

    public View(Model model, Stage primaryStage) {
        this.model = model;
        colors = new Colorscheme(); // Creates coclorscheme

        VBox debuggerVbox = new VBox();
        debuggerVbox.setAlignment(Pos.TOP_RIGHT);
        debuggerVbox.getChildren().addAll(mousePositionLabel, redrawTimerLabel);
        debuggerOverlay = layout.getStackPane(100, 100);
        debuggerOverlay.getChildren().addAll(debuggerVbox);

        // UI design
        // images that is being copied for favoritelist and directions
        turnLeftImage = layout.getImageView("file:icons/turnLeft.png");
        turnRightImage = layout.getImageView("file:icons/turnRight.png");
        serachLoopImage = layout.getImageView("file:icons/searchLoop.png");
        findRouteImage = layout.getImageView("file:icons/findRoute.png");

        // elements for searching stackpane
        searchTextField = layout.getTextField("Skriv adresse");
        searchResultVBox = layout.getSearchResultVbox();
        favoritesButton = layout.getButtonIcon(layout.getImageView("file:icons/starBlack.png"), 20);
        favoritesButton.setTooltip(new Tooltip("Add address to favoritelist"));

        // adds all elements to search stackpane and makes the insets
        searchStackpan = layout.getStackPane(40, 340);
        searchStackpan.getChildren().addAll(searchTextField, searchResultVBox, favoritesButton);
        StackPane.setMargin(searchTextField, new Insets(0, 0, 0, 20));
        StackPane.setMargin(searchResultVBox, new Insets(40, 0, 0, 40));
        StackPane.setMargin(favoritesButton, new Insets(0, 0, 0, 300));

        // elements for find route stackpane
        carButton = layout.getButtonIcon(layout.getImageView("file:icons/car.png"), 20);
        carButton.setBackground(new Background(new BackgroundFill(Color.MAGENTA, null, null)));
        bikeButton = layout.getButtonIcon(layout.getImageView("file:icons/bike.png"), 20);
        walkButton = layout.getButtonIcon(layout.getImageView("file:icons/walk.png"), 20);
        clearFindRouteButton = layout.getButtonIcon(layout.getImageView("file:icons/clear.png"), 20);
        clearFindRouteButton.setTooltip(new Tooltip("Delete search"));
        findRouteFromTextField = layout.getTextField("Find vej fra");
        findRouteToTextField = layout.getTextField("Find vej til");
        searchResultFromVBox = layout.getSearchResultVbox();
        searchResultToVBox = layout.getSearchResultVbox();
        favoritesButton2 = layout.getButtonIcon(layout.getImageView("file:icons/starBlack.png"), 20);
        favoritesButton2.setTooltip(new Tooltip("Add route to favoritelist"));
        routeDescriptionButton = layout.getButtonIcon(layout.getImageView("file:icons/description.png"), 20);
        routeDescriptionButton.setTooltip(new Tooltip("Show directions"));
        swapButton = layout.getButtonIcon(layout.getImageView("file:icons/swap.png"), 20);
        swapButton.setTooltip(new Tooltip("Swap destinations"));
        HBox veichleOption = new HBox(carButton, bikeButton, walkButton);
        veichleOption.setSpacing(10);
        veichleOption.setAlignment(Pos.BASELINE_CENTER);
        Shape box = layout.getRegtangle(130, 340);

        // adds all elements to route stackpane and Insets elements
        findRouteStackPane = layout.getStackPane(100, 300);
        findRouteStackPane.getChildren().addAll(box, findRouteFromTextField, findRouteToTextField, veichleOption,
                searchResultFromVBox, searchResultToVBox, favoritesButton2, swapButton, routeDescriptionButton,
                clearFindRouteButton);
        StackPane.setMargin(clearFindRouteButton, new Insets(0, 0, 90, 300));
        StackPane.setMargin(favoritesButton2, new Insets(45, 0, 45, 300));
        StackPane.setMargin(routeDescriptionButton, new Insets(90, 0, 0, 300));
        StackPane.setMargin(findRouteFromTextField, new Insets(0, 0, 0, 20));
        StackPane.setMargin(searchResultFromVBox, new Insets(40, 0, 0, 40));
        StackPane.setMargin(swapButton, new Insets(45, 0, 0, 0));
        StackPane.setMargin(veichleOption, new Insets(90, 0, 0, 0));
        StackPane.setMargin(findRouteToTextField, new Insets(45, 0, 0, 20));
        StackPane.setMargin(searchResultToVBox, new Insets(85, 0, 0, 40));

        // elements for favoritelist
        favoritBackgound = layout.getRegtangle(40, 340);
        favoritListVBox = new VBox();
        clearFavoritesButton = layout.getButtonIcon(layout.getImageView("file:icons/clear.png"), 20);
        clearFavoritesButton.setTooltip(new Tooltip("Delete favoritelist"));

        // adds all elements to favoritelist stackpane and Insets elements
        favoriteStackPane = layout.getStackPane(40, 340);
        favoriteStackPane.getChildren().addAll(favoritBackgound, favoritListVBox, clearFavoritesButton);
        StackPane.setMargin(favoritListVBox, new Insets(40, 0, 0, 0));
        StackPane.setMargin(clearFavoritesButton, new Insets(0, 0, 0, 300));

        // elemnts for route instruction
        Shape backgroundBox = layout.getRegtangle(280, 340);
        routeDescriptionHhox = new HBox();
        routeDescriptionCloseButton = layout.getButtonIcon(layout.getImageView("file:icons/close.png"), 20);
        routeDescriptionCloseButton.setTooltip(new Tooltip("Hide routedescription"));
        copyButton = layout.getButtonIcon(layout.getImageView("file:icons/copy.png"), 20);
        copyButton.setTooltip(new Tooltip("Copy to clipboard"));
        routeDescriptionInstruction = layout.getSearchResultVbox();
        routeDescriptionScrollpane = new ScrollPane(routeDescriptionInstruction);
        routeInfoHBox = new HBox();

        // adds all elements to route instructions stackpane and Insets elements
        routeDescriptionStackPane = layout.getStackPane(280, 340);
        routeDescriptionStackPane.getChildren().addAll(backgroundBox, routeInfoHBox, routeDescriptionScrollpane,
                routeDescriptionCloseButton, copyButton);
        StackPane.setMargin(routeDescriptionCloseButton, new Insets(0, 0, 0, 300));
        StackPane.setMargin(routeDescriptionScrollpane, new Insets(40, 0, 20, 0));
        StackPane.setMargin(routeInfoHBox, new Insets(10, 0, 0, 40));

        // elements for setting menu
        Menu fileMenu = new Menu("File");
        File file = new File("data");

        // user can upload their own files, endning with osm or zip.
        String[] pathnames = file.list();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("OSM files", "*.osm", "*.osm.zip"));
        fileChoserMenu = new MenuItem("Select file");
        fileChoserMenu.setOnAction(e -> {
            File file2 = fileChooser.showOpenDialog(primaryStage);
            if (file2 != null) {
                try {
                    Model model2 = new Model(file2.getPath());
                    View view = new View(model2, primaryStage);
                    new Controller(model2, view);
                } catch (ClassNotFoundException | IOException | XMLStreamException | FactoryConfigurationError e1) {
                    e1.printStackTrace();
                }
            }
        });

        // gets themes from colorSchemes and add them to menuItem
        for (String name : pathnames) {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(e -> {
                try {
                    var model2 = new Model("data/" + name);
                    View view = new View(model2, primaryStage);
                    new Controller(model2, view);
                } catch (ClassNotFoundException | IOException | XMLStreamException | FactoryConfigurationError e1) {
                    e1.printStackTrace();
                }
            });
            fileMenu.getItems().add(menuItem);
        }

        // gets themes from colorSchemes and add them to menuItem
        Menu colorMenu = new Menu("Color");
        ArrayList<String> themes = colors.getThemes();
        for (String name : themes) {
            MenuItem menuItem = new MenuItem(name);
            menuItem.setOnAction(e -> {
                colors.setColorscheme(name);
                redraw();
            });
            colorMenu.getItems().add(menuItem);
        }
        MenuBar menuBar = new MenuBar();
        Menu toolMenu = new Menu("Tools");
        freezeFrame = new CheckMenuItem("Freeze Canvas");
        showDebugger = new CheckMenuItem("Show DebuggerOverlay");
        toolMenu.getItems().addAll(freezeFrame, showDebugger);

        fileMenu.getItems().addAll(new SeparatorMenuItem(), fileChoserMenu);
        menuBar.getMenus().addAll(fileMenu, colorMenu, toolMenu);

        // adds elements to settings stackpane and insets elements
        settingStackPane = layout.getStackPane(40, 200);
        settingStackPane.getChildren().add(menuBar);
        StackPane.setMargin(menuBar, new Insets(10, 0, 0, 30));

        // mapboarderpane
        // scalebar drawn in the bottom
        int scalebarLineStart = 5;
        int scalebarLineWidth = (int) (canvas.getHeight() * 0.1) + scalebarLineStart;
        int scalebarLineHight = 10;
        Line line1 = new Line(scalebarLineStart, scalebarLineHight / 2, scalebarLineWidth, scalebarLineHight / 2);
        Line line2 = new Line(scalebarLineStart, 0, scalebarLineStart, scalebarLineHight);
        Line line3 = new Line(scalebarLineWidth, 0, scalebarLineWidth, scalebarLineHight);
        Group scalebarLine = new Group(line1, line2, line3);
        scalebar = new Scalebar(model.getMaxlat() - model.getMinlat(), (float) (canvas.getHeight()),
                (float) (line1.getEndX() - line1.getStartX()));
        scaleLabel = new Label(scalebar.getScaleLabel());
        nearestRoad = new Label("Navnet på nærmeste vej");

        // elements in mapboarderpane
        BorderPane mapBoarderPane = new BorderPane();
        mapBoarderPane.setCenter(canvas);
        HBox scalebarHBox = new HBox(scalebarLine, scaleLabel, nearestRoad);
        scalebarHBox.setSpacing(10);
        scalebarHBox.setPadding(new Insets(0, 0, 0, 10));
        mapBoarderPane.setBottom(scalebarHBox);

        // Elements for root stackpane
        searchMenu = layout.getButtonIcon(serachLoopImage, 20);
        searchMenu.setTooltip(new Tooltip("Find address"));
        findRouteMenu = layout.getButtonIcon(findRouteImage, 20);
        findRouteMenu.setTooltip(new Tooltip("Find route"));
        favoriteMenu = layout.getButtonIcon(layout.getImageView("file:icons/bookmark.png"), 20);
        favoriteMenu.setTooltip(new Tooltip("Favoritelist"));
        settingsMenu = layout.getButtonIcon(layout.getImageView("file:icons/settings.png"), 20);
        settingsMenu.setTooltip(new Tooltip("Settings"));

        // rootstackpane adding element and insets
        StackPane root = new StackPane(mapBoarderPane, searchStackpan, settingStackPane, settingsMenu,
                favoriteStackPane, favoriteMenu, findRouteStackPane, routeDescriptionStackPane, findRouteMenu,
                searchMenu, debuggerOverlay);
        root.setAlignment(Pos.TOP_LEFT);

        int buttonDistance = 45;
        StackPane.setMargin(searchMenu, new Insets(10, 0, 0, 10));
        StackPane.setMargin(searchStackpan, new Insets(10, 0, 0, 10));
        StackPane.setMargin(findRouteMenu, new Insets(buttonDistance + 10, 0, 0, 10));
        StackPane.setMargin(findRouteStackPane, new Insets(buttonDistance + 10, 0, 0, 10));
        StackPane.setMargin(routeDescriptionStackPane, new Insets(190, 0, 0, 10));
        StackPane.setMargin(favoriteStackPane, new Insets(buttonDistance * 2 + 10, 0, 0, 10));
        StackPane.setMargin(favoriteMenu, new Insets(buttonDistance * 2 + 10, 0, 0, 10));
        StackPane.setMargin(settingsMenu, new Insets(buttonDistance * 3 + 10, 0, 0, 10));
        StackPane.setMargin(settingStackPane, new Insets(buttonDistance * 3 + 10, 0, 0, 10));
        StackPane.setMargin(debuggerOverlay, new Insets(10, 0, 0, 100));

        // initialise last stuff for view
        position = new Position(scalebar.getScale(), canvas, model);
        primaryStage.setTitle("GoKort");
        primaryStage.getIcons().add(new Image("file:icons/logo.jpg"));
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        redraw();
        pan((float) (-0.56 * model.getMinlon()), model.getMaxlat());
        zoom(0, 0, (float) canvas.getHeight() / (model.getMaxlat() - model.getMinlat()));
    }

    // Draws everything seen on the map.
    void redraw() {
        long startTimer = System.currentTimeMillis(); // Timer for tracking how long time it takes to redraw. Used for
                                                      // debugging features, and to determine runtime smoothness

        gc.setTransform(new Affine());
        gc.setFillRule(FillRule.EVEN_ODD);
        gc.setFill(colors.get("background")); // GAINSBORO
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        float zoom = scalebar.getScale();

        if (!freezeFrame.isSelected()) { // If the freezeframe debugger method is used, then the current canvas is
                                         // saved.
            position.setCanvas();
        }

        // What to draw is determined by the zoom level. Depending on the zoom level,
        // different rtrees are searched, and the result is drawn.

        // The background land (islands, peninsula, islets osv, is allways drawn)
        for (Way way : model.firstLayerRTree.search(position.getCanvas())) {
            way.draw(gc, colors, (float) trans.determinant());
        }

        if (zoom * canvasHeighScale * canvasWidthScale < 500) {
            for (Way way : model.thirdLayerRTree.search(position.getCanvas())) { // Naturals
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.secondLayerRTree.search(position.getCanvas())) { // Landuses
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.fourthLayerRTree.search(position.getCanvas())) { // Buildings
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.smallRoadRtree.search(position.getCanvas())) { // All roads
                way.draw(gc, colors, (float) trans.determinant());

            }

        } else if (zoom * canvasHeighScale * canvasWidthScale < 2000) {
            for (Way way : model.secondLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.mediumRoadRTree.search(position.getCanvas())) { // Medium and big roads
                way.draw(gc, colors, (float) trans.determinant());
            }

        } else if (zoom * canvasHeighScale * canvasWidthScale < 5000) {
            for (Way way : model.secondLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.mediumRoadRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }

        }

        // And the bigroad r tree is allways drawn ontop
        for (Way way : model.bigRoadRTree.search(position.getCanvas())) { // Only big roads

            way.draw(gc, colors, (float) trans.determinant());
        }

        // Draws the route, if there is a route
        if (model.route.size() > 0) {
            if (transportType.equals("walk"))
                gc.setStroke(Color.CYAN);
            else if (transportType.equals("bike"))
                gc.setStroke(Color.TOMATO);
            else
                gc.setStroke(Color.MAGENTA);
            for (Edge edge : model.route) {
                edge.draw(gc, colors, (float) trans.determinant());
                gc.stroke();
            }
        }

        if (freezeFrame.isSelected()) {
            gc.setStroke(Color.RED);
            position.getCanvasOutline().draw(gc, colors, (float) trans.determinant());
            gc.setLineWidth(3 / Math.sqrt(trans.determinant()));
            gc.stroke();
        }

        if (searchFromNode != null) {
            double radius = 5 / Math.sqrt(trans.determinant());
            gc.setFill(Color.GREEN);

            gc.fillOval(0.56 * searchFromNode.getLon() - radius, -searchFromNode.getLat() - radius, radius * 2,
                    radius * 2);
        }

        if (searchToNode != null) {
            double radius = 5 / Math.sqrt(trans.determinant());
            gc.setFill(Color.RED);

            gc.fillOval(0.56 * searchToNode.getLon() - radius, -searchToNode.getLat() - radius, radius * 2, radius * 2);

        }

        long stopTimer = System.currentTimeMillis();
        redrawTimerLabel.setText((stopTimer - startTimer) + " : Redraw Timer");

    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
        redraw();
    }
}