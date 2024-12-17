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

/*
 * Kelas OfferItemView adalah tampilan untuk seller yang menampilkan daftar item yang telah menerima tawaran harga dari pembeli.
 * Seller dapat menerima atau menolak tawaran harga yang diberikan pembeli.
 */
public class OfferItemView extends BorderPane {
    private Label errorLbl;  // Label untuk menampilkan pesan error
    private TableView<Item> table;  // Tabel untuk menampilkan item yang memiliki tawaran harga
    private Button acceptBtn, declineBtn, backBtn;  // Tombol untuk menerima tawaran, menolak tawaran, dan kembali ke halaman sebelumnya
    private Stage stage;  // Stage untuk menampilkan tampilan
    private User user;  // User yang sedang login (seller)

    /*
     * Konstruktor utama untuk tampilan OfferItemView.
     * - Menyiapkan dan menampilkan komponen-komponen UI.
     * - Memuat item yang memiliki tawaran harga untuk ditampilkan di tabel.
     */
    public OfferItemView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;

        table = new TableView<>();
        acceptBtn = new Button("Accept");
        declineBtn = new Button("Decline");
        backBtn = new Button("Back");

        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);

        setTableLayout();  // Mengatur layout tabel
        setLayout();  // Mengatur tata letak komponen UI
        setEvents();  // Menetapkan event handler untuk tombol
        refreshTable();  // Menyegarkan tabel dengan data item yang memiliki tawaran

        Scene scene = new Scene(this, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    /*
     * Menetapkan layout untuk tabel dengan kolom Item Name dan Offered Price.
     * - Kolom pertama menampilkan nama item.
     * - Kolom kedua menampilkan harga tawaran yang diajukan oleh pembeli.
     */
    private void setTableLayout() {
        TableColumn<Item, String> name = new TableColumn<>("Item Name");
        name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        
        TableColumn<Item, String> price = new TableColumn<>("Offered Price");
        price.setCellValueFactory(new PropertyValueFactory<>("offered_price"));  // Pastikan ini memanggil getter yang benar

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(name, price);
    }

    /*
     * Menetapkan layout tampilan dengan tombol-tombol dan tabel.
     * - Tombol backBtn untuk kembali ke halaman sebelumnya.
     * - Tombol acceptBtn untuk menerima tawaran harga.
     * - Tombol declineBtn untuk menolak tawaran harga.
     * - Menampilkan tabel yang memuat daftar item yang memiliki tawaran harga.
     */
    private void setLayout() {
        GridPane topGP = new GridPane();
        topGP.add(backBtn, 0, 0);
        topGP.add(acceptBtn, 1, 0);
        topGP.add(declineBtn, 2, 0);
        topGP.add(errorLbl, 3, 0);

        this.setTop(topGP);
        this.setCenter(new ScrollPane(table));
    }

    /*
     * Menetapkan event handler untuk tombol-tombol.
     * - Tombol acceptBtn: Menerima tawaran harga yang dipilih dan mengubah status item menjadi "Purchased".
     * - Tombol declineBtn: Menolak tawaran harga yang dipilih dan mengubah status tawaran item menjadi "Not Offered".
     * - Tombol backBtn: Kembali ke tampilan HomeSellerView.
     */
    private void setEvents() {
        backBtn.setOnAction(e -> new HomeSellerView(stage, user));  // Navigasi ke tampilan HomeSellerView

        acceptBtn.setOnAction(e -> {
            Item selectedItem = table.getSelectionModel().getSelectedItem();  // Mengambil item yang dipilih
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
                errorLbl.setText("Please select an offer to accept.");  // Pesan error jika tidak ada tawaran yang dipilih
            }
        });

        declineBtn.setOnAction(e -> {
            Item selectedItem = table.getSelectionModel().getSelectedItem();  // Mengambil item yang dipilih
            if (selectedItem != null) {
                String response = ItemController.declineOffer(selectedItem.getItem_id());  // Menolak tawaran yang dipilih
                errorLbl.setText(response);
                refreshTable();  // Menyegarkan tabel setelah tawaran ditolak
            } else {
                errorLbl.setText("Please select an offer to decline.");  // Pesan error jika tidak ada tawaran yang dipilih
            }
        });
    }

    /*
     * Menyegarkan tabel dengan mengambil data item yang memiliki tawaran harga dari pembeli.
     * - Mengambil item yang memiliki tawaran dari controller dan menampilkan di tabel.
     */
    private void refreshTable() {
        ObservableList<Item> offeredItems = ItemController.viewOfferedItems(user.getUser_id());
        table.setItems(offeredItems);
    }
}


