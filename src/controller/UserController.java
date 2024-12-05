package controller;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import model.User;

public class UserController {
	public static String register(
		String username,
		String password,
		String phone,
		String address,
		ToggleGroup role
		) {
		boolean isExist = User.validateUsername(username);
		RadioButton selectedRole = (RadioButton) role.getSelectedToggle();
			if(username.isEmpty() && password.isEmpty() && phone.isEmpty() && address.isEmpty() && selectedRole == null) {
				return "All fields must be filled";
			}else if (isExist == true) {
				return "Username already exist";
			}
			
			String roles = selectedRole.getText();
			
			int result = User.register(username, password, phone, address, roles);
			if (result == 0) {
				return "Register Failed";
			}
			return "Register Success";
		}
}
