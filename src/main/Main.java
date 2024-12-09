package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;
import view.RegisterView;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("CaLouseIF");
		new LoginView(stage);
	}

}
