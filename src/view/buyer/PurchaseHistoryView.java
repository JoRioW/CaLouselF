package view.buyer;

import controller.TransactionController;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Item;
import model.Transaction;
import model.User;
import model.Wishlist;

public class PurchaseHistoryView extends BorderPane {
	
	private Label errorLbl;
    private TableView<Transaction> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private TableColumn<Transaction, String> id, name, category, size, price;
    private Item selectedItem;
    private User user;
    private Stage stage;
	private Button backBtn;
	
	private void init() {
		backBtn = new Button("Back");
		table = new TableView<>();
		tableSP = new ScrollPane(table);
        topGP = new GridPane();
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);
	}
	
	private void setTableLayout() {
		id = new TableColumn<>("Transaction ID");
		id.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));

		name = new TableColumn<>("Item Name");
		name.setCellValueFactory(new PropertyValueFactory<>("item_name"));

        category = new TableColumn<>("Item Category");
        category.setCellValueFactory(new PropertyValueFactory<>("item_category"));

        size = new TableColumn<>("Item Size");
        size.setCellValueFactory(new PropertyValueFactory<>("item_size"));

        price = new TableColumn<>("Item Price");
        price.setCellValueFactory(new PropertyValueFactory<>("item_price"));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(id, name, category, size, price);
    }
	
	private void setLayout() {
        topGP.add(backBtn, 0, 0);
        topGP.add(errorLbl, 1, 0);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }
	
	private void setEvents() {
        backBtn.setOnAction(e -> {
            new HomeBuyerView(stage, user);
        });
    }
	
	private void refreshTable() {
        ObservableList<Transaction> transactions = TransactionController.viewPurchaseHistory(user.getUser_id());
        table.setItems(transactions);
    }
	
	public PurchaseHistoryView(Stage stage, User user) {
		this.user = user;
        this.stage = stage;

        init();
        setTableLayout();
        setLayout();
        setEvents();
        refreshTable();
        
        System.out.println(user.getUser_id());

        Scene scene = new Scene(this, 600, 600);
        stage.setScene(scene);
        stage.show();
	}

}
