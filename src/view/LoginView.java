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
import javafx.stage.Stage;
import model.User;

public class LoginView extends BorderPane{
	private TextField usernameTF;
	private PasswordField passwordPF;
	private Button loginBtn;
	private GridPane centerGP;
	private Label errorLbl;
	
	private Stage stage;
	
	private void init() {
		centerGP = new GridPane();
		usernameTF = new TextField();
		passwordPF = new PasswordField();
		errorLbl = new Label();
		loginBtn = new Button("Login");
		
		errorLbl.setTextFill(Color.RED);
	}
	
	private void setLayout() {
		centerGP.add(usernameTF, 0, 0);
		centerGP.add(passwordPF, 0, 1);
		centerGP.add(errorLbl, 0, 2);
		centerGP.add(loginBtn, 0, 3);
		
		this.setCenter(centerGP);
		centerGP.setAlignment(Pos.CENTER);
	}
	
	private void setEvents() {
		loginBtn.setOnAction(e -> {
			String user_name = usernameTF.getText();
			String user_password = passwordPF.getText();
			
			String message = UserController.login(user_name, user_password);
			//debug
			if(message.equals("Success") == false) {
				errorLbl.setText(message);
				System.out.println("User Not Found");
			}else {
				System.out.println("User Found");
				new HomeView(stage);
			}
		});
		
	}
	
	public LoginView(Stage stage) {
		init();
		setLayout();
		setEvents();
		
		this.stage = stage;
		
		Scene scene = new Scene(this, 400, 300);
		stage.setScene(scene);
		stage.setTitle("Login");
		stage.show();
	}
}
