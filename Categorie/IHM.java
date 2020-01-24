package Categorie;

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
	
	{
		//widthWindow;
		//heightWindow;
		//imgModule
		primaryPane = new BorderPane();
		title.set("Categorie Management");
	}
	
	private Categorie.DAO catdao = new Categorie.DAO();
	private List<Categorie.Model> list = catdao.retrieveAll();
	private ObservableList<Categorie.Model> data = FXCollections.observableArrayList(list);
	
	GridPane grid = new GridPane();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	
	Label labelId = new Label("ID : ");
	Label labelIntitule = new Label("Intitule : ");
	
	TextField txtId = new TextField();
	TextField txtIntitule = new TextField();
	
	TextField txtSearch = new TextField();
	TableView<Categorie.Model> table = new TableView<Categorie.Model>();
			
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
		
		
		TableColumn<Categorie.Model, Integer> colId = new TableColumn<Categorie.Model, Integer>("ID");
		colId.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		colId.setCellValueFactory(new PropertyValueFactory<Categorie.Model, Integer>("Id"));
		table.getColumns().add(colId);

		TableColumn<Categorie.Model, String> colIntitule = new TableColumn<Categorie.Model, String>("Intitule");
		colIntitule.prefWidthProperty().bind(table.widthProperty().multiply(0.7));
		colIntitule.setCellValueFactory(new PropertyValueFactory<Categorie.Model, String>("Intitule"));
		table.getColumns().add(colIntitule);
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		        if(table.getSelectionModel().getSelectedItem() != null) 
		        {
		        	indexOfTable = table.getSelectionModel().getSelectedIndex();
		        	resetTextFields();
		        	Categorie.Model p = table.getSelectionModel().getSelectedItem();
		        	txtId.setText(String.valueOf(p.getId()));
		        	txtIntitule.setText(p.getIntitule());
		        }
			 }
	     });
		
		txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String txt = txtSearch.getText();
				Predicate<Categorie.Model> Intitule = i -> i.getIntitule().toLowerCase().contains(txt.toLowerCase());
				table.setItems(data.filtered(Intitule));
			}
		});
		
		txtSearch.setPromptText("Rechercher....");
		sideBarRight.getChildren().addAll(txtSearch,table);
	}
	
	
	public void initGrid(){
		grid.addRow(0, labelId, txtId);
		grid.addRow(1, labelIntitule, txtIntitule);
		
		grid.setVgap(10);
		txtId.setDisable(true);
	}
	
	private void resetTextFields() {
		txtId.setText("");  
		txtIntitule.setText(""); 
	}
	
	public void initButtons(){

		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtIntitule.getText().length()!=0) {
					Categorie.Model p = new Categorie.Model(1, txtIntitule.getText());
					long id = catdao.create(p);
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
					Categorie.Model p =catdao.retrieve(Long.parseLong(txtId.getText()));
					if(p!=null) {
						p.setIntitule(txtIntitule.getText());
						data.remove(indexOfTable, indexOfTable+1);
						data.add(indexOfTable+1,p);
						catdao.update(p);
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
					Categorie.Model p =catdao.retrieve(Long.parseLong(txtId.getText()));
					if(p!=null) {
						catdao.delete(p);
						data.remove(indexOfTable, indexOfTable+1);
						table.getSelectionModel().clearSelection();
						resetTextFields();
					}
				}
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
