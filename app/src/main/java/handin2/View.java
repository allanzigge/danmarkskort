package handin2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
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
    Layout layout = new Layout();
    TextField fromSearch = new TextField("");
    TextField toSearch = new TextField("");
    Address TSTadr = null;

    Colorscheme colors;
    Canvas canvas = new Canvas(640, 480);
    double canvasHeighScale = 1;
    double canvasWidthScale = 1;

    GraphicsContext gc = canvas.getGraphicsContext2D();

    Affine trans = new Affine();
    Label scaleLabel;
    Scalebar scalebar;
    Model model;
    Position position;
    Scene scene;
    Label nearestRoad;

    // Debugger
    CheckMenuItem freezeFrame;
    CheckMenuItem showOnlyRoads;
    Label mousePositionLabel = new Label();
    Label redrawTimerLabel = new Label();
    BorderPane debuggerOverlay;
    CheckMenuItem showDebugger;

    ArrayList<Address> POI = new ArrayList<Address>();
    Address[] routeAdrs = new Address[2];
    ArrayList<Highway> currentRoute = new ArrayList<Highway>();
    String routeInstrux = "";

    Node searchFromNode = null;
    Node searchToNode = null;
    Way path;
    String transportType = "car";

    // search elements
    ImageView serachLoopImage;
    ImageView findRouteImage;

    StackPane searchStackpan;

    Boolean search = false;
    VBox searchResultVBox;
    TextField searchTextField;
    Button favoritesButton;
    Button searchMenu;
    Button findRouteMenu;
    Boolean foundAddress = false;
    Button favoriteMenu;
    Button settingsMenu;
    StackPane settingStackPane;
    Button clearFavoritesButton;
    Button clearFindRouteButton;
    Button walkButton;
    Button bikeButton;
    Button carButton;

    StackPane findRouteStackPane;
    TextField findRouteFromTextField;
    TextField findRouteToTextField;
    VBox searchResultFromVBox;
    VBox searchResultToVBox;
    Button favoritesButton2;
    Button routeDescriptionButton;
    Button swapButton;

    StackPane favoriteStackPane;
    VBox favoritListVBox;

    StackPane routeDescriptionStackPane;
    Button routeDescriptionCloseButton;
    Button copyButton;
    HBox routeDescriptionHhox;
    VBox routeDescriptionInstruction;
    ScrollPane routeDescriptionScrollpane;

    MenuItem fileChoserMenu;
    Rectangle favoritBackgound;

    public View(Model model, Stage primaryStage) {
        gc.setFillRule(FillRule.EVEN_ODD);
        this.model = model;
        colors = new Colorscheme(); // Creates coclorscheme

        primaryStage.setTitle("GoKort");
        primaryStage.getIcons().add(new Image("file:logo.jpg"));

        BorderPane mapBoarderPane = new BorderPane();

        MenuBar menuBar = new MenuBar();

        Menu toolMenu = new Menu("Tools");

        freezeFrame = new CheckMenuItem("Freeze Canvas");
        showDebugger = new CheckMenuItem("Show DebuggerOverlay");
        showOnlyRoads = new CheckMenuItem("Show Only Roads");
        toolMenu.getItems().add(freezeFrame);
        toolMenu.getItems().add(showDebugger);
        toolMenu.getItems().add(showOnlyRoads);

        VBox debuggerVbox = new VBox();
        debuggerVbox.setLayoutX(200);
        debuggerVbox.setLayoutY(100);
        debuggerVbox.setAlignment(Pos.TOP_RIGHT);
        debuggerVbox.getChildren().add(mousePositionLabel);
        debuggerVbox.getChildren().add(redrawTimerLabel);
        debuggerOverlay = new BorderPane();
        debuggerOverlay.setMouseTransparent(true);
        debuggerOverlay.setVisible(false);
        debuggerOverlay.setCenter(debuggerVbox);

        // New layout ------------------------------------------------------------
        searchResultVBox = layout.getSearchResultVbox();
        searchTextField = layout.getTextField("Skriv adresse");

        serachLoopImage = layout.getImageView("file:searchLoop.png");
        findRouteImage = layout.getImageView("file:findRoute.png");
        searchMenu = layout.getButtonIcon(serachLoopImage, 20);
        favoritesButton = layout.getButtonIcon(layout.getImageView("file:starBlack.png"), 20);
        findRouteMenu = layout.getButtonIcon(findRouteImage, 20);
        favoriteMenu = layout.getButtonIcon(layout.getImageView("file:bookmark.png"), 20);
        settingsMenu = layout.getButtonIcon(layout.getImageView("file:settings.png"), 20);
        clearFavoritesButton = layout.getButtonIcon(layout.getImageView("file:clear.png"), 20);

        searchStackpan = layout.getStackPane(40, 340);
        searchStackpan.getChildren().addAll(searchTextField, searchResultVBox, favoritesButton);
        StackPane.setMargin(searchTextField, new Insets(0, 0, 0, 20));
        StackPane.setMargin(searchResultVBox, new Insets(40, 0, 0, 40));
        StackPane.setMargin(favoritesButton, new Insets(0, 0, 0, 300));

        carButton = layout.getButtonIcon(layout.getImageView("file:car.png"), 20);
        carButton.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
        bikeButton = layout.getButtonIcon(layout.getImageView("file:bike.png"), 20);
        walkButton = layout.getButtonIcon(layout.getImageView("file:walk.png"), 20);
        clearFindRouteButton = layout.getButtonIcon(layout.getImageView("file:clear.png"), 20);

        findRouteFromTextField = layout.getTextField("Find vej fra");
        findRouteToTextField = layout.getTextField("Find vej til");
        searchResultFromVBox = layout.getSearchResultVbox();
        searchResultToVBox = layout.getSearchResultVbox();
        favoritesButton2 = layout.getButtonIcon(layout.getImageView("file:starBlack.png"), 20);
        routeDescriptionButton = layout.getButtonIcon(layout.getImageView("file:description.png"), 20);
        swapButton = layout.getButtonIcon(layout.getImageView("file:swap.png"), 20);

        HBox veichleOption = new HBox(carButton, bikeButton, walkButton);
        veichleOption.setSpacing(10);
        veichleOption.setAlignment(Pos.BASELINE_CENTER);

        Shape box = layout.getRegtangle(130, 340);
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

        favoriteStackPane = layout.getStackPane(40, 340);
        favoritBackgound = layout.getRegtangle(40, 340);
        favoritListVBox = new VBox();

        favoriteStackPane.getChildren().addAll(favoritBackgound, favoritListVBox, clearFavoritesButton);
        StackPane.setMargin(favoritListVBox, new Insets(40, 0, 0, 0));
        StackPane.setMargin(clearFavoritesButton, new Insets(0, 0, 0, 300));

        Shape backgroundBox = layout.getRegtangle(280, 340);
        routeDescriptionHhox = new HBox();
        Label routeTitle = new Label("RuteVejledning");
        routeDescriptionCloseButton = layout.getButtonIcon(layout.getImageView("file:close.png"), 20);
        copyButton = layout.getButtonIcon(layout.getImageView("file:copy.png"), 20);

        routeDescriptionInstruction = layout.getSearchResultVbox();
        for (int i = 0; i < 100; i++) {
            Label label = new Label("hej");
            routeDescriptionInstruction.getChildren().add(label);

        }
        routeDescriptionScrollpane = layout.getScrollpane(routeDescriptionInstruction);

        routeDescriptionStackPane = layout.getStackPane(280, 340);
        routeDescriptionStackPane.getChildren().addAll(backgroundBox, routeDescriptionCloseButton, copyButton,
                routeTitle, routeDescriptionScrollpane);
        StackPane.setMargin(routeDescriptionCloseButton, new Insets(0, 0, 0, 300));
        StackPane.setMargin(routeTitle, new Insets(10, 0, 0, 40));
        StackPane.setMargin(routeDescriptionScrollpane, new Insets(40, 0, 20, 0));

        Menu fileMenu = new Menu("File");
        File file = new File("data");

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

        SeparatorMenuItem separator = new SeparatorMenuItem();
        fileMenu.getItems().add(separator);
        fileMenu.getItems().add(fileChoserMenu);
        menuBar.getMenus().addAll(fileMenu, colorMenu, toolMenu);

        settingStackPane = layout.getStackPane(40, 200);
        settingStackPane.getChildren().add(menuBar);
        StackPane.setMargin(menuBar, new Insets(10, 0, 0, 30));

        StackPane stackPane = new StackPane(mapBoarderPane, searchStackpan, settingStackPane, settingsMenu,
                favoriteStackPane, favoriteMenu, findRouteStackPane, routeDescriptionStackPane, findRouteMenu,
                searchMenu, debuggerOverlay);
        stackPane.setAlignment(Pos.TOP_LEFT);
        int buttonDistance = 45;
        // top, right, bottom, left
        StackPane.setMargin(searchMenu, new Insets(10, 0, 0, 10));
        StackPane.setMargin(searchStackpan, new Insets(10, 0, 0, 10));

        StackPane.setMargin(findRouteMenu, new Insets(buttonDistance + 10, 0, 0, 10));
        StackPane.setMargin(findRouteStackPane, new Insets(buttonDistance + 10, 0, 0, 10));
        StackPane.setMargin(routeDescriptionStackPane, new Insets(190, 0, 0, 10));

        StackPane.setMargin(favoriteStackPane, new Insets(buttonDistance * 2 + 10, 0, 0, 10));
        StackPane.setMargin(favoriteMenu, new Insets(buttonDistance * 2 + 10, 0, 0, 10));

        StackPane.setMargin(settingsMenu, new Insets(buttonDistance * 3 + 10, 0, 0, 10));
        StackPane.setMargin(settingStackPane, new Insets(buttonDistance * 3 + 10, 0, 0, 10));

        StackPane.setAlignment(debuggerOverlay, Pos.BOTTOM_LEFT);

        int scalebarLineStart = 5;
        int scalebarLineWidth = (int) (canvas.getHeight() * 0.1) + scalebarLineStart;
        int scalebarLineHight = 10;

        Line line1 = new Line(scalebarLineStart, scalebarLineHight / 2, scalebarLineWidth, scalebarLineHight / 2);
        Line line2 = new Line(scalebarLineStart, 0, scalebarLineStart, scalebarLineHight);
        Line line3 = new Line(scalebarLineWidth, 0, scalebarLineWidth, scalebarLineHight);
        Group scalebarLine = new Group(line1, line2, line3);

        scalebar = new Scalebar(model.maxlat - model.minlat, (float) (canvas.getHeight()),
                (float) (line1.getEndX() - line1.getStartX()));
        scaleLabel = new Label(scalebar.getScaleLabel());
        position = new Position(scalebar.getScale(), canvas, model);

        mapBoarderPane.setCenter(canvas);

        // Label with nearest road
        nearestRoad = new Label("Navnet på nærmeste vej");

        HBox scalebarHBox = new HBox(scalebarLine, scaleLabel, nearestRoad);
        scalebarHBox.setSpacing(10);
        scalebarHBox.setPadding(new Insets(0, 0, 0, 10));
        mapBoarderPane.setBottom(scalebarHBox);

        scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        redraw();
        pan((float) (-0.56 * model.minlon), model.maxlat);
        zoom(0, 0, (float) canvas.getHeight() / (model.maxlat - model.minlat));

    }

    void redraw() {
        long startTimer = System.currentTimeMillis();

        gc.setTransform(new Affine());
        // gc.setFill(colors.get("other")); //GAINSBORO
        gc.setFill(colors.get("background")); // GAINSBORO
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        float zoom = scalebar.getScale();

        if (!freezeFrame.isSelected()) {
            position.setCanvas();
        }

        if (zoom * canvasHeighScale * canvasWidthScale < 500) {
            gc.setFillRule(FillRule.NON_ZERO);
            for (Way way : model.firstLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            gc.setFillRule(FillRule.EVEN_ODD);
            for (Way way : model.thirdLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.secondLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.fithLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.smallRoadRtree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());

            }

            if (model.route.size() > 0) {
                gc.setStroke(Color.PURPLE);
                for (Edge edge : model.route) {
                    edge.draw(gc, colors, (float) trans.determinant());
                    gc.stroke();
                }
            }

        } else if (zoom * canvasHeighScale * canvasWidthScale < 2000) {
            gc.setFillRule(FillRule.NON_ZERO);
            for (Way way : model.firstLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            gc.setFillRule(FillRule.EVEN_ODD);
            for (Way way : model.secondLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.mediumRoadRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            if (model.route.size() > 0) {
                gc.setStroke(Color.PURPLE);
                for (Edge edge : model.route) {
                    edge.draw(gc, colors, (float) trans.determinant());
                    gc.stroke();
                }
            }

        } else if (zoom * canvasHeighScale * canvasWidthScale < 5000) {
            gc.setFillRule(FillRule.NON_ZERO);
            for (Way way : model.firstLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            gc.setFillRule(FillRule.EVEN_ODD);
            for (Way way : model.secondLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            for (Way way : model.mediumRoadRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            if (model.route.size() > 0) {
                gc.setStroke(Color.PURPLE);
                for (Edge edge : model.route) {
                    edge.draw(gc, colors, (float) trans.determinant());
                    gc.stroke();
                }
            }

        } else {
            gc.setFillRule(FillRule.NON_ZERO);
            for (Way way : model.firstLayerRTree.search(position.getCanvas())) {
                way.draw(gc, colors, (float) trans.determinant());
            }
            gc.setFillRule(FillRule.EVEN_ODD);

            if (model.route.size() > 0) {
                gc.setStroke(Color.PURPLE);
                for (Edge edge : model.route) {
                    edge.draw(gc, colors, (float) trans.determinant());
                    gc.stroke();
                }
            }

        }

        for (Way way : model.fourthLayerRTree.search(position.getCanvas())) {

            way.draw(gc, colors, (float) trans.determinant());
        }

        for (Way way : model.bigRoadRTree.search(position.getCanvas())) {

            way.draw(gc, colors, (float) trans.determinant());
        }
        if (model.route.size() > 0) {
            gc.setStroke(Color.PURPLE);
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
            gc.setFill(Color.RED);

            // gc.fillOval(searchFromNode.getLon()-radius, searchFromNode.getLat()-radius,
            // radius * 2, radius * 2);
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

    void pan(float dx, float dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    void zoom(float dx, float dy, float factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
        redraw();
    }
    
}