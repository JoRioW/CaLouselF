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

public class RegisterView extends BorderPane {

	private TextField usernameTF, phoneTF, addressTF;
	private PasswordField passwordPF;
	private Button registerBtn;
	private RadioButton buyerBtn, sellerBtn;
	private ToggleGroup roleGroup;
	private Label roleLbl, errorLbl, redirectLbl, titleLbl;
	
	private Stage stage;
	private GridPane centerGP, topGP;

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
		roleGroup = new ToggleGroup();
		errorLbl = new Label();
		
		usernameTF.setPromptText("Username");
		passwordPF.setPromptText("Password");
		phoneTF.setPromptText("Phone Number");
		addressTF.setPromptText("Address");
		
		buyerBtn.setToggleGroup(roleGroup);
		sellerBtn.setToggleGroup(roleGroup);
		
		registerBtn = new Button("Register");
		redirectLbl = new Label("Already have an account? Sign in");
	}

	private void setLayout() {

		centerGP.add(usernameTF, 0, 0);
		centerGP.add(passwordPF, 0, 1);
		centerGP.add(phoneTF, 0, 2);
		centerGP.add(addressTF, 0, 3);
		centerGP.add(roleLbl, 0, 4);
		centerGP.add(buyerBtn, 0, 5);
		centerGP.add(sellerBtn, 1, 5);
		centerGP.add(errorLbl, 0, 6);
		centerGP.add(registerBtn, 0, 10);
		centerGP.add(redirectLbl, 0, 11);
	
		topGP.add(titleLbl, 0, 0);
		this.setTop(topGP);
		topGP.setAlignment(Pos.CENTER);
		
		this.setCenter(centerGP);
		centerGP.setVgap(10);
		centerGP.setAlignment(Pos.CENTER);
	}
	
	private void setStyle() {
		titleLbl.setFont(Font.font(null, FontWeight.BOLD ,24));
		errorLbl.setTextFill(Color.RED);
	}

	private void setEvents() {
		registerBtn.setOnAction(e -> {
			String Username = usernameTF.getText();
			String Password = passwordPF.getText();
			String PhoneNumber = phoneTF.getText();
			String Address = addressTF.getText();
			
			String errorMessage = UserController.checkAccountValidation(Username, Password, PhoneNumber, Address, roleGroup);
			
			if (errorMessage.equals("Success")) {
				RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
				String Roles = selectedRole.getText();
				
				String Register = UserController.register(Username, Password, PhoneNumber, Address, Roles);
				
				if (Register.equals("Success")) {
					System.out.println(Register);
					new LoginView(stage);
				}else {
					errorLbl.setText(Register);
					System.out.println(Register);
				}
			}else {
				errorLbl.setText(errorMessage);
				System.out.println(errorMessage);
			}
		});
		
		redirectLbl.setOnMouseClicked(e -> {
			new LoginView(stage);
		});

	}

	public RegisterView(Stage stage) {
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
