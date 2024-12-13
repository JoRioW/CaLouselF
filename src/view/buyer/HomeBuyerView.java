package view.buyer;

import controller.ItemController;
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
import view.LoginView;

public class HomeBuyerView extends BorderPane {
    private static final String SUCCESS = "Success";

    private Label errorLbl;
    private TableView<Item> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private Button viewWishlistBtn, viewHistoryBtn, offerPriceBtn,
    purchaseItemBtn, addWishlistBtn, removeWishlistBtn, logoutBtn;
    private TableColumn<Item, String> name, category, size, price;
    private Item selectedItem;
    private User user;
    private Stage stage;

    private void init() {
        table = new TableView<>();
        viewWishlistBtn = new Button("View Wishlist");
        viewHistoryBtn = new Button("View Purchase History");
        offerPriceBtn = new Button("Offer Price");
        purchaseItemBtn = new Button("Purchase Item");
        addWishlistBtn = new Button("Add Wishlist");
//        removeWishlistBtn = new Button("Remove Wishlist");
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
        topGP.add(viewWishlistBtn, 0, 0);
        topGP.add(viewHistoryBtn, 1, 0);
        topGP.add(offerPriceBtn, 2, 0);
        topGP.add(purchaseItemBtn, 0, 1);
        topGP.add(addWishlistBtn, 1, 1);
//        topGP.add(removeWishlistBtn, 2, 0);
        topGP.add(logoutBtn, 3, 0);
        topGP.add(errorLbl, 0, 2);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }

    private void setEvents() {
        table.setOnMouseClicked(e -> {
            selectedItem = table.getSelectionModel().getSelectedItem();
        });

        addWishlistBtn.setOnAction(e -> {
            if (selectedItem != null) {
                System.out.println("Item added to wishlist: " + selectedItem.getItem_name());
            } else {
                errorLbl.setText("No item selected to add to wishlist.");
            }
        });

        logoutBtn.setOnAction(e -> {
        	user = null;
        	new LoginView(stage);
        });
    }

    private void refreshTable() {
        ObservableList<Item> items = ItemController.viewItem(user.getUser_id(), user.getRoles());
        table.setItems(items);
    }

    public HomeBuyerView(Stage stage, User user) {
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
