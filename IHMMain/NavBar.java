package IHMMain;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class NavBar {
    BorderPane pane = new BorderPane();
    Label titleLabel = new Label();
    Button retour = new Button();
    ImageView icon = new ImageView(NavBar.class.getResource("retour.png").toExternalForm());

    public NavBar(){
        pane.getStyleClass().add("header-section");
        titleLabel.setId("header-text");
        retour.setId("account");

        pane.setRight(titleLabel);
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        retour.setText("Retour");
        retour.setGraphic(icon);
    }

    public void InModule(BorderPane Rootpane, FlowPane childpane, Stage window){
        pane.setLeft(retour);
        retour.setOnMouseClicked(event -> {
            Rootpane.setCenter(childpane);
            window.titleProperty().unbind();
            window.setTitle("Magazine Management");
            titleLabel.textProperty().bind(window.titleProperty());
            pane.setLeft(null);
        });
    }



}
