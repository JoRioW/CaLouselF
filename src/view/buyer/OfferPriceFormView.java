package view.buyer;

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
import model.Item;
import model.User;
import view.seller.HomeSellerView;

/*
 * Kelas OfferPriceFormView adalah tampilan untuk formulir tawar harga pada item oleh pembeli.
 * Pembeli dapat melihat informasi item yang dipilih dan memasukkan harga tawaran mereka.
 */
public class OfferPriceFormView extends BorderPane {

    // Deklarasi variabel untuk komponen UI
    private Stage stage;
    private TextField itemNameTF, itemCategoryTF, itemSizeTF, itemPriceTF, offerPriceTF;
    private Label errorLbl;
    private Button backBtn, submitBtn;
    
    private User user;
    private Item item;
    
    private GridPane topGP, centerGP;

    /*
     * Inisialisasi komponen UI yang diperlukan untuk tampilan tawar harga.
     * - Formulir ini terdiri dari field untuk menampilkan informasi item dan memasukkan tawaran harga.
     */
    private void init() {
        topGP = new GridPane();
        centerGP = new GridPane();

        itemNameTF = new TextField(item.getItem_name());
        itemNameTF.setEditable(false);  // Menampilkan nama item yang tidak dapat diedit

        itemCategoryTF = new TextField(item.getItem_category());
        itemCategoryTF.setEditable(false);  // Menampilkan kategori item yang tidak dapat diedit

        itemSizeTF = new TextField(item.getItem_size());
        itemSizeTF.setEditable(false);  // Menampilkan ukuran item yang tidak dapat diedit

        itemPriceTF = new TextField(item.getItem_price());
        itemPriceTF.setEditable(false);  // Menampilkan harga item yang tidak dapat diedit
        
        errorLbl = new Label();  // Label untuk menampilkan pesan error
        
        offerPriceTF = new TextField(item.getOffered_price());  // TextField untuk memasukkan harga tawaran oleh pembeli

        backBtn = new Button("Back");  // Tombol untuk kembali ke halaman sebelumnya
        submitBtn = new Button("Submit");  // Tombol untuk mengirim tawaran harga
    }

    /*
     * Mengatur layout tampilan dengan menambahkan komponen ke dalam grid dan menentukan posisinya.
     * Menambahkan tombol dan formulir input ke dalam layout.
     */
    private void setLayout() {
        topGP.add(backBtn, 0, 0);
        
        centerGP.add(itemNameTF, 0, 0);
        centerGP.add(itemCategoryTF, 0, 1);
        centerGP.add(itemSizeTF, 0, 2);
        centerGP.add(itemPriceTF, 0, 3);
        centerGP.add(offerPriceTF, 0, 4);
        centerGP.add(errorLbl, 0, 6);
        centerGP.add(submitBtn, 0, 8);
        
        this.setCenter(centerGP);
        centerGP.setAlignment(Pos.CENTER);
        centerGP.setVgap(10);
        
        this.setTop(topGP);
        topGP.setAlignment(Pos.CENTER_LEFT);
    }

    /*
     * Menetapkan event handler untuk tombol "Back" dan "Submit".
     * - "Back": Mengarahkan pengguna kembali ke tampilan sebelumnya (HomeBuyerView).
     * - "Submit": Mengirim tawaran harga dan memprosesnya.
     */
    public void setEvents() {
        backBtn.setOnAction(e -> {
            new HomeBuyerView(stage, user);  // Navigasi kembali ke halaman HomeBuyerView
        });
        
        submitBtn.setOnAction(e -> {
            String itemId = item.getItem_id();
            String offerPrice = offerPriceTF.getText();  // Mengambil harga tawaran dari field

            // Validasi tawaran harga
            if (offerPrice.isEmpty()) {
                errorLbl.setText("Offer price cannot be empty.");  // Pesan jika tawaran kosong
                return;  // Tidak melanjutkan jika kosong
            }

            try {
                // Mencoba mengonversi tawaran harga menjadi angka
                int newOfferPrice = Integer.parseInt(offerPrice);

                // Mengirim tawaran harga melalui controller
                String result = ItemController.makeOffer(itemId, user.getUser_id(), String.valueOf(newOfferPrice));
                errorLbl.setText(result);

                // Jika tawaran berhasil, navigasi ke HomeBuyerView
                if (result.equals("Offer submitted successfully.")) {
                    new HomeBuyerView(stage, user);
                }
            } catch (NumberFormatException ex) {
                // Menangani jika tawaran harga tidak berupa angka
                errorLbl.setText("Offer price must be a valid number.");
            }
        });
    }

    /*
     * Menetapkan gaya untuk elemen UI, seperti memberi warna merah pada errorLbl.
     */
    private void setStyle() {
        errorLbl.setTextFill(Color.RED);  // Menampilkan pesan error dengan warna merah
    }

    /*
     * Konstruktor untuk menginisialisasi OfferPriceFormView.
     * Menerima Stage, User, dan Item sebagai parameter untuk mengatur tampilan.
     */
    public OfferPriceFormView(Stage stage, User user, Item item) {
        this.stage = stage;
        this.user = user;
        this.item = item;
        init();
        setLayout();
        setEvents();
        setStyle();
        
        Scene scene = new Scene(this, 500, 500);
        stage.setScene(scene);  // Menampilkan tampilan di stage
        stage.show();
    }
}

