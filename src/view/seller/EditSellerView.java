package view.seller;

import controller.ItemController;
import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Item;
import model.User;
import view.LoginView;

/*
 * Kelas EditSellerView menyediakan tampilan untuk seller (penjual) guna mengedit detail item yang telah diunggah.
 * Tampilan ini memungkinkan seller untuk mengubah nama, kategori, ukuran, dan harga item yang sudah ada.
 * Setelah melakukan perubahan, seller dapat menyimpan perubahan tersebut.
 */
public class EditSellerView extends BorderPane{
    private Stage stage;  // Stage untuk menampilkan tampilan
    private Item item;  // Item yang akan diedit
    private GridPane topGP, centerGP;  // Layout untuk bagian atas dan tengah tampilan
    
    private Label titleLbl, errorLbl;  // Label untuk judul dan error message
    private TextField itemNameTF, itemCategoryTF, itemSizeTF, itemPriceTF;  // TextField untuk input data item
    private Button submitBtn, backBtn;  // Tombol submit dan kembali
    
    private User user;  // User yang sedang login

    /*
     * Inisialisasi komponen UI yang diperlukan untuk tampilan Edit Seller.
     * - Membuat field input, tombol, dan label untuk tampilan.
     */
    private void init() {
        centerGP = new GridPane();
        topGP = new GridPane();
        
        titleLbl = new Label("Edit Item");
        itemNameTF = new TextField(item.getItem_name());
        itemCategoryTF = new TextField(item.getItem_category());
        itemSizeTF = new TextField(item.getItem_size());
        itemPriceTF = new TextField(item.getItem_price());
        errorLbl = new Label();
        
        // Menambahkan prompt text pada field input untuk membantu pengguna.
        itemNameTF.setPromptText("Item Name");
        itemCategoryTF.setPromptText("Item Category");
        itemSizeTF.setPromptText("Item Size");
        itemPriceTF.setPromptText("Item Price");
        
        backBtn = new Button("Back");
        submitBtn = new Button("Submit");
    }

    /*
     * Mengatur layout tampilan dengan meletakkan komponen-komponen UI dalam posisi yang diinginkan.
     * - Menambahkan input field ke dalam grid.
     * - Menambahkan tombol dan label untuk error message.
     */
    private void setLayout() {
        centerGP.add(itemNameTF, 0, 0);
        centerGP.add(itemCategoryTF, 0, 1);
        centerGP.add(itemSizeTF, 0, 2);
        centerGP.add(itemPriceTF, 0, 3);
        centerGP.add(errorLbl, 0, 5);
        centerGP.add(submitBtn, 0, 7);
    
        topGP.add(backBtn, 0, 0);
        topGP.add(titleLbl, 0,1);
        this.setTop(topGP);
        topGP.setAlignment(Pos.CENTER_LEFT);
        
        this.setCenter(centerGP);
        centerGP.setVgap(10);
        centerGP.setAlignment(Pos.CENTER);
    }

    /*
     * Mengatur gaya tampilan untuk komponen UI.
     * - Menambahkan font yang lebih besar untuk title label.
     * - Mengubah warna teks error menjadi merah.
     */
    private void setStyle() {
        titleLbl.setFont(Font.font(null, FontWeight.BOLD ,24));
        errorLbl.setTextFill(Color.RED);
    }

    /*
     * Mengatur event handler untuk tombol-tombol yang ada.
     * - Tombol backBtn untuk kembali ke halaman HomeSellerView.
     * - Tombol submitBtn untuk menyimpan perubahan item yang telah diedit.
     */
    private void setEvents() {
        backBtn.setOnAction(e -> {
            new HomeSellerView(stage, user);  // Navigasi kembali ke halaman home seller
        });
        
        submitBtn.setOnAction(e -> {
            String ItemId = item.getItem_id();
            String ItemName = itemNameTF.getText();
            String ItemCategory = itemCategoryTF.getText();
            String ItemSize = itemSizeTF.getText();
            String ItemPrice = itemPriceTF.getText();
            
            // Validasi input item sebelum menyimpannya
            String errorMessage = ItemController.checkItemValidation(ItemName, ItemName, ItemSize, ItemPrice);
            
            if (errorMessage.equals("Success")) {
                String editItem = ItemController.editItem(ItemId, ItemName, ItemCategory, ItemSize, ItemPrice);
                
                if (editItem.equals("Success")) {
                    System.out.println(editItem);
                    new HomeSellerView(stage, user);  // Navigasi ke halaman home seller setelah item berhasil diubah
                } else {
                    errorLbl.setText(editItem);  // Menampilkan error jika gagal mengubah item
                }
            } else {
                errorLbl.setText(errorMessage);  // Menampilkan error jika input tidak valid
            }
        });
    }

    /*
     * Konstruktor utama yang menginisialisasi tampilan Edit Seller.
     * - Menerima parameter stage, item, dan user untuk mempersiapkan tampilan dan memuat data item yang akan diedit.
     */
    public EditSellerView(Stage stage, Item item, User user) {
        this.stage = stage;
        this.item = item;
        this.user = user;
        
        init();
        setLayout();
        setStyle();
        setEvents();
        
        Scene scene = new Scene(this, 500, 600);
        stage.setScene(scene);
        stage.show();
    }
}

