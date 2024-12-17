package view.seller;

import controller.ItemController;
import controller.TransactionController;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
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
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Item;
import model.User;
import view.LoginView;

/*
 * Kelas HomeSellerView adalah tampilan utama untuk penjual (Seller) setelah login.
 * Penjual dapat mengunggah item baru, memperbarui item yang sudah ada, menghapus item, melihat tawaran harga dari pembeli, dan keluar dari aplikasi.
 * Tampilan ini juga menyediakan tabel untuk menampilkan item yang dimiliki oleh penjual.
 */
public class HomeSellerView extends BorderPane {
    private static final String SUCCESS = "Success";  // Constant untuk status sukses

    private Label errorLbl;  // Label untuk menampilkan pesan error
    private Button uploadItemBtn, updateItemBtn, deleteItemBtn, viewOfferItemBtn, logoutBtn;  // Tombol untuk berbagai tindakan
    private TableView<Item> table;  // Tabel untuk menampilkan daftar item
    private ScrollPane tableSP;  // ScrollPane untuk tabel
    private GridPane topGP;  // GridPane untuk tata letak tombol
    private TableColumn<Item, String> name, category, size, price;  // Kolom tabel untuk nama, kategori, ukuran, dan harga
    private Item selectedItem;  // Item yang dipilih
    private Stage stage;  // Stage untuk menampilkan tampilan
    private User user;  // User yang sedang login

    /*
     * Inisialisasi komponen-komponen UI untuk tampilan Home Seller.
     * - Membuat tombol, tabel, dan komponen UI lainnya.
     */
    private void init() {
        table = new TableView<>();
        uploadItemBtn = new Button("Upload");
        updateItemBtn = new Button("Edit");
        deleteItemBtn = new Button("Delete");
        viewOfferItemBtn = new Button("View Offer");
        logoutBtn = new Button("Logout");
        tableSP = new ScrollPane(table);
        topGP = new GridPane();
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);
    }

    /*
     * Menetapkan layout tabel dengan menambahkan kolom untuk item.
     * - Kolom yang ditambahkan: nama, kategori, ukuran, dan harga item.
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
     * Menetapkan layout tampilan dengan meletakkan tombol-tombol dan tabel di tempat yang sesuai.
     * - Tombol untuk mengunggah, memperbarui, menghapus item, melihat tawaran, dan logout.
     */
    private void setLayout() {
        topGP.add(uploadItemBtn, 0, 0);
        topGP.add(updateItemBtn, 1, 0);
        topGP.add(deleteItemBtn, 2, 0);
        topGP.add(viewOfferItemBtn, 3, 0);
        topGP.add(logoutBtn, 4, 0);
        topGP.add(errorLbl, 4, 0);
        GridPane.setHalignment(errorLbl, HPos.RIGHT);
        GridPane.setHgrow(errorLbl, Priority.ALWAYS);
        topGP.setHgap(10);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }

    /*
     * Menetapkan event handler untuk tombol-tombol.
     * - Tombol uploadItemBtn: Menampilkan tampilan untuk mengunggah item.
     * - Tombol updateItemBtn: Menampilkan tampilan untuk mengedit item yang dipilih.
     * - Tombol deleteItemBtn: Menghapus item yang dipilih.
     * - Tombol viewOfferItemBtn: Menampilkan tampilan untuk melihat tawaran harga dari pembeli.
     * - Tombol logoutBtn: Mengarahkan pengguna ke tampilan login.
     */
    private void setEvents() {
        uploadItemBtn.setOnAction(e -> {
            new UploadFormView(stage, user);  // Navigasi ke tampilan Upload Form
        });

        table.setOnMouseClicked(e -> {
            selectedItem = table.getSelectionModel().getSelectedItem();  // Menyimpan item yang dipilih
        });

        updateItemBtn.setOnAction(e -> {
            if (selectedItem != null) {
                new EditSellerView(stage, selectedItem, user);  // Navigasi ke tampilan Edit Seller
            } else {
                errorLbl.setText("No item selected to update.");  // Pesan error jika tidak ada item yang dipilih
            }
        });

        deleteItemBtn.setOnAction(e -> {
            if (selectedItem != null) {
                String id = selectedItem.getItem_id();
                String message = ItemController.deleteItem(id);  // Menghapus item yang dipilih

                if (SUCCESS.equals(message)) {
                    refreshTable();  // Menyegarkan tabel jika item berhasil dihapus
                } else {
                    errorLbl.setText(message);  // Menampilkan pesan error jika gagal menghapus item
                }
            } else {
                errorLbl.setText("No item selected to delete.");  // Pesan error jika tidak ada item yang dipilih
            }
        });
        
        viewOfferItemBtn.setOnAction(e -> {
            new OfferItemView(stage, user);  // Navigasi ke tampilan View Offer Item
        });
        
        logoutBtn.setOnAction(e -> {
            user = null;
            new LoginView(stage);  // Navigasi ke tampilan Login setelah logout
        });
    }

    /*
     * Menyegarkan tabel dengan mengambil data item yang dimiliki oleh seller.
     * - Data diambil menggunakan controller dan ditampilkan di tabel.
     */
    private void refreshTable() {
        ObservableList<Item> items = ItemController.viewItem(user.getUser_id(), user.getRoles());
        table.setItems(items);
    }

    /*
     * Konstruktor utama untuk tampilan HomeSellerView.
     * - Menginisialisasi tampilan dan memuat data item seller.
     * - Menampilkan tampilan pada stage.
     */
    public HomeSellerView(Stage stage, User user) {
        this.user = user;
        this.stage = stage;

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

