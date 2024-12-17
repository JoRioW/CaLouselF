package view;

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

/*
 * Kelas RegisterView ini menangani tampilan dan logika untuk halaman pendaftaran pengguna baru.
 * Pengguna dapat memasukkan data mereka untuk mendaftar sebagai "Buyer" atau "Seller".
 * Jika pendaftaran berhasil, pengguna akan diarahkan ke halaman login.
 */
public class RegisterView extends BorderPane {

    private TextField usernameTF, phoneTF, addressTF;  // Field input untuk username, nomor telepon, dan alamat
    private PasswordField passwordPF;  // Field input untuk password
    private Button registerBtn;  // Tombol untuk mengirimkan data pendaftaran
    private RadioButton buyerBtn, sellerBtn;  // RadioButton untuk memilih peran sebagai Buyer atau Seller
    private ToggleGroup roleGroup;  // Group untuk menampung pilihan peran
    private Label roleLbl, errorLbl, redirectLbl, titleLbl;  // Label untuk judul, peran, error, dan pengalihan ke login

    private Stage stage;  // Stage untuk menampilkan tampilan
    private GridPane centerGP, topGP;  // GridPane untuk tata letak

    /*
     * Menginisialisasi komponen-komponen UI.
     * - Membuat field input untuk username, password, nomor telepon, dan alamat.
     * - Menyiapkan radio buttons untuk memilih peran.
     * - Menyiapkan tombol dan label untuk error dan pengalihan.
     */
    private void init() {
        centerGP = new GridPane();
        topGP = new GridPane();

        titleLbl = new Label("Register");
        usernameTF = new TextField();
        passwordPF = new PasswordField();
        phoneTF = new TextField();
        addressTF = new TextField();
        roleLbl = new Label("Roles");
        buyerBtn = new RadioButton("Buyer");
        sellerBtn = new RadioButton("Seller");
        roleGroup = new ToggleGroup();  // Mengelompokkan kedua radio button dalam satu grup
        errorLbl = new Label();

        usernameTF.setPromptText("Username");  // Menetapkan placeholder untuk username
        passwordPF.setPromptText("Password");  // Menetapkan placeholder untuk password
        phoneTF.setPromptText("Phone Number");  // Menetapkan placeholder untuk nomor telepon
        addressTF.setPromptText("Address");  // Menetapkan placeholder untuk alamat

        buyerBtn.setToggleGroup(roleGroup);  // Menetapkan grup untuk buyerBtn
        sellerBtn.setToggleGroup(roleGroup);  // Menetapkan grup untuk sellerBtn

        registerBtn = new Button("Register");  // Tombol untuk mendaftar
        redirectLbl = new Label("Already have an account? Sign in");  // Label untuk pengalihan ke halaman login
    }

    /*
     * Mengatur tata letak tampilan.
     * - Menambahkan komponen-komponen UI ke dalam GridPane.
     * - Menyusun elemen-elemen pada posisi yang sesuai.
     */
    private void setLayout() {

        centerGP.add(usernameTF, 0, 0);  // Menambahkan input username
        centerGP.add(passwordPF, 0, 1);  // Menambahkan input password
        centerGP.add(phoneTF, 0, 2);  // Menambahkan input phone number
        centerGP.add(addressTF, 0, 3);  // Menambahkan input address
        centerGP.add(roleLbl, 0, 4);  // Menambahkan label untuk roles
        centerGP.add(buyerBtn, 0, 5);  // Menambahkan radio button Buyer
        centerGP.add(sellerBtn, 1, 5);  // Menambahkan radio button Seller
        centerGP.add(errorLbl, 0, 6);  // Menambahkan label untuk error
        centerGP.add(registerBtn, 0, 10);  // Menambahkan tombol untuk register
        centerGP.add(redirectLbl, 0, 11);  // Menambahkan label untuk redirect ke login

        topGP.add(titleLbl, 0, 0);  // Menambahkan judul di bagian atas
        this.setTop(topGP);
        topGP.setAlignment(Pos.CENTER);  // Menetapkan posisi judul ke tengah

        this.setCenter(centerGP);  // Menetapkan grid utama di tengah
        centerGP.setVgap(10);  // Menetapkan jarak vertikal antar elemen di centerGP
        centerGP.setAlignment(Pos.CENTER);  // Menetapkan posisi elemen di tengah
    }

    /*
     * Menetapkan gaya untuk tampilan.
     * - Menetapkan font untuk titleLbl (judul halaman).
     * - Menetapkan warna untuk errorLbl menjadi merah.
     */
    private void setStyle() {
        titleLbl.setFont(Font.font(null, FontWeight.BOLD, 24));  // Menetapkan font untuk judul
        errorLbl.setTextFill(Color.RED);  // Menetapkan warna teks error menjadi merah
    }

    /*
     * Menetapkan event handler untuk tombol register dan label pengalihan ke halaman login.
     * - Tombol register akan memproses pendaftaran pengguna baru.
     * - Label pengalihan akan membawa pengguna ke halaman login jika sudah memiliki akun.
     */
    private void setEvents() {
        registerBtn.setOnAction(e -> {
            String Username = usernameTF.getText();  // Mengambil username dari field input
            String Password = passwordPF.getText();  // Mengambil password dari field input
            String PhoneNumber = phoneTF.getText();  // Mengambil nomor telepon dari field input
            String Address = addressTF.getText();  // Mengambil alamat dari field input

            // Validasi akun menggunakan UserController
            String errorMessage = UserController.checkAccountValidation(Username, Password, PhoneNumber, Address, roleGroup);

            if (errorMessage.equals("Success")) {
                RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();  // Mendapatkan role yang dipilih
                String Roles = selectedRole.getText();

                // Memanggil metode register dari UserController
                String Register = UserController.register(Username, Password, PhoneNumber, Address, Roles);

                if (Register.equals("Success")) {
                    System.out.println(Register);
                    new LoginView(stage);  // Arahkan pengguna ke halaman login setelah sukses
                } else {
                    errorLbl.setText(Register);  // Menampilkan pesan error jika pendaftaran gagal
                    System.out.println(Register);
                }
            } else {
                errorLbl.setText(errorMessage);  // Menampilkan pesan error jika validasi gagal
                System.out.println(errorMessage);
            }
        });

        redirectLbl.setOnMouseClicked(e -> {
            new LoginView(stage);  // Arahkan pengguna ke halaman login jika sudah punya akun
        });
    }

    /*
     * Konstruktor untuk kelas RegisterView.
     * - Menyiapkan tampilan pendaftaran, menetapkan stage dan user.
     * - Menampilkan tampilan register pada stage.
     */
    public RegisterView(Stage stage) {
        init();  // Menginisialisasi komponen UI
        setLayout();  // Menyusun tata letak
        setEvents();  // Menetapkan event handler
        setStyle();  // Menetapkan gaya

        this.stage = stage;
        Scene scene = new Scene(this, 500, 500);  // Membuat scene dengan ukuran 500x500
        stage.setScene(scene);  // Menetapkan scene pada stage
        stage.show();  // Menampilkan tampilan
    }
}

