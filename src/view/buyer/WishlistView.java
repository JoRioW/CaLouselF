package view.buyer;

import controller.ItemController;
import controller.WishlistController;
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
import model.User;
import model.Wishlist;

public class WishlistView extends BorderPane {
	
	private Label errorLbl;
    private TableView<Item> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private TableColumn<Item, String> name, category, size, price;
    private Item selectedItem;
    private User user;
    private Stage stage;
	private Button backBtn, removeBtn;
	
	private void init() {
		backBtn = new Button("Back");
		removeBtn = new Button("Remove");
		table = new TableView<>();
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
		topGP.add(backBtn, 0, 0);
		topGP.add(removeBtn, 1, 0);
        topGP.add(errorLbl, 0, 2);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }
	
	private void setEvents() {
		backBtn.setOnAction(e -> {
			new HomeBuyerView(stage, user);
		});
		
        table.setOnMouseClicked(e -> {
            selectedItem = table.getSelectionModel().getSelectedItem();
        });
        
        removeBtn.setOnAction(e -> {
        	String message = WishlistController.removeWishlist(selectedItem.get);
        });

        
    }
	
	private void refreshTable() {
        ObservableList<Wishlist> wishlist = WishlistController.viewWishlist(user.getUser_id());
        table.setItems(wishlist);
    }
	
	public WishlistView(Stage stage, User user) {
		this.user = user;
        this.stage = stage;

        init();
        setTableLayout();
        setLayout();
        setEvents();
        refreshTable();

        Scene scene = new Scene(this, 600, 600);
        stage.setScene(scene);
        stage.show();
	}

}
