package view.seller;

import controller.ItemController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Item;

public class HomeSellerView extends BorderPane {
	private Label errorLbl;
	private Button uploadItemBtn;
	private TableView table;
	private Stage stage;
	private ScrollPane tableSP;
	private GridPane topGP;
	
	private TableColumn<Item, String> name, category, size, price;
	private TableColumn<Item, Void> action;
	
	
	private void init() {
		table = new TableView<>();
		
		uploadItemBtn = new Button("Upload");
		
		tableSP = new ScrollPane(table);
		topGP = new GridPane();
		errorLbl = new Label();
	}
	
	private void setTableLayout() {
		//Set label kolom pada tabel
		name = new TableColumn<>("Item Name");
		name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_name()));
		category = new TableColumn<>("Item Category");
		category.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_category()));
		size = new TableColumn<>("Item Size");
		size.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_size()));
		price = new TableColumn<>("Item Price");
		price.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_price()));
		action = new TableColumn<>("Action");
		
		//builder untuk button update dan delete
		Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<TableColumn<Item,Void>, TableCell<Item,Void>>() {
			
			@Override
			public TableCell<Item, Void> call(TableColumn<Item, Void> param) {
				final TableCell<Item, Void> cell = new TableCell<Item, Void>() {
					private final Button updateBtn = new Button("Update"); 
					private final Button deleteBtn = new Button("Delete"); 
					private final GridPane btnGP = new GridPane();
					
					{
						btnGP.setHgap(10);
						btnGP.add(updateBtn, 0, 0);
						btnGP.add(deleteBtn, 1, 0);
						
						updateBtn.setOnAction(e -> {
							Item selectedItem = (Item) table.getItems().get(getIndex());
							new EditSellerView(stage, selectedItem);
						});
						
						deleteBtn.setOnAction(e -> {
							Item selectedItem = (Item) table.getItems().get(getIndex());
							String id = selectedItem.getItem_id();
							String message = ItemController.deleteItem(id);
							
							if (message == "Success") {
								refreshTable();
							}else {
								errorLbl.setText(message);
							}
						});
						
					}
					
					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						}else {
							setGraphic(btnGP);
						}
					}
				};
				return cell;
			}
		};
		action.setCellFactory(cellFactory);
		table.getColumns().addAll(name, category, size, price, action);
	}
	
	private void setLayout() {
		topGP.add(uploadItemBtn, 0, 0);
		topGP.add(errorLbl, 1, 0);
		this.setTop(topGP);
		
		
		tableSP.setFitToWidth(true);
		tableSP.setMaxSize(500, 500);
		this.setCenter(tableSP);
	}
	
	private void setEvents() {
		uploadItemBtn.setOnAction(e -> {
			new UploadFormView(stage);
		});
	}
	
	private void refreshTable() {
		ObservableList<Item> items = ItemController.viewItem();
		table.setItems(items);
	}
	
	
	public HomeSellerView(Stage stage) {
		init();
		setTableLayout();
		setLayout();
		setEvents();
		
		refreshTable();
		
		this.stage = stage;
		Scene scene = new Scene(this, 500, 600);
		stage.setScene(scene);
		stage.show();
	}

}
