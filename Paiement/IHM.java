package Paiement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Predicate;

public class IHM extends Factories.ihm.IHMFactorie{
	ComboBox<Paiement.Mode> modeComboBox = new ComboBox<>();
	
	{
		//widthWindow;
		//heightWindow;
		//title;
		primaryPane = new BorderPane();
		title.set("Paiement Management");
		modeComboBox.getItems().addAll(Mode.values());
	}
	public Paiement.DAO paimentDao = new Paiement.DAO();
	public long vente = 0;
	
	public List<Paiement.Model> list = paimentDao.retrieveAllVente(vente);
	public ObservableList<Paiement.Model> data = FXCollections.observableArrayList(list);
	
	GridPane grid = new GridPane();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	
	Label labelId = new Label("ID : ");
	Label labelMode = new Label("Mode : ");
	Label labelTotaleRecept = new Label("TotaleRecept : ");
	public Label labelRest = new Label("rest : ");
	
	TextField txtId = new TextField();
	TextField txtTotaleRecept = new TextField();
	
	TextField txtSearch = new TextField();
	TableView<Paiement.Model> table = new TableView<Paiement.Model>();
	
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("RESET FORM");
	Button btnSupprimer = new Button("Supprimer");	
	
	int indexOfTable=-1;
	private void initTable() {
		table.setItems(data);
		table.autosize();
		table.prefHeightProperty().bind(primaryPane.heightProperty());
		table.prefWidthProperty().bind(primaryPane.widthProperty().multiply(0.4));
		
		TableColumn<Paiement.Model, Integer> colId = new TableColumn<Paiement.Model, Integer>("ID");
		colId.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
		colId.setCellValueFactory(new PropertyValueFactory<Paiement.Model, Integer>("Id"));
		table.getColumns().add(colId);
		
		TableColumn<Paiement.Model, String> colMode = new TableColumn<Paiement.Model, String>("Mode");
		colMode.prefWidthProperty().bind(table.widthProperty().multiply(0.35));
		colMode.setCellValueFactory(new PropertyValueFactory<Paiement.Model, String>("Mode"));
		table.getColumns().add(colMode);
		
		TableColumn<Paiement.Model, String> colTotaleRecept = new TableColumn<Paiement.Model, String>("Totale Recept");
		colTotaleRecept.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
		colTotaleRecept.setCellValueFactory(new PropertyValueFactory<Paiement.Model, String>("TotaleRecept"));
		table.getColumns().add(colTotaleRecept);
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		        if(table.getSelectionModel().getSelectedItem() != null) 
		        {
		        	indexOfTable = table.getSelectionModel().getSelectedIndex();
		        	resetTextFields();
		        	Paiement.Model p = table.getSelectionModel().getSelectedItem();
		        	txtId.setText(String.valueOf(p.getId()));
		        	modeComboBox.setValue(p.getMode());
		        	txtTotaleRecept.setText(p.getTotaleRecept()+"");
		        }
			 }
	     });
		
		txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String txt = txtSearch.getText();
				Predicate<Paiement.Model> Mode = i -> i.getMode().toString().toLowerCase().contains(txt.toLowerCase());
				table.setItems(data.filtered(Mode));
			}
		});
		
		txtSearch.setPromptText("Rechercher....");
		sideBarRight.getChildren().addAll(txtSearch,table);
	}
	
	public void initGrid(){
		grid.addRow(0, labelId, txtId);
		grid.addRow(1, labelMode, modeComboBox);
		grid.addRow(2, labelTotaleRecept, txtTotaleRecept);
		
		grid.setVgap(10);
		txtId.setDisable(true);
	}
	
	private void resetTextFields() {
		txtId.setText("");  
		modeComboBox.setValue(Paiement.Mode.values()[0]); 
		txtTotaleRecept.setText(""); 
	}
	
	public void initButtons(){
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Paiement.Model p = new Paiement.Model(1, verifyNumber(txtTotaleRecept.getText()), modeComboBox.getValue(), (new Vente.DAO().retrieve(vente)));
				long id = paimentDao.create(p);
				if(id > 0) {
					p.setId(id);
					data.add(p);
					resetTextFields();
					TotaleRest();
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
					Paiement.Model p =paimentDao.retrieve((long)verifyNumber(txtId.getText()));
					if(p!=null) {
						p.setMode(modeComboBox.getValue());
						p.setTotaleRecept(verifyNumber(txtTotaleRecept.getText()));
						data.remove(indexOfTable, indexOfTable+1);
						if(indexOfTable == 0)
							data.add(indexOfTable,p);
						else
							data.add(indexOfTable+1,p);
						paimentDao.update(p);
						table.getSelectionModel().clearSelection();
						resetTextFields();
						TotaleRest();
					}
				}
			}
		});
		
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtId.getText()!="") {
					Paiement.Model p =paimentDao.retrieve((long)verifyNumber(txtId.getText()));
					if(p!=null) {
						paimentDao.delete(p);
						data.remove(indexOfTable, indexOfTable+1);
						table.getSelectionModel().clearSelection();
						resetTextFields();
						TotaleRest();
					}
				}
			}
		});
		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
		
		
	}

	public void TotaleRest(){
		double rest = 0;
		try {
			rest = (new Vente.DAO().retrieve(vente).getTotale()) - new Paiement.DAO().TotalReceptVente(vente);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		labelRest.setText("Rest : "+rest);
	}
	
	@Override
	public void start(){
		initGrid();
		initTable();
		initButtons();
		labelRest.setStyle("-fx-background-color:red;-fx-width:400px;-fx-color:white;");
		BorderPane.setAlignment(labelRest, Pos.CENTER);
		
		((BorderPane)primaryPane).setTop(labelRest);
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
