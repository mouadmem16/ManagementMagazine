package Login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class IHM extends Factories.ihm.IHMFactorie{
	{
		//widthWindow;
		//heightWindow;
		imgModule = null;
		primaryPane = new GridPane();
		title.set("Login Magazine");
	}

    @Override
    public void start() {
        ((GridPane)primaryPane).setAlignment(Pos.CENTER);
        ((GridPane)primaryPane).setHgap(10);
        ((GridPane)primaryPane).setVgap(10);
        ((GridPane)primaryPane).setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setId("welcome-text");
        ((GridPane)primaryPane).add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        ((GridPane)primaryPane).add(userName, 0, 1);

        TextField userTextField = new TextField();
        ((GridPane)primaryPane).add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        ((GridPane)primaryPane).add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        ((GridPane)primaryPane).add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        ((GridPane)primaryPane).add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        ((GridPane)primaryPane).add(actiontarget, 1, 6);
        actiontarget.setId("actiontarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setText("Sign in button pressed");
            }
        });

//        scene.getStylesheets().add(Login.class.getResource("Login.css").toExternalForm());
//        primaryPane.getScene().getStylesheets().add(IHM.class.getResource("MyCss.css").toExternalForm());
    }
}
