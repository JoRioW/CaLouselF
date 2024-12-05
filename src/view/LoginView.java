package view;

import controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.User;

public class LoginView extends BorderPane{
//	TextField usernameTF;
//	PasswordField passwordPF;
//	Button loginBtn;
//	GridPane gp;
//	
//	private void init() {
//		gp = new GridPane();
//		usernameTF = new TextField();
//		passwordPF = new PasswordField();
//		loginBtn = new Button("Login");
//	}
//	
//	private void setLayout() {
//		gp.add(usernameTF, 0, 0);
//		gp.add(passwordPF, 0, 1);
//		gp.add(loginBtn, 0, 2);
//		
//		this.setCenter(gp);
//	}
//	
//	private void setEvents() {
//		loginBtn.setOnAction(e -> {
//			String user_name = usernameTF.getText();
//			String user_password = passwordPF.getText();
//			
//			User user = UserController.login(user_name, user_password);
//			if(user == null) {
//				System.out.println("User Not Found");
//			}else {
//				System.out.println("User Found");
//			}
//		});
//		
//	}
//	
//	public LoginView(Stage stage) {
//		init();
//		setLayout();
//		setEvents();
//		
//		Scene scene = new Scene(this, 400, 300);
//		stage.setScene(scene);
//		stage.setTitle("Login");
//		stage.show();
//	}
}
