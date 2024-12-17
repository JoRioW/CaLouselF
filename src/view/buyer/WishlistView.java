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

/*
 * Kelas WishlistView menampilkan daftar item yang ada di wishlist pengguna.
 * Menampilkan tabel yang berisi item yang ditambahkan ke wishlist, termasuk nama item, kategori, ukuran, dan harga.
 */
public class WishlistView extends BorderPane {

    // Deklarasi variabel untuk komponen UI
    private Label errorLbl;
    private TableView<Item> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private TableColumn<Item, String> name, category, size, price;
    private Item selectedItem;
    private User user;
    private Stage stage;
    private Button backBtn, removeBtn;

    /*
     * Inisialisasi komponen UI yang diperlukan untuk tampilan wishlist.
     * - Membuat tabel wishlist dan tombol untuk navigasi serta menghapus item dari wishlist.
     */
    private void init() {
        backBtn = new Button("Back");
        removeBtn = new Button("Remove");
        table = new TableView<>();
        tableSP = new ScrollPane(table);
        topGP = new GridPane();
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);
    }

    /*
     * Menetapkan layout untuk tabel wishlist.
     * - Menambahkan kolom untuk nama item, kategori, ukuran, dan harga.
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
     * Menetapkan layout UI untuk tampilan wishlist.
     * - Menambahkan tombol "Back" dan "Remove" ke bagian atas tampilan.
     * - Menyusun tabel untuk menampilkan item wishlist.
     */
    private void setLayout() {
        topGP.add(backBtn, 0, 0);
        topGP.add(removeBtn, 1, 0);
        topGP.add(errorLbl, 0, 2);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }

    /*
     * Menetapkan event handler untuk tombol-tombol dan interaksi lainnya.
     * - Tombol "Back" mengarahkan pengguna kembali ke halaman utama pembeli.
     * - Tombol "Remove" menghapus item yang dipilih dari wishlist.
     * - Pengguna dapat memilih item dari tabel untuk dihapus.
     */
    private void setEvents() {
        backBtn.setOnAction(e -> {
            new HomeBuyerView(stage, user);  // Navigasi ke halaman utama pembeli
        });

        // Ketika tabel di klik, menyimpan item yang dipilih
        table.setOnMouseClicked(e -> {
            selectedItem = table.getSelectionModel().getSelectedItem();
        });

        // Event ketika tombol "Remove" diklik untuk menghapus item dari wishlist
        removeBtn.setOnAction(e -> {
            String itemId = selectedItem.getItem_id();
            String userId = user.getUser_id();
            if (selectedItem != null) {
                String message = WishlistController.removeWishlist(userId, itemId);
                if (message.equals("Success")) {
                    errorLbl.setText(message);
                } else {
                    errorLbl.setText(message);
                }
            } else {
                errorLbl.setText("No item selected to remove from wishlist.");
            }
        });
    }

    /*
     * Memuat ulang tabel wishlist dengan data terbaru.
     */
    private void refreshTable() {
        ObservableList<Item> wishlist = WishlistController.viewWishlist(user.getUser_id());
        table.setItems(wishlist);
    }

    /*
     * Konstruktor utama untuk WishlistView.
     * - Menerima Stage dan User sebagai parameter untuk menginisialisasi tampilan dan memuat data wishlist pengguna.
     */
    public WishlistView(Stage stage, User user) {
        this.user = user;
        this.stage = stage;

        init();
        setTableLayout();
        setLayout();
        setEvents();
        refreshTable();

        Scene scene = new Scene(this, 600, 600);
        stage.setScene(scene);  // Mengatur scene dengan tampilan WishlistView
        stage.show();
    }
}

