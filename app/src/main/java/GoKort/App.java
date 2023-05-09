package GoKort;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        String filename = "data/denmark-latest.osm.zip.obj";

        var model = new Model(filename);
        var view = new View(model, primaryStage);
        new Controller(model, view);

    }

}