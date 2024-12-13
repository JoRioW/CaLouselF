package view.admin;

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
import model.User;
import view.LoginView;

public class HomeAdminView extends BorderPane {

	private Label errorLbl;
    private Button approveItemBtn, declineItemBtn, logoutBtn;
    private TableView<Item> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private TableColumn<Item, String> name, category, size, price;
    private Item selectedItem;
    private Stage stage;
    private User user;
	
	private void init() {
		table = new TableView<>();
		
		approveItemBtn = new Button("Approve");
		declineItemBtn = new Button("Decline");
		logoutBtn = new Button("Logout");
		
		tableSP = new ScrollPane(table);
        topGP = new GridPane();
        
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);
	}
	
	private void setTableLayout() {
        name = new TableColumn<>("Item Name");
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
        topGP.add(approveItemBtn, 0, 0);
        topGP.add(declineItemBtn, 1, 0);
        topGP.add(logoutBtn, 2, 0);
        topGP.add(errorLbl, 3, 0);
        GridPane.setHalignment(errorLbl, HPos.RIGHT);
        GridPane.setHgrow(errorLbl, Priority.ALWAYS);
        topGP.setHgap(10);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }
	
	private void setEvents() {
		approveItemBtn.setOnAction(e -> {
		    if (selectedItem != null) {
		        String id = selectedItem.getItem_id();
		        String message = ItemController.approveItem(id);

		        if ("Item approved successfully".equals(message)) {
		            refreshTable();
		        } else {
		            errorLbl.setText(message);
		        }
		    } else {
		        errorLbl.setText("No item selected to approve.");
		    }
		});
		
		declineItemBtn.setOnAction(e -> {
			
		});
		
		logoutBtn.setOnAction(e -> {
			new LoginView(stage);
		});
	}
	
	private void refreshTable() {
        ObservableList<Item> items = ItemController.viewItem(user.getUser_id(), user.getRoles());
        table.setItems(items);
    }
	
	public HomeAdminView() {
        init();
        setTableLayout();
        setLayout();
        setEvents();
        refreshTable();

        Scene scene = new Scene(this, 500, 600);
        stage.setScene(scene);
        stage.show();
	}

}
