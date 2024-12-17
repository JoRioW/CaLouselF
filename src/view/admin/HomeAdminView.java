package view.admin;

import controller.ItemController;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
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
 * Kelas HomeAdminView adalah tampilan utama bagi pengguna dengan peran "Admin".
 * Tampilan ini memungkinkan admin untuk menyetujui atau menolak item yang ditampilkan di tabel.
 * Admin juga dapat melakukan logout dari aplikasi.
 */
public class HomeAdminView extends BorderPane {

    // Deklarasi variabel untuk komponen UI
    private Label errorLbl;
    private Button approveItemBtn, declineItemBtn, logoutBtn;
    private TableView<Item> table;
    private ScrollPane tableSP;
    private GridPane topGP;
    private TableColumn<Item, String> name, category, size, price;
    private Item selectedItem;
    private Stage stage;
    private User user;

    /*
     * Inisialisasi komponen UI.
     * Menyiapkan tabel, tombol, dan label untuk UI.
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
     * Menetapkan event handler untuk tombol dan tabel.
     * - approveItemBtn: Menyetujui item yang dipilih.
     * - declineItemBtn: Menolak item yang dipilih.
     * - logoutBtn: Melakukan logout dan kembali ke halaman login.
     */
    private void setEvents() {
        // Menangani klik pada tabel untuk memilih item
        table.setOnMouseClicked(e -> {
            selectedItem = table.getSelectionModel().getSelectedItem();
        });

        // Menangani aksi tombol approve
        approveItemBtn.setOnAction(e -> {
            if (selectedItem != null) {
                String id = selectedItem.getItem_id();
                String message = ItemController.approveItem(id);

                if ("Item approved successfully".equals(message)) {
                    refreshTable();  // Refresh tabel setelah item disetujui
                } else {
                    errorLbl.setText(message);  // Menampilkan pesan error jika gagal
                }
            } else {
                errorLbl.setText("No item selected to approve.");
            }
        });

        // Menangani aksi tombol decline
        declineItemBtn.setOnAction(e -> {
            if(selectedItem != null) {
                String id = selectedItem.getItem_id();
                String message = ItemController.deleteItem(id);
                
                if("Success".equals(message)) {
                    refreshTable();  // Refresh tabel setelah item ditolak
                }
                else {
                    errorLbl.setText(message);  // Menampilkan pesan error jika gagal
                }
            }
            else {
                errorLbl.setText("No item selected to decline.");
            }
        });

        // Menangani aksi tombol logout
        logoutBtn.setOnAction(e -> {
            new LoginView(stage);  // Arahkan ke halaman login
        });
    }

    /*
     * Merefresh tabel dengan data terbaru setelah aksi dilakukan (misalnya, item disetujui atau ditolak).
     */
    private void refreshTable() {
        ObservableList<Item> items = ItemController.viewItem(user.getUser_id(), user.getRoles());
        table.setItems(items);
    }

    /*
     * Konstruktor untuk menginisialisasi HomeAdminView.
     * Menerima objek Stage dan User sebagai parameter untuk pengaturan tampilan.
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

