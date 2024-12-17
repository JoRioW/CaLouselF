package view.buyer;

import controller.ItemController;
import controller.TransactionController;
import controller.WishlistController;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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

/*
 * Kelas HomeBuyerView adalah tampilan utama bagi pengguna dengan peran "Buyer".
 * Tampilan ini memungkinkan pembeli untuk melihat daftar item yang tersedia, 
 * menambahkan item ke wishlist, melakukan tawaran harga, dan membeli item.
 */
public class HomeBuyerView extends BorderPane {
    private static final String SUCCESS = "Success";

    // Deklarasi komponen UI
    private Label errorLbl;
    private TableView<Item> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private Button viewWishlistBtn, viewHistoryBtn, offerPriceBtn,
    purchaseItemBtn, addWishlistBtn, logoutBtn;
    private TableColumn<Item, String> name, category, size, price;
    private Item selectedItem;
    private User user;
    private Stage stage, popUp;

    /*
     * Inisialisasi komponen UI.
     * Menyiapkan tabel, tombol, dan label untuk UI.
     */
    private void init() {
        table = new TableView<>();
        viewWishlistBtn = new Button("View Wishlist");
        viewHistoryBtn = new Button("View Purchase History");
        offerPriceBtn = new Button("Offer Price");
        purchaseItemBtn = new Button("Purchase Item");
        addWishlistBtn = new Button("Add Wishlist");
        logoutBtn = new Button("Logout");
        tableSP = new ScrollPane(table);
        topGP = new GridPane();
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);
    }

    /*
     * Mengatur layout tabel dengan kolom yang sesuai untuk item yang ditampilkan.
     * Kolom yang ditampilkan: Nama Item, Kategori Item, Ukuran Item, Harga Item.
     */
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

    /*
     * Mengatur layout tampilan dengan menambahkan tombol dan tabel ke grid dan posisi yang sesuai.
     */
    private void setLayout() {
        topGP.add(viewWishlistBtn, 0, 0);
        topGP.add(viewHistoryBtn, 1, 0);
        topGP.add(offerPriceBtn, 2, 0);
        topGP.add(purchaseItemBtn, 0, 1);
        topGP.add(addWishlistBtn, 1, 1);
        topGP.add(logoutBtn, 3, 0);
        topGP.add(errorLbl, 0, 2);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }

    /*
     * Menetapkan event handler untuk tombol dan tabel.
     * - viewWishlistBtn: Menampilkan tampilan wishlist pembeli.
     * - addWishlistBtn: Menambahkan item ke wishlist pembeli.
     * - purchaseItemBtn: Menampilkan pop-up konfirmasi pembelian.
     * - offerPriceBtn: Menampilkan tampilan tawar harga untuk item yang dipilih.
     * - logoutBtn: Melakukan logout dan kembali ke halaman login.
     */
    private void setEvents() {
        table.setOnMouseClicked(e -> {
            selectedItem = table.getSelectionModel().getSelectedItem();
        });
        
        viewWishlistBtn.setOnAction(e -> {
            new WishlistView(stage, user);  // Navigasi ke halaman wishlist
        });

        addWishlistBtn.setOnAction(e -> {
            if (selectedItem != null) {
                String message = WishlistController.addWishlist(user.getUser_id(), selectedItem.getItem_id());
                if (message.equals(SUCCESS)) {
                    errorLbl.setText(message);  // Menampilkan pesan sukses
                } else {
                    errorLbl.setText(message);  // Menampilkan pesan error jika gagal
                }
            } else {
                errorLbl.setText("No item selected to add to wishlist.");  // Pesan jika tidak ada item yang dipilih
            }
        });
        
        purchaseItemBtn.setOnAction(e -> {
            if (selectedItem != null) {
                popUpAction();  // Menampilkan pop-up untuk konfirmasi pembelian
            } else {
                errorLbl.setText("No item selected to purchase.");  // Pesan jika tidak ada item yang dipilih
            }
        });
        
        viewHistoryBtn.setOnAction(e -> {
            new PurchaseHistoryView(stage, user);  // Navigasi ke halaman riwayat pembelian
        });
        
        offerPriceBtn.setOnAction(e -> {
            if (selectedItem != null) {
                new OfferPriceFormView(stage, user, selectedItem);  // Menampilkan form tawar harga
            } else {
                errorLbl.setText("No item selected to offer.");  // Pesan jika tidak ada item yang dipilih
            }
        });
        
        logoutBtn.setOnAction(e -> {
            user = null;  // Menghapus data pengguna
            new LoginView(stage);  // Navigasi ke halaman login
        });
    }

    /*
     * Menampilkan pop-up untuk konfirmasi pembelian item.
     * Pop-up ini berisi tombol untuk membeli atau membatalkan pembelian.
     */
    private void popUpAction() {
        popUp = new Stage();
        popUp.setTitle("Purchase Confirmation");
        Button purchaseBtn = new Button("Purchase");
        Button declineBtn = new Button("Decline");
        GridPane popUpGP = new GridPane();
        popUpGP.add(purchaseBtn, 0, 0);
        popUpGP.add(declineBtn, 0, 1);
        popUpGP.setAlignment(Pos.CENTER);
        Scene popUpScene = new Scene(popUpGP, 400, 200);

        popUp.setScene(popUpScene);
        popUp.show();
        
        purchaseBtn.setOnAction(e -> {
            String message = TransactionController.purchaseItem(user.getUser_id(), selectedItem.getItem_id());
            if (message.equals(SUCCESS)) {
                String updatePurchase = ItemController.updatePurchase(selectedItem.getItem_id());
                if (updatePurchase.equals(SUCCESS)) {
                    errorLbl.setText(message);  // Menampilkan pesan sukses
                    refreshTable();  // Refresh tabel setelah pembelian
                } else {
                    errorLbl.setText(updatePurchase);  // Menampilkan pesan error jika gagal mengupdate status item
                }
            } else {
                errorLbl.setText(message);  // Menampilkan pesan error jika gagal membeli item
            }
            popUp.close();
        });
        
        declineBtn.setOnAction(e -> {
            popUp.close();  // Menutup pop-up jika pembelian dibatalkan
        });
    }

    /*
     * Merefresh tabel dengan data terbaru setelah aksi dilakukan (misalnya, item dibeli atau ditambahkan ke wishlist).
     */
    private void refreshTable() {
        ObservableList<Item> items = ItemController.viewItem(user.getUser_id(), user.getRoles());
        table.setItems(items);
    }

    /*
     * Konstruktor untuk menginisialisasi HomeBuyerView.
     * Menerima objek Stage dan User sebagai parameter untuk pengaturan tampilan.
     */
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

