package handin2;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        String filename = "data/fyn.osm.zip";

        // String filename = "data/map.osm";

        var model = Model.load(filename);
        var view = new View(model, primaryStage);
        new Controller(model, view);

    }

}