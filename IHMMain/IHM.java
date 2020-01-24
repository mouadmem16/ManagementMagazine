package IHMMain;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class IHM extends Application{
	private Scene scene;
	private BorderPane pane = new BorderPane();
	NavBar navbar = new NavBar();
	Footer footer = new Footer();
	FlowPane paneButtons = new FlowPane(80, 30);

	public static void main(String[] args) { launch(args); }

	@Override
	public void start(Stage window) throws Exception {
		window.setTitle("Magazine Management");
		navbar.titleLabel.textProperty().bind(window.titleProperty());
		pane.setTop(navbar.pane);
		pane.setBottom(footer.pane);
		paneButtons.setAlignment(Pos.CENTER);
		navbar.titleLabel.textProperty().bind(window.titleProperty());
		File file = new File(IHM.class.getResource("config.xml").getFile());
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList listModules = document.getElementsByTagName("Module");
			for(int i=0; i<listModules.getLength(); i++){
				Class<?> Module = Class.forName(listModules.item(i).getTextContent()+".IHM");
				Factories.ihm.IHMFactorie factorie = (Factories.ihm.IHMFactorie)Module.newInstance();
				paneButtons.getChildren().add(factorie.getImageViewWrapper());
				factorie.start();
				factorie.getImageViewWrapper().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				     @Override
				     public void handle(MouseEvent event) {
							pane.setCenter(factorie.getPrimaryPane());
							window.titleProperty().bind(factorie.getTitleProperty());
							window.setHeight(factorie.getHeightWindowProperty().get());
							window.setWidth(factorie.getWidthWindowProperty().get());
							navbar.InModule(pane, paneButtons, window);
				     }
				});
			}
		} catch (Exception e) {}

		pane.setCenter(paneButtons);
		scene = new Scene(pane);
		scene.getStylesheets().add(IHM.class.getResource("MyCss.css").toExternalForm());
		window.setMinHeight(500);
		window.setMinWidth(900);
		window.setScene(scene);
		window.show();
	}
}
