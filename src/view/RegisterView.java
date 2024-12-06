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
import javafx.stage.Stage;

public class RegisterView extends BorderPane {

	private TextField usernameTF, phoneTF, addressTF;
	private PasswordField passwordPF;
	private Button registerBtn;
	private RadioButton buyerBtn, sellerBtn;
	private ToggleGroup roleGroup;
	private Label roleLbl, errorLbl, redirectLbl;
	
	private Stage stage;
	private GridPane gp;

	private void init() {
		gp = new GridPane();
		
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
		
		errorLbl.setTextFill(Color.RED);
		
		buyerBtn.setToggleGroup(roleGroup);
		sellerBtn.setToggleGroup(roleGroup);
		
		registerBtn = new Button("Register");
		redirectLbl = new Label("Already have an account? Sign in");
	}

	private void setLayout() {

		gp.add(usernameTF, 0, 0);
		gp.add(passwordPF, 0, 1);
		gp.add(phoneTF, 0, 2);
		gp.add(addressTF, 0, 3);
		gp.add(roleLbl, 0, 4);
		gp.add(buyerBtn, 0, 5);
		gp.add(sellerBtn, 1, 5);
		gp.add(errorLbl, 0, 6);
		gp.add(registerBtn, 0, 10);
		gp.add(redirectLbl, 0, 11);
		
		this.setCenter(gp);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
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
		
		this.stage = stage;
		Scene scene = new Scene(this, 500, 500);
		stage.setScene(scene);
		stage.show();
	}
}
