package Factories.ihm;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

abstract public class IHMFactorie{
	protected IntegerProperty widthWindow = new SimpleIntegerProperty(900);
	protected IntegerProperty heightWindow = new SimpleIntegerProperty(500);
	protected StringProperty title = new SimpleStringProperty("Gestion Magazine");
	protected ImageView imgModule;
	protected Pane primaryPane;
	protected BorderPane imageViewWrapper;

	{
		Image img=null;
		try {
			img = new Image(new FileInputStream(IHMFactorie.class.getResource("home.png").getPath()));
		} catch (FileNotFoundException e) {}
		imgModule = new ImageView(img);	
		imgModule.fitHeightProperty().bind(heightWindow.divide(8).add(50));
		imgModule.fitWidthProperty().bind(widthWindow.divide(8).add(50));
		initBorderImage();
	}

	public void initBorderImage(){
		imageViewWrapper = new BorderPane(imgModule);
		imageViewWrapper.getStyleClass().add("image-view-wrapper");
	}

	
	public double verifyNumber(String key){
		try{
			double a = Double.parseDouble(key);
			return a;
		}catch(Exception a){
			return 0;
		}
	}
	
	public ImageView getImgModule() {
		return imgModule;
	}

	public void setImgModule(ImageView imgModule) {
		this.imgModule = imgModule;
	}
	
	public StringProperty getTitleProperty() {
		return title;
	}

	public void setTitleProperty(String title) {
		this.title.set(title);
	}
	
	public IntegerProperty getWidthWindowProperty() {
		return widthWindow;
	}

	public void setWidthWindowProperty(int widthWindow) {
		this.widthWindow.set(widthWindow);
	}

	public IntegerProperty getHeightWindowProperty() {
		return heightWindow;
	}

	public void setHeightWindowProperty(int heightWindow) {
		this.heightWindow.set(heightWindow);
	}

	public Pane getPrimaryPane() {
		return primaryPane;
	}

	public void setPrimaryPane(Pane primaryPane) {
		this.primaryPane = primaryPane;
	}

	public BorderPane getImageViewWrapper() {
		return imageViewWrapper;
	}

	public void setImageViewWrapper(BorderPane imageViewWrapper) {
		this.imageViewWrapper = imageViewWrapper;
	}

	public abstract void start();
}
