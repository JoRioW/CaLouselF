package view.seller;

import controller.ItemController;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Item;

public class HomeSellerView extends BorderPane {
	private Label errorLbl;
	private Button uploadItemBtn, updateItemBtn, deleteItemBtn;
	private TableView<Item> table;
	private Stage stage;
	private ScrollPane tableSP;
	private GridPane topGP, bottomGP;
	
	private TableColumn<Item, String> name, category, size, price;
	private Item selectedItem;
	
	
	private void init() {
		table = new TableView<>();
		
		uploadItemBtn = new Button("Upload");
		updateItemBtn = new Button("Edit");
		deleteItemBtn = new Button("Delete");
		
		tableSP = new ScrollPane(table);
		topGP = new GridPane();
		bottomGP = new GridPane();
		errorLbl = new Label();
	}
	
	private void setTableLayout() {
		//Set label kolom pada tabel
		name = new TableColumn<Item, String>("Item Name");
		name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
		category = new TableColumn<>("Item Category");
		category.setCellValueFactory(new PropertyValueFactory<>("item_category"));
		size = new TableColumn<>("Item Size");
		size.setCellValueFactory(new PropertyValueFactory<>("item_size"));
		price = new TableColumn<>("Item Price");
		price.setCellValueFactory(new PropertyValueFactory<>("item_price"));
		
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.getColumns().addAll(name, category, size, price);
	}
	
	private void setLayout() {
		topGP.add(uploadItemBtn, 0, 0);
		topGP.add(updateItemBtn, 1, 0);
		topGP.add(deleteItemBtn, 2, 0);
		topGP.add(errorLbl, 3, 0);
		GridPane.setHalignment(errorLbl, HPos.RIGHT);
		GridPane.setHgrow(errorLbl, Priority.ALWAYS);
		topGP.setHgap(10);
		this.setTop(topGP);
		
		tableSP.setFitToWidth(true);
		tableSP.setFitToHeight(true);
		tableSP.setMaxSize(500, 500);
		this.setCenter(tableSP);
	}
	
	private void setEvents() {
		uploadItemBtn.setOnAction(e -> {
			new UploadFormView(stage);
		});
		
		table.setOnMouseClicked(e -> {
			selectedItem = table.getSelectionModel().getSelectedItem();
		});
		
		updateItemBtn.setOnAction(e -> {
			if (selectedItem != null) {
				new EditSellerView(stage, selectedItem);
			}else {
				errorLbl.setText("No item selected to be update");
			}
		});
		
		deleteItemBtn.setOnAction(e -> {
			if (selectedItem != null) {
				String id = selectedItem.getItem_id();
				String message = ItemController.deleteItem(id);
				
				if (message == "Success") {
					refreshTable();
				}else {
					errorLbl.setText(message);
				}
			}else {
				errorLbl.setText("No item selected to be delete");
			}
		});
		
		
	}
	
	private void setStyles() {
		errorLbl.setTextFill(Color.RED);
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
		setStyles();
		
		refreshTable();
		
		this.stage = stage;
		Scene scene = new Scene(this, 500, 600);
		stage.setScene(scene);
		stage.show();
	}

}
