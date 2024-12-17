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

/*
 * Kelas PurchaseHistoryView menampilkan riwayat pembelian item oleh pengguna.
 * Menampilkan tabel yang berisi detail transaksi yang telah dilakukan oleh pengguna, termasuk ID transaksi, nama item, kategori, ukuran, dan harga.
 */
public class PurchaseHistoryView extends BorderPane {
    
    // Deklarasi variabel untuk komponen UI
    private Label errorLbl;
    private TableView<Transaction> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private TableColumn<Transaction, String> id, name, category, size, price;
    private Item selectedItem;
    private User user;
    private Stage stage;
    private Button backBtn;

    /*
     * Inisialisasi komponen UI yang diperlukan untuk tampilan riwayat pembelian.
     * - Membuat tabel transaksi dan tombol untuk kembali ke halaman sebelumnya.
     */
    private void init() {
        backBtn = new Button("Back");
        table = new TableView<>();
        tableSP = new ScrollPane(table);
        topGP = new GridPane();
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);
    }

    /*
     * Menetapkan layout untuk tabel riwayat pembelian.
     * - Menambahkan kolom untuk ID transaksi, nama item, kategori, ukuran, dan harga.
     */
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

    /*
     * Menetapkan layout UI untuk tampilan riwayat pembelian.
     * - Menambahkan tombol "Back" dan label error ke bagian atas tampilan.
     * - Menyusun tabel untuk menampilkan transaksi.
     */
    private void setLayout() {
        topGP.add(backBtn, 0, 0);
        topGP.add(errorLbl, 1, 0);
        this.setTop(topGP);

        tableSP.setFitToWidth(true);
        tableSP.setFitToHeight(true);
        this.setCenter(tableSP);
    }

    /*
     * Menetapkan event handler untuk tombol "Back".
     * - Tombol "Back" akan mengarahkan pengguna kembali ke halaman utama pembeli (HomeBuyerView).
     */
    private void setEvents() {
        backBtn.setOnAction(e -> {
            new HomeBuyerView(stage, user);
        });
    }

    /*
     * Memuat ulang tabel dengan data transaksi terbaru dari pengguna.
     */
    private void refreshTable() {
        ObservableList<Transaction> transactions = TransactionController.viewPurchaseHistory(user.getUser_id());
        table.setItems(transactions);
    }

    /*
     * Konstruktor utama untuk PurchaseHistoryView.
     * - Menerima Stage dan User sebagai parameter untuk menginisialisasi tampilan dan memuat data transaksi pengguna.
     */
    public PurchaseHistoryView(Stage stage, User user) {
        this.user = user;
        this.stage = stage;

        init();
        setTableLayout();
        setLayout();
        setEvents();
        refreshTable();

        System.out.println(user.getUser_id());  // Debugging: Menampilkan user ID

        Scene scene = new Scene(this, 600, 600);
        stage.setScene(scene);  // Mengatur scene dengan tampilan PurchaseHistoryView
        stage.show();
    }
}

