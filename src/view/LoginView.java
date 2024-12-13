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

public class LoginView extends BorderPane {
    private static final String ROLE_SELLER = "Seller";
    private static final String ROLE_BUYER = "Buyer";
    private static final String ROLE_ADMIN = "Admin";

    private TextField usernameTF;
    private PasswordField passwordPF;
    private Button loginBtn;
    private GridPane centerGP, topGP;
    private Label titleLbl, errorLbl, redirectLbl;

    private Stage stage;

    private void init() {
        centerGP = new GridPane();
        topGP = new GridPane();

        titleLbl = new Label("Login");
        redirectLbl = new Label("Don't have an account? Let's Register!!!");

        usernameTF = new TextField();
        passwordPF = new PasswordField();

        usernameTF.setPromptText("Username");
        passwordPF.setPromptText("Password");

        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);

        loginBtn = new Button("Login");
    }

    private void setLayout() {
        topGP.add(titleLbl, 0, 0);

        centerGP.add(usernameTF, 0, 0);
        centerGP.add(passwordPF, 0, 1);
        centerGP.add(errorLbl, 0, 2);
        centerGP.add(loginBtn, 0, 3);
        centerGP.add(redirectLbl, 0, 4);

        this.setTop(topGP);
        this.setCenter(centerGP);

        topGP.setAlignment(Pos.CENTER);
        centerGP.setAlignment(Pos.CENTER);
        centerGP.setVgap(10);
    }

    private void setEvents() {
        loginBtn.setOnAction(e -> {
            String username = usernameTF.getText().trim();
            String password = passwordPF.getText().trim();

            // Validasi input kosong
            if (username.isEmpty() || password.isEmpty()) {
                errorLbl.setText("Username and password cannot be empty.");
                return;
            }

            // Proses login dan mendapatkan objek User
            User user = User.login(username, password);

            if (user != null) {
                // Setelah login berhasil, periksa role user
                String role = user.getRoles();

                if (ROLE_SELLER.equals(role)) {
                    new HomeSellerView(stage, user);
                } else if (ROLE_BUYER.equals(role)) {
                    new HomeBuyerView(stage, user);
                } else if (ROLE_ADMIN.equals(role)) {
//                    System.out.println("Admin View under development.");
                    new HomeAdminView(stage, user);
                }
            } else {
                errorLbl.setText("Invalid username or password.");
            }
        });

        redirectLbl.setOnMouseClicked(e -> {
            new RegisterView(stage);
        });
    }

    private void setStyle() {
        titleLbl.setFont(Font.font(null, FontWeight.BOLD, 24));
        redirectLbl.setTextFill(Color.BLUE);
    }

    public LoginView(Stage stage) {
        this.stage = stage;
        init();
        setLayout();
        setEvents();
        setStyle();

        Scene scene = new Scene(this, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
}


