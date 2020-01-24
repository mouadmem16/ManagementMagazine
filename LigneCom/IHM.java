package LigneCom;

import java.util.List;
import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class IHM extends Factories.ihm.IHMFactorie{
	private LigneCom.DAO ligneComdao = new LigneCom.DAO();
	public List<LigneCom.Model> list = ligneComdao.retrieveAll();
	public long vente = 5;
	public ObservableList<LigneCom.Model> data = FXCollections.observableArrayList(list);
	ComboBox<Produit.Model> produitComboBox = new ComboBox<>();
	Produit.DAO prdDAO = new Produit.DAO();
	private List<Produit.Model> prds = prdDAO.retrieveAll();
	
	{
		primaryPane = new BorderPane();
		title.set("Commande Management");
		
		produitComboBox.getItems().addAll(prds);	
		produitComboBox.setValue(prds.get(0));
	}
	
	GridPane grid = new GridPane();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	
	Label labelQuantite = new Label("Quantite : ");
	Label labelSousTotale = new Label("SousTotale : ");
	Label labelproduit = new Label("Produit : ");
	
	TextField txtQuantite = new TextField();
	TextField txtSousTotale = new TextField();
	
	TextField txtSearch = new TextField();
	TableView<LigneCom.Model> table = new TableView<LigneCom.Model>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("RESET FORM");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;
	private void initTable() {
//		table.setEditable(true);
		table.setItems(data);
		table.autosize();
		table.prefHeightProperty().bind(primaryPane.heightProperty());
		table.prefWidthProperty().bind(primaryPane.widthProperty().divide(2.3));
		
		
		TableColumn<LigneCom.Model, Integer> colproduit = new TableColumn<LigneCom.Model, Integer>("Produit");
		colproduit.prefWidthProperty().bind(table.widthProperty().multiply(0.35));
		colproduit.setCellValueFactory(new PropertyValueFactory<LigneCom.Model, Integer>("produit"));
		table.getColumns().add(colproduit);

		TableColumn<LigneCom.Model, Integer> colquantite = new TableColumn<LigneCom.Model, Integer>("QTE");
		colquantite.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
		colquantite.setCellValueFactory(new PropertyValueFactory<LigneCom.Model, Integer>("quantite"));
		table.getColumns().add(colquantite);
		
		TableColumn<LigneCom.Model, Double> colsousTotale = new TableColumn<LigneCom.Model, Double>("Sous Totale");
		colsousTotale.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
		colsousTotale.setCellValueFactory(new PropertyValueFactory<LigneCom.Model, Double>("sousTotale"));
		table.getColumns().add(colsousTotale);
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		        if(table.getSelectionModel().getSelectedItem() != null) 
		        {
		        	indexOfTable = table.getSelectionModel().getSelectedIndex();
		        	resetTextFields();
		        	LigneCom.Model p = table.getSelectionModel().getSelectedItem();
		        	txtQuantite.setText(String.valueOf(p.getQuantite()));
		        	txtSousTotale.setText(String.valueOf(p.getSousTotale()));
		        	produitComboBox.setValue(p.getProduit());
		        }
			 }
	     });
		
		txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String txt = txtSearch.getText();
				Predicate<LigneCom.Model> prd = i -> i.getProduit().getDesignation().toLowerCase().contains(txt.toLowerCase());
				table.setItems(data.filtered(prd));
			}
		});
		
		txtSearch.setPromptText("Rechercher....");
		sideBarRight.getChildren().addAll(txtSearch,table);
	}
	
	
	public void initGrid(){
		grid.addRow(0, labelproduit, produitComboBox);
		grid.addRow(1, labelQuantite, txtQuantite);
		grid.addRow(2, labelSousTotale, txtSousTotale);
		
		txtQuantite.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				txtSousTotale.setText(""+produitComboBox.getValue().getPrixVente()*(int)verifyNumber(txtQuantite.getText()));
			}
		});
		grid.setVgap(10);
		txtSousTotale.setDisable(true);
	}
	
	private void resetTextFields() {
		txtQuantite.setText("");  
		txtSousTotale.setText(""); 
		produitComboBox.setValue(prds.get(0));
	}
	
	public void initButtons(){

		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {	
				LigneCom.Model p = new LigneCom.Model(produitComboBox.getValue(), vente, (int)verifyNumber(txtQuantite.getText()));
				list.add(p);
				data.add(p);
				resetTextFields();
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
				if(txtQuantite.getText()!="") {
					list.get(table.getSelectionModel().getSelectedIndex()).setProduit(produitComboBox.getValue());
					list.get(table.getSelectionModel().getSelectedIndex()).setQuantite((int)verifyNumber(txtQuantite.getText()));
					data.clear();
					data.addAll(list);
				}
			}
		});
		
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				list.remove(table.getSelectionModel().getSelectedIndex());
				data.remove(table.getSelectionModel().getSelectedIndex());
			}
		});

		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
		
		
	}
	
	@Override
	public void start(){
		initGrid();
		initTable();
		initButtons();
		
		((BorderPane)primaryPane).setRight(sideBarRight);
		((BorderPane)primaryPane).setCenter(grid);
		((BorderPane)primaryPane).setLeft(sideBarLeft);
		sideBarLeft.setAlignment(Pos.CENTER);
		grid.setAlignment(Pos.CENTER);
		sideBarRight.setAlignment(Pos.CENTER);
		
		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("grid");
		
	}
}
