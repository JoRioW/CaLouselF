package view.admin;

import controller.ItemController;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
 * Kelas HomeAdminView adalah tampilan utama bagi pengguna dengan peran "Admin".
 * Tampilan ini memungkinkan admin untuk menyetujui atau menolak item yang ditampilkan di tabel.
 * Admin juga dapat melakukan logout dari aplikasi.
 */
public class HomeAdminView extends BorderPane {

    // Deklarasi variabel komponen UI
    private Label errorLbl;
    private Button approveItemBtn, declineItemBtn, logoutBtn;
    private TableView<Item> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private TableColumn<Item, String> name, category, size, price;
    private Item selectedItem;
    private Stage stage, popUp;
    private User user;

    /*
     * Fungsi init() untuk menginisialisasi komponen UI.
     * Semua komponen seperti tabel, tombol, label, dan grid dibuat di sini.
     */
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

    /*
     * Fungsi setTableLayout() untuk mengatur kolom-kolom di dalam tabel.
     * Kolom yang ditambahkan:
     * - name: Nama item
     * - category: Kategori item
     * - size: Ukuran item
     * - price: Harga item
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
     * Fungsi setLayout() untuk mengatur tata letak komponen UI.
     * Komponen atas berisi tombol approve, decline, logout, dan label error.
     * Tabel ditampilkan di bagian tengah dengan ScrollPane.
     */
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

    /*
     * Fungsi setEvents() untuk menetapkan event handler pada tombol dan tabel.
     * - approveItemBtn: Menyetujui item yang dipilih.
     * - declineItemBtn: Membuka pop-up untuk memasukkan alasan penolakan.
     * - logoutBtn: Mengarahkan pengguna ke halaman login.
     */
    private void setEvents() {
        table.setOnMouseClicked(e -> selectedItem = table.getSelectionModel().getSelectedItem());

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
            if (selectedItem != null) {
                popUpDeclineAction();
            } else {
                errorLbl.setText("No item selected to decline.");
            }
        });

        logoutBtn.setOnAction(e -> new LoginView(stage));
    }

    /*
     * Fungsi popUpDeclineAction() untuk menampilkan pop-up input alasan penolakan.
     * - Validasi input: Alasan tidak boleh kosong.
     * - Jika valid, item akan ditolak dan tabel diperbarui.
     */
    private void popUpDeclineAction() {
        popUp = new Stage();
        popUp.setTitle("Decline Item Reason");

        Label reasonLbl = new Label("Reason for Decline:");
        TextArea reasonTA = new TextArea();
        Button submitBtn = new Button("Submit");
        Button cancelBtn = new Button("Cancel");
        Label errorLblPopup = new Label();
        errorLblPopup.setTextFill(Color.RED);

        GridPane popUpGP = new GridPane();
        popUpGP.setAlignment(Pos.CENTER);
        popUpGP.setVgap(10);
        popUpGP.setHgap(10);

        popUpGP.add(reasonLbl, 0, 0);
        popUpGP.add(reasonTA, 0, 1, 2, 1);
        popUpGP.add(submitBtn, 0, 2);
        popUpGP.add(cancelBtn, 1, 2);
        popUpGP.add(errorLblPopup, 0, 3, 2, 1);

        Scene popUpScene = new Scene(popUpGP, 400, 300);
        popUp.setScene(popUpScene);
        popUp.show();

        submitBtn.setOnAction(e -> {
            String reason = reasonTA.getText().trim();

            if (reason.isEmpty()) {
                errorLblPopup.setText("Reason cannot be empty.");
            } else {
                String response = ItemController.deleteItem(selectedItem.getItem_id());
                if ("Success".equals(response)) {
                    errorLbl.setText("Item declined: " + reason);
                    refreshTable();
                    popUp.close();
                } else {
                    errorLblPopup.setText(response);
                }
            }
        });

        cancelBtn.setOnAction(e -> popUp.close());
    }

    /*
     * Fungsi refreshTable() untuk memperbarui data tabel.
     * Data diambil dari database menggunakan ItemController.
     */
    private void refreshTable() {
        ObservableList<Item> items = ItemController.viewItem(user.getUser_id(), user.getRoles());
        table.setItems(items);
    }

    /*
     * Konstruktor HomeAdminView(Stage stage, User user)
     * - Mengatur tampilan admin dengan memanggil fungsi inisialisasi, layout, dan event handler.
     * - Data tabel dimuat dan ditampilkan.
     */
    public HomeAdminView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
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
