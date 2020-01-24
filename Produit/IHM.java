package Produit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class IHM extends Factories.ihm.IHMFactorie{
	ComboBox CategorieComboBox = new ComboBox();
	Categorie.DAO catdao = new Categorie.DAO();
	List<Categorie.Model> cats = catdao.retrieveAll();
	
	{
		widthWindow.setValue(1100);
		//heightWindow;
		//title;
		primaryPane = new BorderPane();
		Image img=null;
		try {
			img = new Image(new FileInputStream(IHM.class.getResource("home.png").getPath()));
		} catch (FileNotFoundException e) {}
		imgModule.setImage(img);
		imgModule.resize(200, 200);
		title.set("Produit Management");
		
		for (Categorie.Model cat : cats) {
			CategorieComboBox.getItems().add(cat.toString());
		}
	}
	
	private Produit.DAO prddao = new Produit.DAO();
	private List<Produit.Model> list = prddao.retrieveAll();
	private ObservableList<Produit.Model> data = FXCollections.observableArrayList(list);
	
	GridPane grid = new GridPane();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	
	Label labelId = new Label("ID : ");
	Label labelDesignation = new Label("Designation : ");
	Label labelPrixAchat = new Label("PrixAchat : ");
	Label labelCategorie = new Label("Categorie : ");
	Label labelPrixVente = new Label("PrixVente : ");

	TextField txtId = new TextField();
	TextField txtDesignation = new TextField();
	TextField txtPrixAchat = new TextField();
//	TextField txtCategorie = new TextField();
	TextField txtPrixVente = new TextField();

	TextField txtSearch = new TextField();
	TableView<Produit.Model> table = new TableView<Produit.Model>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("RESET FORM");
	Button btnSupprimer = new Button("Supprimer");	
	Button gestionCat = new Button("Categorie");	

	int indexOfTable=-1;
	private void initTable() {
//		table.setEditable(true);
		table.setItems(data);
		table.autosize();
		table.prefHeightProperty().bind(primaryPane.heightProperty());
		table.prefWidthProperty().bind(primaryPane.widthProperty().multiply(0.5));
		
		
		TableColumn<Produit.Model, Integer> colId = new TableColumn<Produit.Model, Integer>("ID");
		colId.prefWidthProperty().bind(table.widthProperty().multiply(0.08));
		colId.setCellValueFactory(new PropertyValueFactory<Produit.Model, Integer>("id"));
		table.getColumns().add(colId);

		TableColumn<Produit.Model, String> colDesignation = new TableColumn<Produit.Model, String>("Designation");
		colDesignation.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		colDesignation.setCellValueFactory(new PropertyValueFactory<Produit.Model, String>("designation"));
		table.getColumns().add(colDesignation);
		
		TableColumn<Produit.Model, String> colPrixAchat = new TableColumn<Produit.Model, String>("PrixAchat");
		colPrixAchat.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
		colPrixAchat.setCellValueFactory(new PropertyValueFactory<Produit.Model, String>("PrixAchat"));
		table.getColumns().add(colPrixAchat);

		TableColumn<Produit.Model, String> colPrixVente = new TableColumn<Produit.Model, String>("PrixVente");
		colPrixVente.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
		colPrixVente.setCellValueFactory(new PropertyValueFactory<Produit.Model, String>("PrixVente"));
		table.getColumns().add(colPrixVente);
		
		TableColumn<Produit.Model, String> colCategorie = new TableColumn<Produit.Model, String>("Categorie");
		colCategorie.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		colCategorie.setCellValueFactory(new PropertyValueFactory<Produit.Model, String>("Categorie"));
		table.getColumns().add(colCategorie);
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		        if(table.getSelectionModel().getSelectedItem() != null) 
		        {
		        	indexOfTable = table.getSelectionModel().getSelectedIndex();
		        	resetTextFields();
		        	Produit.Model p = table.getSelectionModel().getSelectedItem();
		        	txtPrixAchat.setText(String.valueOf(p.getPrixAchat()));
		        	txtId.setText(String.valueOf(p.getId()));
		        	txtDesignation.setText(p.getDesignation());
		        	txtPrixVente.setText(String.valueOf(p.getPrixVente()));
		        	CategorieComboBox.setValue(p.getCategorie().getIntitule());
		        }
			 }
	     });
		
		txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String txt = txtSearch.getText();
				Predicate<Produit.Model> design = i -> i.getDesignation().toLowerCase().contains(txt.toLowerCase());
				Predicate<Produit.Model> categorie = i -> i.getCategorie().getIntitule().toLowerCase().contains(txt.toLowerCase());
				Predicate<Produit.Model> predicate = design.or(categorie.or(categorie));
				table.setItems(data.filtered(predicate));
			}
		});
		
		txtSearch.setPromptText("Rechercher....");
		sideBarRight.getChildren().addAll(txtSearch,table);
	}
	
	
	public void initGrid(){
		grid.addRow(0, labelId, txtId);
		grid.addRow(1, labelDesignation, txtDesignation);
		grid.addRow(2, labelPrixAchat, txtPrixAchat);
		grid.addRow(3, labelCategorie, CategorieComboBox);
		grid.addRow(4, labelPrixVente, txtPrixVente);

		grid.setVgap(10);
		txtId.setDisable(true);
	}
	
	private void resetTextFields() {
		txtId.setText("");  
		txtDesignation.setText(""); 
		txtPrixAchat.setText(""); 
		txtPrixVente.setText(""); 
		CategorieComboBox.setValue(cats.get(0));
	}
	
	public void initButtons(){

		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Categorie.Model cat = null;
				double prixVente = 0, prixAchat = 0;
				try{
					cat = catdao.retrieveAll(CategorieComboBox.getValue()).get(0);
					prixVente = Double.parseDouble(txtPrixVente.getText());
					prixAchat = Double.parseDouble(txtPrixAchat.getText());
				}catch(Exception exp){}
				if(txtDesignation.getText().length()!=0 && txtPrixAchat.getText().length()!=0) {
					Produit.Model p = new Produit.Model(1, txtDesignation.getText(), prixAchat, prixVente, cat);
					long id = prddao.create(p);
					if(id > 0) {
						p.setId(id);
						data.add(p);
						resetTextFields();
					}
				}
			}
		});

		btnNouveau.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				resetTextFields();
				table.getSelectionModel().clearSelection();
			}
		});

		
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtId.getText()!="") {
					Produit.Model p =prddao.retrieve(Long.parseLong(txtId.getText()));
					if(p!=null) {
						p.setDesignation(txtDesignation.getText());
						p.setPrixAchat(verifyNumber(txtPrixAchat.getText()));
						p.setPrixVente(verifyNumber(txtPrixVente.getText()));
						p.setCategorie(catdao.retrieveAll(CategorieComboBox.getValue()).get(0));
						data.remove(indexOfTable, indexOfTable+1);
						data.add(indexOfTable+1,p);
						prddao.update(p);
						table.getSelectionModel().clearSelection();
						resetTextFields();
					}
				}
			}
		});
		
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtId.getText()!="") {
					Produit.Model p =prddao.retrieve(Long.parseLong(txtId.getText()));
					if(p!=null) {
						prddao.delete(p);
						data.remove(indexOfTable, indexOfTable+1);
						table.getSelectionModel().clearSelection();
						resetTextFields();
					}
				}
			}
		});
		
		gestionCat.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Categorie.IHM catihm = new Categorie.IHM();
				catihm.start();
				Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.FINISH);
				alert.setTitle(catihm.getTitleProperty().getValue());
				alert.setHeaderText(null);
				alert.getDialogPane().getScene().getStylesheets().add(IHMMain.IHM.class.getResource("MyCss.css").toExternalForm());
				alert.getDialogPane().setContent(catihm.getPrimaryPane());
				alert.setOnCloseRequest((event) -> {
					cats = catdao.retrieveAll();
					CategorieComboBox.getItems().clear();
					for (Categorie.Model cat : cats) {
						CategorieComboBox.getItems().add(cat.toString());
					}
				});
				alert.setResizable(true);
				alert.showAndWait();
			}
		});

		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer, gestionCat);
		
		
	}
	
	@Override
	public void start(){
		initGrid();
		initTable();
		initButtons();
		
		((BorderPane)primaryPane).setRight(sideBarRight);
		((BorderPane)primaryPane).setCenter(grid);
		((BorderPane)primaryPane).setLeft(sideBarLeft);
		grid.setAlignment(Pos.CENTER);
		sideBarRight.setAlignment(Pos.CENTER);
		sideBarLeft.setPadding(new Insets(20, 0, 0, 0));
		sideBarLeft.setSpacing(20);
		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("grid");
		
	}
}
