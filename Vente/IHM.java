package Vente;

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
import javafx.util.StringConverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

public class IHM extends Factories.ihm.IHMFactorie{

	private Vente.DAO vntdao = new Vente.DAO();
	private List<Vente.Model> list = vntdao.retrieveAll();
	private ObservableList<Vente.Model> data = FXCollections.observableArrayList(list);
	
	ComboBox<Client.Model> ClientComboBox = new ComboBox<Client.Model>();
	Client.DAO clientDao = new Client.DAO();
	private List<Client.Model> clients = clientDao.retrieveAll();
	
	private LigneCom.IHM ligneIHM = new LigneCom.IHM();
	private Paiement.IHM paiementIHM = new Paiement.IHM();

	DatePicker datePicker = new DatePicker();
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
	{
		primaryPane = new BorderPane();
		Image img=null;
		try {
			img = new Image(new FileInputStream(IHM.class.getResource("home.png").getPath()));
		} catch (FileNotFoundException e) {}
		imgModule.setImage(img);
		imgModule.resize(200, 200);
		title.set("Vente Management");
		ClientComboBox.getItems().addAll(clients);

		ligneIHM.start();
		paiementIHM.start();

    	ligneIHM.vente = data.get(0).getId();
		paiementIHM.vente = data.get(0).getId();
	}
	
	GridPane grid = new GridPane();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	
	Label labelId = new Label("ID : ");
	Label labelDate = new Label("Date : ");
	Label labelTotale = new Label("Totale : ");
	Label labelClient = new Label("Client : ");
	
	TextField txtId = new TextField();
	TextField txtTotale = new TextField();
	
	TextField txtSearch = new TextField();
	TableView<Vente.Model> table = new TableView<Vente.Model>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("RESET FORM");
	Button btnSupprimer = new Button("Supprimer");
	Button gestionligneCom = new Button("Commande");
	Button gestionPaiement = new Button("Paiement");

	int indexOfTable=-1;
	private void initTable() {
		table.setItems(data);
		table.autosize();
		table.prefHeightProperty().bind(primaryPane.heightProperty());
		table.prefWidthProperty().bind(primaryPane.widthProperty().multiply(0.4));
		
		
		TableColumn<Vente.Model, Integer> colId = new TableColumn<Vente.Model, Integer>("ID");
		colId.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		colId.setCellValueFactory(new PropertyValueFactory<Vente.Model, Integer>("id"));
		table.getColumns().add(colId);

		TableColumn<Vente.Model, String> colDate = new TableColumn<Vente.Model, String>("Date");
		colDate.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		colDate.setCellValueFactory(new PropertyValueFactory<Vente.Model, String>("Date"));
		table.getColumns().add(colDate);
		
		TableColumn<Vente.Model, String> colTotale = new TableColumn<Vente.Model, String>("Totale");
		colTotale.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		colTotale.setCellValueFactory(new PropertyValueFactory<Vente.Model, String>("Totale"));
		table.getColumns().add(colTotale);

		TableColumn<Vente.Model, String> colclient = new TableColumn<Vente.Model, String>("client");
		colclient.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		colclient.setCellValueFactory(new PropertyValueFactory<Vente.Model, String>("client"));
		table.getColumns().add(colclient);
		
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		        if(table.getSelectionModel().getSelectedItem() != null) 
		        {
		        	indexOfTable = table.getSelectionModel().getSelectedIndex();
		        	resetTextFields();
		        	Vente.Model p = table.getSelectionModel().getSelectedItem();
		        	
		        	txtTotale.setText(String.valueOf(p.getTotale()));
		        	txtId.setText(String.valueOf(p.getId()));
		        	datePicker.setValue(LocalDate.parse(p.getDate(), dateFormatter));
		        	ClientComboBox.setValue(p.getClient());
		        	
		        	gestionligneCom.setText("Commande "+p.getId());
		        	ligneIHM.list = p.getLignesCommande();
		        	ligneIHM.data.clear();
		        	ligneIHM.data.addAll(FXCollections.observableArrayList(ligneIHM.list));
		        	ligneIHM.vente = p.getId();
		    		ligneIHM.getTitleProperty().set("Ligne de Commande pour vente: "+p.getId());
		    		
		    		gestionPaiement.setText("Paiement "+p.getId());
		    		paiementIHM.vente = p.getId();
		    		paiementIHM.getTitleProperty().set("Paiement pour vente: "+p.getId());
		    		paiementIHM.list.clear();
		    		paiementIHM.list.addAll(paiementIHM.paimentDao.retrieveAllVente(p.getId()));
		    		paiementIHM.data.clear();
		        	paiementIHM.data.addAll(FXCollections.observableArrayList(paiementIHM.list));
					paiementIHM.TotaleRest();
		        }
			 }
	     });
		table.getSelectionModel().select(0);
		
		txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String txt = txtSearch.getText();
				Predicate<Vente.Model> design = i -> i.getDate().toLowerCase().contains(txt.toLowerCase());
				Predicate<Vente.Model> Client = i -> i.getClient().toString().toLowerCase().contains(txt.toLowerCase());
				Predicate<Vente.Model> predicate = design.or(Client);
				table.setItems(data.filtered(predicate));
			}
		});
		
		txtSearch.setPromptText("Rechercher....");
		sideBarRight.getChildren().addAll(txtSearch,table);
	}
	
	
	public void initGrid(){
		datePicker.setValue(LocalDate.now());
		datePicker.setShowWeekNumbers(false);
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}
			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		};
		datePicker.setConverter(converter);
		datePicker.setPromptText("yyyy/MM/dd");
		grid.addRow(0, labelId, txtId);
		grid.addRow(1, labelDate, datePicker);
		grid.addRow(2, labelTotale, txtTotale);
		grid.addRow(3, labelClient, ClientComboBox);
		grid.setVgap(10);
		txtId.setDisable(true);
		txtTotale.setDisable(true);
	}
	
	private void resetTextFields() {
		txtId.setText("");
		datePicker.setValue(LocalDate.now());
		txtTotale.setText(""); 
		ClientComboBox.setValue(clients.get(0)); 
	}
	
	public void initButtons(){
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Vente.Model p = new Vente.Model(1, datePicker.getValue().format(dateFormatter), null, ClientComboBox.getValue());
				long id = vntdao.create(p);
				data.clear();
				list = vntdao.retrieveAll();
				data.addAll(FXCollections.observableArrayList(list));
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
				Vente.Model p = vntdao.retrieve((long)verifyNumber(txtId.getText()));
				if(p!=null) {
					p.setClient(ClientComboBox.getValue());
					vntdao.update(p);
					data.remove(indexOfTable, indexOfTable+1);
					data.add(indexOfTable+1,p);
					table.getSelectionModel().clearSelection();
					resetTextFields();
				}
			}
		});
		
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtId.getText()!="") {
					Vente.Model p =vntdao.retrieve(Long.parseLong(txtId.getText()));
					if(p!=null) {
						vntdao.delete(p);
						data.remove(indexOfTable, indexOfTable+1);
						table.getSelectionModel().clearSelection();
						resetTextFields();
					}
				}
			}
		});
		
		gestionligneCom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.FINISH);
				alert.setTitle(ligneIHM.getTitleProperty().getValue());
				alert.setHeaderText(null);
				alert.getDialogPane().getScene().getStylesheets().add(IHMMain.IHM.class.getResource("MyCss.css").toExternalForm());
				alert.getDialogPane().setContent(ligneIHM.getPrimaryPane());
				alert.setOnCloseRequest((event) -> {
						Vente.Model vent = data.get(indexOfTable);
						vent.setLignesCommande(ligneIHM.list);
						vntdao.update(vent);
						data.clear();
						list = vntdao.retrieveAll();
						data.addAll(FXCollections.observableArrayList(list));
				});
				alert.setResizable(true);
				alert.showAndWait();
			}
		});
		
		gestionPaiement.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.FINISH);
				alert.setTitle(paiementIHM.getTitleProperty().getValue());
				alert.setHeaderText(null);
				alert.getDialogPane().getScene().getStylesheets().add(IHMMain.IHM.class.getResource("MyCss.css").toExternalForm());
				alert.getDialogPane().setContent(paiementIHM.getPrimaryPane());
				alert.setResizable(true);
				alert.showAndWait();
			}
		});
		
		sideBarLeft.getChildren().addAll(btnNouveau,btnAjouter,btnModifier,btnSupprimer);
	}
	
	@Override
	public void start(){
		initGrid();
		initTable();
		initButtons();
		VBox vbox = new VBox();
		vbox.getChildren().add(grid);
		vbox.getChildren().add(gestionligneCom);
		vbox.getChildren().add(gestionPaiement);
		vbox.getStyleClass().add("vbox");
		vbox.setSpacing(20);
		((BorderPane)primaryPane).setRight(sideBarRight);
		((BorderPane)primaryPane).setCenter(vbox);
		((BorderPane)primaryPane).setLeft(sideBarLeft);

		sideBarLeft.setPadding(new Insets(20, 0, 0, 0));
		sideBarLeft.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);
		sideBarRight.setAlignment(Pos.CENTER);
		grid.setAlignment(Pos.CENTER);

		gestionligneCom.getStyleClass().add("gestion");
		gestionPaiement.getStyleClass().add("gestion");
		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("grid");
		
	}
}
