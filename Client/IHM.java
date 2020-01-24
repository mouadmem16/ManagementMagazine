package Client;

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

public class IHM extends Factories.ihm.IHMFactorie{
	
	{
		widthWindow.setValue(1100);
		//heightWindow;
		primaryPane = new BorderPane();
		Image img=null;
		try {
			img = new Image(new FileInputStream(IHM.class.getResource("home.png").getPath()));
		} catch (FileNotFoundException ignored) {}
		imgModule.setImage(img);
		imgModule.resize(200, 200);
		title.set("Client Management");
	}
	
	private Client.DAO cltdao = new Client.DAO();
	private List<Client.Model> list = cltdao.retrieveAll();
	private ObservableList<Client.Model> data = FXCollections.observableArrayList(list);
	
	GridPane grid = new GridPane();
	VBox sideBarLeft = new VBox();
	VBox sideBarRight = new VBox();
	
	Label labelId = new Label("ID : ");
	Label labelNom = new Label("Nom : ");
	Label labelPrenom = new Label("Prenom : ");
	Label labelTele = new Label("Tele : ");
	Label labelEmail = new Label("Email : ");
	Label labelAdr = new Label("Adresse : ");
	
	TextField txtId = new TextField();
	TextField txtNom = new TextField();
	TextField txtPrenom = new TextField();	
	TextField txtTele = new TextField();
	TextField txtEmail = new TextField();
	TextField txtAdr = new TextField();
	
	TextField txtSearch = new TextField();
	TableView<Client.Model> table = new TableView<Client.Model>();
			
	Button btnModifier = new Button("Modifier");
	Button btnAjouter = new Button("Ajouter");
	Button btnNouveau = new Button("RESET FORM");
	Button btnSupprimer = new Button("Supprimer");	

	int indexOfTable=-1;
	private void initTable() {
		/* table.setEditable(true); */
		table.setItems(data);
		table.autosize();
		table.prefHeightProperty().bind(primaryPane.heightProperty());
		table.prefWidthProperty().bind(primaryPane.widthProperty().multiply(0.55));
		
		
		TableColumn<Client.Model, Integer> colId = new TableColumn<>("ID");
		colId.prefWidthProperty().bind(table.widthProperty().multiply(0.05));
		colId.setCellValueFactory(new PropertyValueFactory<Client.Model, Integer>("Id"));
		table.getColumns().add(colId);

		TableColumn<Client.Model, String> colNom = new TableColumn<>("Nom");
		colNom.prefWidthProperty().bind(table.widthProperty().multiply(0.13));
		colNom.setCellValueFactory(new PropertyValueFactory<Client.Model, String>("nom"));
		table.getColumns().add(colNom);
		
		TableColumn<Client.Model, String> colPrenom = new TableColumn<>("Prenom");
		colPrenom.prefWidthProperty().bind(table.widthProperty().multiply(0.13));
		colPrenom.setCellValueFactory(new PropertyValueFactory<Client.Model, String>("prenom"));
		table.getColumns().add(colPrenom);
		
		TableColumn<Client.Model, String> colemail = new TableColumn<>("Email");
		colemail.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		colemail.setCellValueFactory(new PropertyValueFactory<Client.Model, String>("email"));
		table.getColumns().add(colemail);

		TableColumn<Client.Model, String> colTele = new TableColumn<>("Tele");
		colTele.prefWidthProperty().bind(table.widthProperty().multiply(0.17));
		colTele.setCellValueFactory(new PropertyValueFactory<Client.Model, String>("tele"));
		table.getColumns().add(colTele);

		TableColumn<Client.Model, String> colAdr = new TableColumn<>("Adresse");
		colAdr.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
		colAdr.setCellValueFactory(new PropertyValueFactory<Client.Model, String>("adresse"));
		table.getColumns().add(colAdr);

		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
		        if(table.getSelectionModel().getSelectedItem() != null) 
		        {
		        	indexOfTable = table.getSelectionModel().getSelectedIndex();
		        	resetTextFields();
		        	Client.Model p = table.getSelectionModel().getSelectedItem();
		        	txtPrenom.setText(String.valueOf(p.getPrenom()));
		        	txtId.setText(String.valueOf(p.getId()));
		        	txtNom.setText(p.getNom());
		        	txtEmail.setText(p.getEmail());
		        	txtAdr.setText(p.getAdresse());
		        	txtTele.setText(p.getTele());
		        }
			 }
	     });
		
		txtSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String txt = txtSearch.getText();
				Predicate<Client.Model> nom = i -> i.getNom().toLowerCase().contains(txt.toLowerCase());
				Predicate<Client.Model> email = i -> i.getEmail().toLowerCase().contains(txt.toLowerCase());
				Predicate<Client.Model> prenom = i -> i.getPrenom().toLowerCase().contains(txt.toLowerCase());
				Predicate<Client.Model> adresse = i -> i.getAdresse().toLowerCase().contains(txt.toLowerCase());
				Predicate<Client.Model> predicate = nom.or(prenom.or(email.or(adresse)));
				table.setItems(data.filtered(predicate));
			}
		});
		
		txtSearch.setPromptText("Rechercher....");
		sideBarRight.getChildren().addAll(txtSearch,table);
	}
	
	
	public void initGrid(){
		grid.addRow(0, labelId, txtId);
		grid.addRow(1, labelNom, txtNom);
		grid.addRow(2, labelPrenom, txtPrenom);
		grid.addRow(3, labelEmail, txtEmail);
		grid.addRow(4, labelTele, txtTele);
		grid.addRow(5, labelAdr, txtAdr);
		
		grid.setVgap(10);
		txtId.setDisable(true);
	}
	
	private void resetTextFields() {
		txtId.setText("");  
		txtNom.setText(""); 
		txtPrenom.setText(""); 
		txtEmail.setText(""); 
		txtAdr.setText(""); 
		txtTele.setText("");
	}
	
	public void initButtons(){

		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(txtNom.getText().length()!=0 && txtPrenom.getText().length()!=0) {
					Client.Model p = new Client.Model(1, txtNom.getText(), txtPrenom.getText(), txtTele.getText(), txtEmail.getText(), txtAdr.getText());
					long id = cltdao.create(p);
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
					Client.Model p =cltdao.retrieve(Long.parseLong(txtId.getText()));
					if(p!=null) {
						p.setNom(txtNom.getText());
						p.setPrenom(txtPrenom.getText());
						p.setAdresse(txtAdr.getText());
						p.setTele(txtTele.getText());
						p.setEmail(txtEmail.getText());
						data.remove(indexOfTable, indexOfTable+1);
						data.add(indexOfTable+1,p);
						cltdao.update(p);
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
					Client.Model p =cltdao.retrieve(Long.parseLong(txtId.getText()));
					if(p!=null) {
						cltdao.delete(p);
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
		grid.setAlignment(Pos.CENTER);
		sideBarRight.setAlignment(Pos.CENTER);

		sideBarLeft.setPadding(new Insets(20, 0, 0, 0));
		sideBarLeft.setSpacing(20);

		sideBarLeft.getStyleClass().add("sideBarLeft");
		sideBarRight.getStyleClass().add("sideBarRight");
		grid.getStyleClass().add("grid");
		
	}
}
