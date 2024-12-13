package view.buyer;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Item;

public class WishlistView extends BorderPane {
	
	private Stage stage;
	private Button backBtn, removeBtn;
	private TableView<Item> table;
	private TableColumn<Item, String> name, category, size, price;
	
	private void init() {
		backBtn = new Button("Back");
		removeBtn = new Button("Remove");
		table = new TableView<>();
	}
	
	public WishlistView() {
		
	}

}
