package GoKort;

import java.util.ArrayList;
import java.util.Set;

import GoKort.Objects.Highway;
import GoKort.Objects.Node;
import GoKort.Pathfinding.Address;
import GoKort.Pathfinding.Edge;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {
    float lastX;
    float lastY;

    public Controller(Model model, View view) {
        //used for drag calculations
        view.canvas.setOnMousePressed(e -> {
            lastX = (float) e.getX();
            lastY = (float) e.getY();
        });

        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) { // LEFT MOUSEBUTTON
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.position.dragged(dx, dy);

                view.pan(dx, dy); // MOVE THE MAP

            }
            lastX = (float) e.getX();
            lastY = (float) e.getY();

        });

        view.canvas.setOnScroll(e -> {

            // Input on scroll
            float factor = (float) e.getDeltaY();

            // determines how fast zoom in on input
            float zoomSpeed = (float) (Math.pow(1.01, factor));

            // Does the zoom and set the label
            zoomSpeed = view.scalebar.setScale(zoomSpeed);
            view.zoom(e.getX(), e.getY(), zoomSpeed);
            view.scaleLabel.setText(view.scalebar.getScaleLabel());

            view.position.setNewScale(view.scalebar.getScale());
            view.position.mouseZoom(e.getX(), e.getY());

        });

        view.canvas.setOnMouseMoved(e -> {
            view.position.setPosition(e.getX(), e.getY());
            view.mousePositionLabel.setText("lat: " + view.position.getLatPosition() + " lon: " + view.position.getLonPosition());

            //this calculates how far in you need to be zoomed for stuff to be drawn. If the canvas is bigger, the more you need to zoon in. 
            //It finds the the new nearest visible road, by searching the r-Tree with NNSearch. 
            if ((view.scalebar.getScale() * view.canvasHeighScale * view.canvasWidthScale) < 500) {
                if (!model.smallRoadRtree.getIsEmpty()) {
                    view.nearestRoad.setText(((Highway) model.smallRoadRtree
                            .NNSearch(new float[] { (float) view.position.getLatPosition(),
                                    (float) view.position.getLonPosition() }))
                            .toString());
                }
            } else if ((view.scalebar.getScale() * view.canvasHeighScale * view.canvasWidthScale) < 5000) {
                if (!model.mediumRoadRTree.getIsEmpty()) {
                    view.nearestRoad.setText(((Highway) model.mediumRoadRTree
                            .NNSearch(new float[] { (float) view.position.getLatPosition(),
                                    (float) view.position.getLonPosition() }))
                            .toString());
                }
            } else {
                if (!model.bigRoadRTree.getIsEmpty()) {
                    view.nearestRoad.setText(((Highway) model.bigRoadRTree
                            .NNSearch(new float[] { (float) view.position.getLatPosition(),
                                    (float) view.position.getLonPosition() }))
                            .toString());
                }
            }

        });

        //when the scene is expanded, the canvas expands propotrinally 
        view.scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                view.canvas.setWidth(view.canvas.getWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
                view.position.setnewCanvas(view.canvas);
                view.canvasWidthScale = newValue.doubleValue() / 640;
                view.redraw();
            }
        });
        
        //when the scene is expanded, the canvas expands propotrinally 
        view.scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                view.canvas.setHeight(view.canvas.getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
                view.position.setnewCanvas(view.canvas);
                view.canvasHeighScale = newValue.doubleValue() / 480;
                view.redraw();

            }
        });

        //used for the search-bar for finding route from this startpoint
        view.findRouteFromTextField.setOnMouseClicked(e -> {
            view.findRouteFromTextField.clear();
            view.searchFromNode = null;
            model.route.clear();
            view.redraw();
        });

        //used for the search-bar for finding route a route to this endpoint
        view.findRouteToTextField.setOnMouseClicked(e -> {
            view.findRouteToTextField.clear();
            view.searchToNode = null;
            model.route.clear();
            view.redraw();
        });

        //used for the search-bar for finding a single point
        view.searchTextField.setOnMouseClicked(e -> {
            view.searchTextField.clear();
            view.searchFromNode = null;
            view.redraw();
        });

        //When typing in the searchbar, it updates the search Result, and clears the favorites
        view.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            view.searchResultVBox.getChildren().clear();
            view.favoritesButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

            setAdressOptionBox(view.searchTextField, view.searchResultVBox, "searchFromNode", newValue, model, view);

        });

        //When typing in the searchbar, it updates the search Result, and clears the favorites
        view.findRouteFromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            view.searchResultFromVBox.getChildren().clear();
            view.favoritesButton2.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

            setAdressOptionBox(view.findRouteFromTextField, view.searchResultFromVBox, "searchFromNode", newValue,
                    model, view);

        });

        //When typing in the searchbar, it updates the search Result, and clears the favorites
        view.findRouteToTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            view.searchResultToVBox.getChildren().clear();
            view.favoritesButton2.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

            setAdressOptionBox(view.findRouteToTextField, view.searchResultToVBox, "searchToNode", newValue, model,
                    view);

        });

        //if route has been found, this button can activate to show the route instructions
        view.routeDescriptionButton.setOnMouseClicked(e -> {
            if (model.route.size() > 0) {
            applyButtonPressReleaseEffect(view.routeDescriptionButton, Color.LIGHTBLUE);
            view.routeDescriptionStackPane.setVisible(true);
            view.routeDescriptionStackPane.setMouseTransparent(false);
            
            getRouteDescription(model, view);
            }
        });

        //swaps the to-and-from points and checks if the From- or To- points are null, to do the appropiate actions.
        view.swapButton.setOnMouseClicked(e -> {
            if (view.searchFromNode != null || view.searchToNode != null) {
                applyButtonPressReleaseEffect(view.swapButton, Color.LIGHTBLUE);
                String temp = view.findRouteFromTextField.getText();
                Node tempNode = view.searchFromNode;

                view.findRouteFromTextField.setText(view.findRouteToTextField.getText());
                view.searchFromNode = view.searchToNode;

                view.findRouteToTextField.setText(temp);
                view.searchToNode = tempNode;

                if (view.searchFromNode != null && view.searchToNode != null) {
                    view.position.findPosition(view.searchFromNode, view.searchToNode);
                    view.pan(view.position.getPanX(), view.position.getPanY());
                    findRoute(model, view);
                    view.redraw();   //Maybe not
                    getRouteDescription(model,view);
                } else {
                    if(view.searchToNode != null) {
                    view.position.findPosition(view.searchToNode, view.searchFromNode);
                    } else {
                    view.position.findPosition(view.searchFromNode,view.searchToNode);
                    }
                    view.pan(view.position.getPanX(), view.position.getPanY());
                }
            } 
        });

        //adds a point to favorite list, with the adress information, and only if from- is found and not already marked
        view.favoritesButton.setOnMouseClicked(e -> {
            if (view.favoritesButton.getBackground().getFills().get(0).getFill().equals(Color.WHITE)
                    && view.searchFromNode != null) {

                view.favoritesButton.setBackground(new Background(new BackgroundFill(Color.GOLD, null, null)));
                VBox vBox = new VBox(new Label(view.searchTextField.getText()));
                Button button = view.layout.copyButton(view.searchMenu,
                        view.layout.copyImageView(view.serachLoopImage));

                addToFavorite(button, vBox, view, model, view.searchFromNode, null, null);
            }

        });

        //adds a route to favorite list, with the adress information, and only if to- and from- is found and not already marked
        view.favoritesButton2.setOnMouseClicked(e -> {
            if (view.favoritesButton2.getBackground().getFills().get(0).getFill().equals(Color.WHITE)
                    && view.searchFromNode != null && view.searchToNode != null) {
                view.favoritesButton2.setBackground(new Background(new BackgroundFill(Color.GOLD, null, null)));
                Label label1 = new Label(view.findRouteFromTextField.getText());
                Label label2 = new Label(view.findRouteToTextField.getText());
                VBox vBox = new VBox(label1, label2);
                Button button = view.layout.copyButton(view.findRouteMenu,
                        view.layout.copyImageView(view.findRouteImage));

                addToFavorite(button, vBox, view, model, view.searchFromNode, view.searchToNode, view.transportType);
            }
        });

        //this button clears the list of favorites destinations and routes
        view.clearFavoritesButton.setOnMouseClicked(e -> {
            applyButtonPressReleaseEffect(view.clearFavoritesButton, Color.RED);
            view.favoritListVBox.getChildren().clear();
            view.favoritBackgound.setHeight(40);
        });

        //this button clears the searchbars in finding a route to reset the search.
        view.clearFindRouteButton.setOnAction(e -> {
            applyButtonPressReleaseEffect(view.clearFindRouteButton, Color.RED);
            view.findRouteFromTextField.clear();
            view.findRouteToTextField.clear();
            view.favoritesButton2
                    .setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            view.searchFromNode = null;
            view.searchToNode = null;
            model.route.clear();
            view.redraw();
        });

        //button for veichle selection, chooses the color of route and how to calculate the fastest route
        view.carButton.setOnMouseClicked(e -> {
            veichleTypeSelected(view.carButton, view, Color.MAGENTA);
            view.transportType = "car";
            model.pathfinder.setType("car");
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.redraw();
        });
        view.bikeButton.setOnMouseClicked(e -> {
            veichleTypeSelected(view.bikeButton, view, Color.TOMATO);
            view.transportType = "bike";
            model.pathfinder.setType("bike");
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.redraw();
        });
        view.walkButton.setOnMouseClicked(e -> {
            view.transportType = "walk";
            model.pathfinder.setType("walk");

            veichleTypeSelected(view.walkButton, view, Color.CYAN);
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.redraw();
        });

        //hides the other menues when selectig another or the an already selected menu
        view.findRouteMenu.setOnMouseClicked(e -> {
            menuSelected(view.findRouteMenu, view.findRouteStackPane, view, model);
            view.transportType = "car";
            model.pathfinder.setType("car");

        });

        view.favoriteMenu.setOnMouseClicked(e -> {
            menuSelected(view.favoriteMenu, view.favoriteStackPane, view, model);
        });

        view.settingsMenu.setOnMouseClicked(e -> {
            menuSelected(view.settingsMenu, view.settingStackPane, view, model);
        });
        view.searchMenu.setOnMouseClicked(e -> {
            menuSelected(view.searchMenu, view.searchStackpan, view, model);
        });

        
        //Copies the route-instructions to the clip-holder. Clipboard, clipboard content and String.join was made with help from Chat-GPT
        view.copyButton.setOnAction(e -> {
            applyButtonPressReleaseEffect(view.copyButton, Color.LIGHTBLUE);

            ClipboardContent clipboardContent = new ClipboardContent();
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<ArrayList<String>> ruteVejledning = model.pathfinder.getTextRoute();


            for (int i = 0; i < ruteVejledning.size(); i++) {
                String string = ruteVejledning.get(i).get(0);
                strings.add(string);
            }

            clipboardContent.putString(String.join(System.lineSeparator(), strings));
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        });
        
        //hides the route intructions when pressed
        view.routeDescriptionCloseButton.setOnAction(e -> {
            view.routeDescriptionStackPane.setVisible(false);
            view.routeDescriptionStackPane.setMouseTransparent(true);
        });
        
        //expands the background of the favorite list, when adding more elements, just visually pleasing
        view.favoriteStackPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                view.favoritBackgound.setHeight(newValue.intValue());

            }
        });

        //freezes the canvas in view at the point of pressed and makes a red outline.
        view.freezeFrame.setOnAction(e -> {
            if (view.freezeFrame.isSelected()) {
                view.position.getCanvas();
                view.redraw();
            } else {
                view.redraw();
            }
        });

        //makes the debugger overlay visible when pressed. 
        view.showDebugger.setOnAction(e -> {
            if (view.showDebugger.isSelected()) {
                view.debuggerOverlay.setVisible(true);
            } else {
                view.debuggerOverlay.setVisible(false);
            }

        });

    }

    //creates the routedescription with icons, when a route is found.
    private void getRouteDescription(Model model, View view) {
        view.routeDescriptionInstruction.getChildren().clear();
        if(model.pathfinder.getTextRoute().size()>0) {
            ArrayList<ArrayList<String>> ruteVejledning = model.pathfinder.getTextRoute();
            view.routeInfoHBox.getChildren().clear();

            view.routeInfoHBox.getChildren().add(new Label(model.pathfinder.getTravelTime()));
            view.routeInfoHBox.getChildren().add(new Label(model.pathfinder.getTravelLength()));
            view.routeInfoHBox.setSpacing(40);


            for (int i = 0; i < ruteVejledning.size(); i++) {

                Text text = new Text(ruteVejledning.get(i).get(0));
                TextFlow textFlow = new TextFlow(text);
                textFlow.setPrefWidth(300); 

                ImageView imageView = new ImageView();
                 if(ruteVejledning.get(i).get(1).equals("venstre")) {
                    imageView = view.layout.copyImageView(view.turnLeftImage);
                } 
                else if (ruteVejledning.get(i).get(1).equals("højre")) {
                    imageView = view.layout.copyImageView(view.turnRightImage);
                } 
                HBox hBox = new HBox(textFlow, imageView);
                view.routeDescriptionInstruction.getChildren().add(hBox);
                view.routeDescriptionInstruction.setSpacing(10);
                view.routeDescriptionInstruction.setAlignment(Pos.CENTER_LEFT);
            }
        }   
    }

	//when typing in the searchbar, this method looks for adress matches and creates no-more than 5 options
    private void setAdressOptionBox(TextField textField, VBox searchVBox, String searchNode, String newValue,
            Model model, View view) {
        if (newValue.length() > 0) {
            Iterable<String> keys = model.addresses.keysWithPrefix(newValue.toLowerCase());
            int numberOfDropdowns = 0;
            
            for (String key : keys) {
                    searchVBox.getChildren().add(createDropdownOptions(model.addresses.get(key), textField, searchNode, view, model));
                    numberOfDropdowns++;
                if (numberOfDropdowns > 5)
                    break;
            }
        }
    }

    //Creates the different search-result labels
    private Label createDropdownOptions(Address adr, TextField textField, String searchNode, View view, Model model) {
        Label label = view.layout.getAdressLabel(adr, 260);
        label.setOnMouseClicked(e -> {
            setOnClickEventSerachResult(searchNode, textField, adr, label, view, model);
        });
        return label;
    }

    //sets up the on action-event for the searchresults labels
    private void setOnClickEventSerachResult(String searchNode, TextField textField, Address adr, Label label,
            View view, Model model) {
        textField.setText(label.getText());
        if (searchNode.equals("searchFromNode")) {
            view.searchFromNode = new Node(adr.getLat(), adr.getLon(), 1);
        } else {
            view.searchToNode = new Node(adr.getLat(), adr.getLon(), 1);
        }

        view.position.findPosition(view.searchFromNode, view.searchToNode);
        view.pan(view.position.getPanX(), view.position.getPanY());

        if (view.searchFromNode != null && view.searchToNode != null) {
            findRoute(model, view);
            view.redraw();
            getRouteDescription(model,view);
        }
    }

    //when searching for a route, this method is called. 
    private void findRoute(Model model, View view) {
        if (view.transportType.equals("car")) {
            Edge fromEdge = (Edge) model.edgeTreeCar
                    .NNSearch(new float[] { view.searchFromNode.getLat(), view.searchFromNode.getLon() });
            Edge toEdge = (Edge) model.edgeTreeCar
                    .NNSearch(new float[] { view.searchToNode.getLat(), view.searchToNode.getLon() });
            model.route = model.pathfinder.findPathCar(model.vertexMap.get(fromEdge.getFromID()),
                    model.vertexMap.get(toEdge.getToID()));
        } else if (view.transportType.equals("bike") || view.transportType.equals("walk")) {
            Edge fromEdge = (Edge) model.edgeTreeBike
                    .NNSearch(new float[] { view.searchFromNode.getLat(), view.searchFromNode.getLon() });
            Edge toEdge = (Edge) model.edgeTreeBike
                    .NNSearch(new float[] { view.searchToNode.getLat(), view.searchToNode.getLon() });
            model.route = model.pathfinder.findPathBike(model.vertexMap.get(fromEdge.getFromID()),
                    model.vertexMap.get(toEdge.getToID()));
        }
    }

    //setting the pressed viechle type button as shown avtive and sets sets the others to default color. 
    private void veichleTypeSelected(Button viechleType, View view, Color color) {
        view.carButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        view.bikeButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        view.walkButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        viechleType.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    //creates the favorite routes and destinations, and when pressed shows them on the map.
    private void addToFavorite(Button button, VBox vBox, View view, Model model, Node searchFromNode,
            Node searchToNode, String veichleType) {
        button.setOnMouseClicked(i -> {
            model.route.clear();
            view.transportType = veichleType;
            view.searchFromNode = searchFromNode;
            view.searchToNode = searchToNode;
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.position.findPosition(view.searchFromNode, view.searchToNode);
            view.pan(view.position.getPanX(), view.position.getPanY());

        });

        StackPane stackPane = new StackPane(vBox, button);
        StackPane.setAlignment(vBox, Pos.CENTER_LEFT);
        StackPane.setAlignment(button, Pos.TOP_RIGHT);
        StackPane.setMargin(vBox, new Insets(0, 0, 0, 10));
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setMaxWidth(340);
        view.favoritListVBox.getChildren().addAll(separator, stackPane);

    }

    //hides the other menues when selectig another or the an already selected menu
    private void menuSelected(Button button, StackPane stackPane, View view, Model model) {
        if (button.getBackground().getFills().get(0).getFill().equals(Color.LIGHTGREY)) {
            button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            stackPane.setVisible(false);
            stackPane.setMouseTransparent(true);

        } else {
            view.searchMenu.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            view.searchStackpan.setVisible(false);
            view.searchStackpan.setMouseTransparent(true);
            view.searchTextField.clear();
            view.favoritesButton
                    .setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

            view.findRouteMenu.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            view.findRouteStackPane.setVisible(false);
            view.findRouteStackPane.setMouseTransparent(true);
            view.findRouteFromTextField.clear();
            view.findRouteToTextField.clear();
            view.favoritesButton2
                    .setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

            view.favoriteMenu.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            view.favoriteStackPane.setVisible(false);
            view.favoriteStackPane.setMouseTransparent(true);

            view.settingsMenu.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            view.settingStackPane.setVisible(false);
            view.settingStackPane.setMouseTransparent(true);

            button.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
            stackPane.setVisible(true);
            stackPane.setMouseTransparent(false);

            view.routeDescriptionStackPane.setVisible(false);
            view.routeDescriptionStackPane.setMouseTransparent(true);

            view.searchFromNode = null;
            view.searchToNode = null;
            model.route.clear();
            view.redraw();
        }
    }

    //creates a button-press effect for buttons that are not selectable
    private void applyButtonPressReleaseEffect(Button button, Color wantedColor) {
        button.setOnMousePressed(e -> {
            button.setBackground(new Background(new BackgroundFill(wantedColor, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        button.setOnMouseReleased(e -> {
            button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        });
    }
}