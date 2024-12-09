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
import view.seller.HomeSellerView;

public class LoginView extends BorderPane{
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
		loginBtn = new Button("Login");
		
		errorLbl.setTextFill(Color.RED);
	}
	
	private void setLayout() {
		topGP.add(titleLbl, 0, 0);
		
		centerGP.add(usernameTF, 0, 0);
		centerGP.add(passwordPF, 0, 1);
		centerGP.add(errorLbl, 0, 2);
		centerGP.add(loginBtn, 0, 3);
		centerGP.add(redirectLbl, 0, 11);
		
		
		this.setTop(topGP);
		this.setCenter(centerGP);
		topGP.setAlignment(Pos.CENTER);
		centerGP.setAlignment(Pos.CENTER);
		centerGP.setVgap(10);
	}
	
	private void setEvents() {
		loginBtn.setOnAction(e -> {
			String user_name = usernameTF.getText();
			String user_password = passwordPF.getText();
			
			String message = UserController.login(user_name, user_password);
			
			if(message.equals("Seller")) {
				System.out.println(message);
				new HomeSellerView(stage);
			}else if (message.equals("Buyer")) {
				System.out.println(message);
//				new HomeBuyerView(stage);
			}else if (message.equals("Admin")) {
				System.out.println(message);
//				new HomeAdminView(stage);
			}else {
				errorLbl.setText(message);
				System.out.println("User Not Found");
			}
		});
		
		redirectLbl.setOnMouseClicked(e -> {
			new RegisterView(stage);
		});
		
	}
	
	private void setStyle() {
		titleLbl.setFont(Font.font(null, FontWeight.BOLD, 24));
	}
	
	public LoginView(Stage stage) {
		init();
		setLayout();
		setEvents();
		setStyle();
		
		this.stage = stage;
		
		Scene scene = new Scene(this, 500, 500);
		stage.setScene(scene);
		stage.show();
	}
}
