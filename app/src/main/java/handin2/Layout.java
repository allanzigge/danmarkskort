package handin2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

//This class is a helper class for View. It helps create, copy, and size elements in View
public class Layout {
    int boxWidth = 300;
    int circleRadius = 20;

    public Layout() {
      

    }

    public Button getButtonIcon(ImageView imageView, int size) {
       
        Button button = new Button("", imageView);
        button.setPadding(new Insets(size/2, size/2, size/2, size/2));
        button.setShape(new Circle(size));
        button.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
        button.setBorder(new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return button;
    }
    public Button copyButton(Button button, ImageView imageView) {
        Button newButton = new Button("", imageView);
        newButton.setPadding(button.getPadding());
        newButton.setShape(button.getShape());
        newButton.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
        newButton.setBorder(button.getBorder());
        return newButton;
    }
    
    public ImageView getImageView(String path) {
        ImageView imageView = new ImageView(path);
        imageView.setFitHeight(circleRadius);
        imageView.setFitWidth(circleRadius);

        return imageView;

    }
    public ImageView copyImageView(ImageView imageView) {
        ImageView newImageView = new ImageView(imageView.getImage());
        newImageView.setFitHeight(imageView.getFitHeight());
        newImageView.setFitWidth(imageView.getFitHeight());

        return newImageView;

    }

    public TextField getTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
        textField.setBorder(new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        textField.setMaxWidth(300);
        textField.setMaxHeight(circleRadius*2);
        textField.setPadding(new Insets(0, 0, 0, circleRadius));
        textField.setMaxHeight(40);
        return textField;
    }

    public VBox getSearchResultVbox() {
        VBox vBox = new VBox();
        vBox.setMaxHeight(0);
        
        
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));

        return vBox;  
    }

    public Label getAdressLabel(Address address, int width) {
        Label label = new Label(address.getStreet()+ " " + address.getAdrNum()+address.getAdrLet()+", " + address.getPostcode() + " " + address.getCity());
        label.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
        label.setMaxWidth(width);
        return label;
    }

    public StackPane getStackPane(int height, int width) {
        StackPane stackPane = new StackPane();
        stackPane.setMaxHeight(height);
        stackPane.setMaxWidth(width);
        stackPane.setVisible(false);
        stackPane.setMouseTransparent(true);
        stackPane.setAlignment(Pos.TOP_LEFT);
        return stackPane;
    }

    public Rectangle getRegtangle(int height, int width) {
        Rectangle r = new Rectangle();
        r.setX(width);
        r.setY(height);
        r.setWidth(width);
        r.setHeight(height);
        r.setArcWidth(40);
        r.setArcHeight(40);
        r.setFill(Color.WHITE);
        return r;

    }

}
