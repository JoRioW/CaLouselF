package view;

import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import view.admin.HomeAdminView;
import view.buyer.HomeBuyerView;
import view.seller.HomeSellerView;

/*
 * Kelas LoginView ini menangani tampilan dan logika untuk halaman login pada aplikasi.
 * Pengguna dapat memasukkan username dan password mereka untuk mengakses aplikasi sesuai dengan peran mereka (Seller, Buyer, atau Admin).
 * Jika login berhasil, tampilan akan mengarahkan pengguna ke halaman yang sesuai berdasarkan peran mereka.
 */
public class LoginView extends BorderPane {
    private static final String ROLE_SELLER = "Seller";  // Peran untuk seller
    private static final String ROLE_BUYER = "Buyer";    // Peran untuk buyer
    private static final String ROLE_ADMIN = "Admin";    // Peran untuk admin

    private TextField usernameTF;  // TextField untuk username
    private PasswordField passwordPF;  // PasswordField untuk password
    private Button loginBtn;  // Tombol untuk login
    private GridPane centerGP, topGP;  // GridPane untuk tata letak
    private Label titleLbl, errorLbl, redirectLbl;  // Label untuk judul, error, dan pengalihan

    private Stage stage;  // Stage untuk menampilkan tampilan

    /*
     * Menginisialisasi komponen-komponen UI.
     * - Membuat field input username dan password.
     * - Menyiapkan tombol login dan label untuk error dan pengalihan ke halaman registrasi.
     */
    private void init() {
        centerGP = new GridPane();
        topGP = new GridPane();

        titleLbl = new Label("Login");
        redirectLbl = new Label("Don't have an account? Let's Register!!!");

        usernameTF = new TextField();
        passwordPF = new PasswordField();

        usernameTF.setPromptText("Username");  // Menetapkan placeholder untuk username
        passwordPF.setPromptText("Password");  // Menetapkan placeholder untuk password

        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);  // Menetapkan warna teks error menjadi merah

        loginBtn = new Button("Login");  // Tombol untuk melakukan login
    }

    /*
     * Mengatur tata letak tampilan.
     * - Menambahkan komponen ke dalam GridPane.
     * - Menetapkan posisi dan jarak antar elemen.
     */
    private void setLayout() {
        topGP.add(titleLbl, 0, 0);  // Menambahkan titleLbl ke dalam topGP

        centerGP.add(usernameTF, 0, 0);  // Menambahkan usernameTF ke dalam centerGP
        centerGP.add(passwordPF, 0, 1);  // Menambahkan passwordPF ke dalam centerGP
        centerGP.add(errorLbl, 0, 2);  // Menambahkan errorLbl untuk menampilkan pesan error
        centerGP.add(loginBtn, 0, 3);  // Menambahkan loginBtn untuk melakukan login
        centerGP.add(redirectLbl, 0, 4);  // Menambahkan redirectLbl untuk pengalihan ke registrasi

        this.setTop(topGP);  // Menetapkan topGP sebagai bagian atas dari tampilan
        this.setCenter(centerGP);  // Menetapkan centerGP sebagai bagian tengah dari tampilan

        topGP.setAlignment(Pos.CENTER);  // Menetapkan posisi topGP di tengah
        centerGP.setAlignment(Pos.CENTER);  // Menetapkan posisi centerGP di tengah
        centerGP.setVgap(10);  // Menetapkan jarak vertikal antar elemen di centerGP
    }

    /*
     * Menetapkan event handler untuk tombol login dan label pengalihan.
     * - Tombol login akan memproses login berdasarkan username dan password.
     * - Label pengalihan akan membawa pengguna ke halaman registrasi.
     */
    private void setEvents() {
        loginBtn.setOnAction(e -> {
            String username = usernameTF.getText().trim();  // Mendapatkan username dari field input
            String password = passwordPF.getText().trim();  // Mendapatkan password dari field input

            // Validasi jika username atau password kosong
            if (username.isEmpty() || password.isEmpty()) {
                errorLbl.setText("Username and password cannot be empty.");
                return;
            }

            // Proses login dengan validasi username dan password
            User user = User.login(username, password);  // Mengambil objek User berdasarkan login

            if (user != null) {  // Jika login berhasil, cek peran user
                String role = user.getRoles();

                if (ROLE_SELLER.equals(role)) {
                    new HomeSellerView(stage, user);  // Navigasi ke tampilan Seller
                } else if (ROLE_BUYER.equals(role)) {
                    new HomeBuyerView(stage, user);  // Navigasi ke tampilan Buyer
                } else if (ROLE_ADMIN.equals(role)) {
                    new HomeAdminView(stage, user);  // Navigasi ke tampilan Admin
                }
            } else {
                errorLbl.setText("Invalid username or password.");  // Menampilkan pesan error jika login gagal
            }
        });

        redirectLbl.setOnMouseClicked(e -> {
            new RegisterView(stage);  // Navigasi ke halaman registrasi jika klik label "Don't have an account?"
        });
    }

    /*
     * Menetapkan gaya untuk tampilan.
     * - Menetapkan font untuk titleLbl (judul halaman login).
     * - Menetapkan warna untuk redirectLbl menjadi biru.
     */
    private void setStyle() {
        titleLbl.setFont(Font.font(null, FontWeight.BOLD, 24));  // Menetapkan font untuk judul
        redirectLbl.setTextFill(Color.BLUE);  // Menetapkan warna untuk teks pengalihan ke biru
    }

    /*
     * Konstruktor untuk kelas LoginView.
     * - Menyiapkan tampilan login, menetapkan stage dan user.
     * - Menampilkan tampilan login pada stage.
     */
    public LoginView(Stage stage) {
        this.stage = stage;
        init();  // Menginisialisasi komponen UI
        setLayout();  // Menyusun tata letak
        setEvents();  // Menetapkan event handler
        setStyle();  // Menetapkan gaya

        Scene scene = new Scene(this, 500, 500);  // Membuat scene dengan ukuran 500x500
        stage.setScene(scene);  // Menetapkan scene pada stage
        stage.show();  // Menampilkan tampilan
    }
}
