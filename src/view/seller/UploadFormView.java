package view.seller;

import controller.ItemController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;

/*
 * Kelas UploadFormView adalah tampilan yang memungkinkan seller untuk mengunggah item baru.
 * Seller dapat mengisi nama item, kategori, ukuran, dan harga item, kemudian mengirimkan data untuk disimpan.
 */
public class UploadFormView extends BorderPane {
    private TextField itemNameTF, itemCategoryTF, itemSizeTF, itemPriceTF;  // TextField untuk input item
    private Label errorLbl, titleLbl;  // Label untuk judul dan error
    private Button submitBtn, backBtn;  // Tombol untuk submit dan kembali
    
    private User user;  // Objek user yang sedang login (seller)
    
    private GridPane centerGP, topGP;  // GridPane untuk layout
    private Stage stage;  // Stage untuk menampilkan tampilan

    /*
     * Menginisialisasi komponen-komponen UI yang digunakan dalam tampilan ini.
     */
    private void init() {
        topGP = new GridPane();
        centerGP = new GridPane();
        
        backBtn = new Button("Back");
        titleLbl = new Label("Upload Items");
        
        itemNameTF = new TextField();
        itemCategoryTF = new TextField();
        itemSizeTF = new TextField();
        itemPriceTF = new TextField();
        
        // Menetapkan placeholder (prompt text) untuk masing-masing field input
        itemNameTF.setPromptText("Item Name");
        itemCategoryTF.setPromptText("Item Category");
        itemSizeTF.setPromptText("Item Size");
        itemPriceTF.setPromptText("Item Price");
        
        errorLbl = new Label();
        
        submitBtn = new Button("Submit");
    }

    /*
     * Mengatur tata letak komponen UI dalam tampilan ini.
     * - Memasukkan komponen-komponen ke dalam GridPane.
     * - Menambahkan elemen-elemen ke dalam top dan center layout.
     */
    private void setLayout() {
        topGP.add(backBtn, 0, 0);
        topGP.add(titleLbl, 1, 0);
        
        centerGP.add(itemNameTF, 0, 0);
        centerGP.add(itemCategoryTF, 0, 1);
        centerGP.add(itemSizeTF, 0, 2);
        centerGP.add(itemPriceTF, 0, 3);
        centerGP.add(errorLbl, 0, 5);
        centerGP.add(submitBtn, 0, 7);
        
        this.setCenter(centerGP);
        centerGP.setAlignment(Pos.CENTER);
        centerGP.setVgap(10);
        
        this.setTop(topGP);
        topGP.setAlignment(Pos.CENTER_LEFT);
    }

    /*
     * Menetapkan event handler untuk tombol-tombol dalam tampilan.
     * - Tombol backBtn untuk kembali ke tampilan sebelumnya (HomeSellerView).
     * - Tombol submitBtn untuk mengunggah item setelah validasi data.
     */
    private void setEvents() {
        backBtn.setOnAction(e -> {
            new HomeSellerView(stage, user);  // Navigasi kembali ke tampilan HomeSellerView
        });
        
        submitBtn.setOnAction(e -> {
            String ItemName = itemNameTF.getText();
            String ItemCategory = itemCategoryTF.getText();
            String ItemSize = itemSizeTF.getText();
            String ItemPrice = itemPriceTF.getText();
            String currentUserId = user.getUser_id();
            
            // Validasi inputan item
            String validationMessage = ItemController.checkItemValidation(ItemName, ItemCategory, ItemSize, ItemPrice);
            
            if (validationMessage.equals("Success")) {
                String uploadItemMessage = ItemController.uploadItem(currentUserId, ItemName, ItemCategory, ItemSize, ItemPrice);
                
                if (uploadItemMessage.equals("Success")) {
                    new HomeSellerView(stage, user);  // Setelah sukses, navigasi kembali ke tampilan HomeSellerView
                } else {
                    errorLbl.setText(uploadItemMessage);  // Tampilkan pesan error jika gagal upload item
                }
            } else {
                errorLbl.setText(validationMessage);  // Tampilkan pesan error jika validasi gagal
            }
        });
    }

    /*
     * Menetapkan gaya untuk tampilan ini.
     * - Menetapkan font untuk titleLbl (judul halaman).
     * - Menetapkan warna merah untuk label errorLbl.
     */
    private void setStyle() {
        titleLbl.setFont(Font.font(null, FontWeight.BOLD ,24));  // Font untuk titleLbl
        errorLbl.setTextFill(Color.RED);  // Warna untuk errorLbl
    }

    /*
     * Konstruktor untuk kelas UploadFormView.
     * - Menyiapkan tampilan dan komponen UI.
     * - Menampilkan tampilan di stage.
     */
    public UploadFormView(Stage stage, User user) {
        init();  // Inisialisasi komponen UI
        setLayout();  // Menyusun tata letak
        setEvents();  // Menetapkan event handler untuk tombol
        setStyle();  // Menetapkan gaya

        this.stage = stage;
        this.user = user;
        
        Scene scene = new Scene(this, 500, 500);
        stage.setScene(scene);  // Menampilkan tampilan pada stage
        stage.show();
    }
}

