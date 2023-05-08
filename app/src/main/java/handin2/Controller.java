package handin2;

import java.util.ArrayList;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Controller {
    float lastX;
    float lastY;
    String transportType = "car";

    public Controller(Model model, View view) {
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
            view.mousePositionLabel.setText("lat: " + view.position.latPosition + " lon: " + view.position.lonPosition);

            if ((view.scalebar.getScale() * view.canvasHeighScale * view.canvasWidthScale) < 500) {
                if (!model.smallRoadRtree.isEmpty) {
                    view.nearestRoad.setText(((Highway) model.smallRoadRtree
                            .NNSearch(new float[] { (float) view.position.latPosition,
                                    (float) view.position.lonPosition }))
                            .toString());
                }
            } else if ((view.scalebar.getScale() * view.canvasHeighScale * view.canvasWidthScale) < 5000) {
                if (!model.mediumRoadRTree.isEmpty) {
                    view.nearestRoad.setText(((Highway) model.mediumRoadRTree
                            .NNSearch(new float[] { (float) view.position.latPosition,
                                    (float) view.position.lonPosition }))
                            .toString());
                }
            } else {
                if (!model.bigRoadRTree.isEmpty) {
                    view.nearestRoad.setText(((Highway) model.bigRoadRTree
                            .NNSearch(new float[] { (float) view.position.latPosition,
                                    (float) view.position.lonPosition }))
                            .toString());
                }
            }

        });

        view.scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                view.canvas.setWidth(view.canvas.getWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
                view.position.setnewCanvas(view.canvas);
                view.canvasWidthScale = newValue.doubleValue() / 640;
                view.redraw();
            }
        });
        view.scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                view.canvas.setHeight(view.canvas.getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
                view.position.setnewCanvas(view.canvas);
                view.canvasHeighScale = newValue.doubleValue() / 480;
                view.redraw();

            }
        });

        view.findRouteFromTextField.setOnMouseClicked(e -> {
            view.findRouteFromTextField.clear();
            view.searchFromNode = null;
            model.route.clear();
            view.redraw();
        });

        view.findRouteToTextField.setOnMouseClicked(e -> {
            view.findRouteToTextField.clear();
            view.searchToNode = null;
            model.route.clear();
            view.redraw();
        });
        view.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            view.searchResultVBox.getChildren().clear();
            view.favoritesButton.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

            setAdressOptionBox(view.searchTextField, view.searchResultVBox, "searchFromNode", newValue, model, view);

        });

        view.findRouteFromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            view.searchResultFromVBox.getChildren().clear();
            view.favoritesButton2.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

            setAdressOptionBox(view.findRouteFromTextField, view.searchResultFromVBox, "searchFromNode", newValue,
                    model, view);

        });

        view.findRouteToTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            view.searchResultToVBox.getChildren().clear();
            view.favoritesButton2.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

            setAdressOptionBox(view.findRouteToTextField, view.searchResultToVBox, "searchToNode", newValue, model,
                    view);

        });
        view.routeDescriptionButton.setOnMouseClicked(e -> {
            if (model.route.size() > 0) {
            applyButtonPressReleaseEffect(view.routeDescriptionButton, Color.LIGHTBLUE);
            view.routeDescriptionStackPane.setVisible(true);
            view.routeDescriptionStackPane.setMouseTransparent(false);
            
            getRouteDescription(model, view);
            }
        });

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
                    view.pan(view.position.panX, view.position.panY);
                    findRoute(model, view);
                    getRouteDescription(model,view);
                } else {
                    if(view.searchToNode != null) {
                    view.position.findPosition(view.searchToNode, view.searchFromNode);
                    } else {
                    view.position.findPosition(view.searchFromNode,view.searchToNode);
                    }
                    view.pan(view.position.panX, view.position.panY);
                }
            } 
        });

        view.favoritesButton.setOnMouseClicked(e -> {
            if (view.favoritesButton.getBackground().getFills().get(0).getFill().equals(Color.WHITE)
                    && view.searchFromNode != null) {

                view.favoritesButton.setBackground(new Background(new BackgroundFill(Color.GOLD, null, null)));
                VBox vBox = new VBox(new Label(view.searchTextField.getText()));
                Button button = view.layout.copyButton(view.searchMenu,
                        view.layout.copyImageView(view.serachLoopImage));

                addToFavorite(button, vBox, view, model, view.searchFromNode, null);
            }

        });

        view.favoritesButton2.setOnMouseClicked(e -> {
            if (view.favoritesButton2.getBackground().getFills().get(0).getFill().equals(Color.WHITE)
                    && view.searchFromNode != null && view.searchToNode != null) {
                view.favoritesButton2.setBackground(new Background(new BackgroundFill(Color.GOLD, null, null)));
                Label label1 = new Label(view.findRouteFromTextField.getText());
                Label label2 = new Label(view.findRouteToTextField.getText());
                VBox vBox = new VBox(label1, label2);
                Button button = view.layout.copyButton(view.findRouteMenu,
                        view.layout.copyImageView(view.findRouteImage));

                addToFavorite(button, vBox, view, model, view.searchFromNode, view.searchToNode);
            }
        });

        view.findRouteMenu.setOnMouseClicked(e -> {
            menuSelected(view.findRouteMenu, view.findRouteStackPane, view, model);
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
        view.copyButton.setOnAction(e -> {
            applyButtonPressReleaseEffect(view.copyButton, Color.LIGHTBLUE);

            ClipboardContent clipboardContent = new ClipboardContent();
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<String[]> ruteVejledning = model.pathfinder.getTextRoute();


            for (int i = 0; i < ruteVejledning.size(); i++) {
                String string = ruteVejledning.get(i)[0];
                strings.add(string);
            }

            clipboardContent.putString(String.join(System.lineSeparator(), strings));
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        });
        view.routeDescriptionCloseButton.setOnAction(e -> {
            view.routeDescriptionStackPane.setVisible(false);
            view.routeDescriptionStackPane.setMouseTransparent(true);
        });

        view.clearFavoritesButton.setOnMouseClicked(e -> {
            applyButtonPressReleaseEffect(view.clearFavoritesButton, Color.RED);
            view.favoritListVBox.getChildren().clear();
            view.favoritBackgound.setHeight(40);
        });

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
        view.carButton.setOnMouseClicked(e -> {
            veichleTypeSelected(view.carButton, view);
            transportType = "car";
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.redraw();
        });

        view.bikeButton.setOnMouseClicked(e -> {
            veichleTypeSelected(view.bikeButton, view);
            transportType = "bike";
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.redraw();
        });

        view.walkButton.setOnMouseClicked(e -> {
            transportType = "walk";
            veichleTypeSelected(view.walkButton, view);
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.redraw();
        });

        view.favoriteStackPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                view.favoritBackgound.setHeight(newValue.intValue());

            }

        });
        view.freezeFrame.setOnAction(e -> {
            if (view.freezeFrame.isSelected()) {
                view.position.getCanvas();
                view.redraw();
            } else {
                view.redraw();
            }
            ;
        });

        view.showDebugger.setOnAction(e -> {
            if (view.showDebugger.isSelected()) {
                view.debuggerOverlay.setVisible(true);
            } else {
                view.debuggerOverlay.setVisible(false);
            }

        });

    }
private void getRouteDescription(Model model, View view) {
    view.routeDescriptionInstruction.getChildren().clear();
    if(model.pathfinder.getTextRoute().size()>0) {
    ArrayList<String[]> ruteVejledning = model.pathfinder.getTextRoute();


    for (int i = 0; i < ruteVejledning.size(); i++) {
        Label label = new Label(ruteVejledning.get(i)[0]);
        view.routeDescriptionInstruction.getChildren().add(label);
    }
    }   
}

	
    private void setAdressOptionBox(TextField textField, VBox searchVBox, String searchNode, String newValue,
            Model model, View view) {
        if (newValue.length() > 0) {
            Iterable<String> keys = model.addresses.keysWithPrefix(newValue.toLowerCase());
            int numberOfDropdowns = 0;
            for (String key : keys) {
                Set<Address> setOfAdr = model.addresses.get(key);

                for (Address adr : setOfAdr) {
                    searchVBox.getChildren().add(createDropdownOptions(adr, textField, searchNode, view, model));
                    numberOfDropdowns++;
                    if (numberOfDropdowns > 5)
                        break;
                }
                if (numberOfDropdowns > 5)
                    break;
            }
        }
    }

    private Label createDropdownOptions(Address adr, TextField textField, String searchNode, View view, Model model) {
        Label label = view.layout.getAdressLabel(adr, 260);
        label.setOnMouseClicked(e -> {
            setOnClickEventSerachResult(searchNode, textField, adr, label, view, model);
        });
        return label;
    }

    private void setOnClickEventSerachResult(String searchNode, TextField textField, Address adr, Label label,
            View view, Model model) {
        textField.setText(label.getText());
        if (searchNode.equals("searchFromNode")) {
            view.searchFromNode = new Node(adr.getLat(), adr.getLon(), 1);
        } else {
            view.searchToNode = new Node(adr.getLat(), adr.getLon(), 1);
        }

        view.position.findPosition(view.searchFromNode, view.searchToNode);
        view.pan(view.position.panX, view.position.panY);

        if (view.searchFromNode != null && view.searchToNode != null) {
            findRoute(model, view);
            view.redraw();
            getRouteDescription(model,view);
        }
    }

    private void findRoute(Model model, View view) {
        if (transportType.equals("car")) {
            Edge fromEdge = (Edge) model.edgeTreeCar
                    .NNSearch(new float[] { view.searchFromNode.lat, view.searchFromNode.lon });
            Edge toEdge = (Edge) model.edgeTreeCar
                    .NNSearch(new float[] { view.searchToNode.lat, view.searchToNode.lon });
            model.route = model.pathfinder.findPathCar(model.vertexMap.get(fromEdge.toID),
                    model.vertexMap.get(toEdge.toID));
        } else if (transportType.equals("bike") | transportType.equals("walk")) {
            Edge fromEdge = (Edge) model.edgeTreeBike
                    .NNSearch(new float[] { view.searchFromNode.lat, view.searchFromNode.lon });
            Edge toEdge = (Edge) model.edgeTreeBike
                    .NNSearch(new float[] { view.searchToNode.lat, view.searchToNode.lon });
            model.route = model.pathfinder.findPathBike(model.vertexMap.get(fromEdge.toID),
                    model.vertexMap.get(toEdge.toID));
        }
    }

    private void veichleTypeSelected(Button viechleType, View view) {
        view.carButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        view.bikeButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        view.walkButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        viechleType.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    private void addToFavorite(Button button, VBox vBox, View view, Model model, Node searchFromNode,
            Node searchToNode) {
        button.setOnMouseClicked(i -> {
            view.searchFromNode = searchFromNode;
            view.searchToNode = searchToNode;
            if (view.searchFromNode != null && view.searchToNode != null) {
                findRoute(model, view);
                getRouteDescription(model,view);
            }
            view.position.findPosition(view.searchFromNode, view.searchToNode);
            view.pan(view.position.panX, view.position.panY);

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

    private void applyButtonPressReleaseEffect(Button button, Color wantedColor) {
        button.setOnMousePressed(e -> {
            button.setBackground(new Background(new BackgroundFill(wantedColor, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        button.setOnMouseReleased(e -> {
            button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        });
    }
}