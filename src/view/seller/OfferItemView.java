package view.seller;

import controller.ItemController;
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
import model.User;

public class OfferItemView extends BorderPane {
    private Label errorLbl;
    private TableView<Item> table;
    private Button acceptBtn, declineBtn, backBtn;
    private Stage stage;
    private User user;

    public OfferItemView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;

        table = new TableView<>();
        acceptBtn = new Button("Accept");
        declineBtn = new Button("Decline");
        backBtn = new Button("Back");

        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);

        setTableLayout();
        setLayout();
        setEvents();
        refreshTable();

        Scene scene = new Scene(this, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void setTableLayout() {
        TableColumn<Item, String> name = new TableColumn<>("Item Name");
        name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        
        TableColumn<Item, String> price = new TableColumn<>("Offered Price");
        price.setCellValueFactory(new PropertyValueFactory<>("offered_price")); // pastikan ini memanggil getter yang benar

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(name, price);
    }


    private void setLayout() {
        GridPane topGP = new GridPane();
        topGP.add(backBtn, 0, 0);
        topGP.add(acceptBtn, 1, 0);
        topGP.add(declineBtn, 2, 0);
        topGP.add(errorLbl, 3, 0);

        this.setTop(topGP);
        this.setCenter(new ScrollPane(table));
    }

    private void setEvents() {
        backBtn.setOnAction(e -> new HomeSellerView(stage, user));

        acceptBtn.setOnAction(e -> {
            Item selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                    // Setelah transaksi berhasil, perbarui status item menjadi "Purchased"
                    String updatePurchase = ItemController.updatePurchase(selectedItem.getItem_id());
                    
                    if (updatePurchase.equals("Success")) {
                        // Pembelian sukses, ubah status item dan buat transaksi
                        String acceptOfferResponse = ItemController.acceptOffer(selectedItem.getItem_id(), selectedItem.getOffered_price());

                        if (acceptOfferResponse.equals("Offer accepted, transaction created.")) {
                            errorLbl.setText("Offer accepted and transaction created successfully.");
                            refreshTable();
                        } else {
                            errorLbl.setText("Error in accepting the offer: " + acceptOfferResponse);
                        }
                    } else {
                        errorLbl.setText("Error updating item status to Purchased.");
                    }
                
            } else {
                errorLbl.setText("Please select an offer to accept.");
            }
        });

        declineBtn.setOnAction(e -> {
            Item selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String response = ItemController.declineOffer(selectedItem.getItem_id());
                errorLbl.setText(response);
                refreshTable();
            } else {
                errorLbl.setText("Please select an offer to decline.");
            }
        });
    }


    private void refreshTable() {
        ObservableList<Item> offeredItems = ItemController.viewOfferedItems(user.getUser_id());
        table.setItems(offeredItems);
    }
}

