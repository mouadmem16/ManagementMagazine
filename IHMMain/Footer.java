package IHMMain;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class Footer {
    Label copyRight = new Label("Copyright 2019 Nouha & Mouaad");
    BorderPane pane = new BorderPane();

    public Footer(){
        pane.setCenter(copyRight);
        pane.setId("footer-section");
    }
}
