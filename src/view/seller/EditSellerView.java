package view.seller;

import controller.ItemController;
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
import model.Item;
import view.LoginView;

public class EditSellerView extends BorderPane{
	private Stage stage;
	private Item item;
	private GridPane topGP, centerGP;
	
	private Label titleLbl, errorLbl;
	private TextField itemNameTF, itemCategoryTF, itemSizeTF, itemPriceTF;
	private Button submitBtn, backBtn;
	
	private void init() {
		centerGP = new GridPane();
		topGP = new GridPane();
		
		titleLbl = new Label("Edit Item");
		itemNameTF = new TextField(item.getItem_name());
		itemCategoryTF = new TextField(item.getItem_category());
		itemSizeTF = new TextField(item.getItem_size());
		itemPriceTF = new TextField(item.getItem_price());
		errorLbl = new Label();
		
		itemNameTF.setPromptText("Item Name");
		itemCategoryTF.setPromptText("Item Category");
		itemSizeTF.setPromptText("Item Size");
		itemPriceTF.setPromptText("Item Price");
		
		backBtn = new Button("Back");
		submitBtn = new Button("Submit");
	}

	private void setLayout() {

		centerGP.add(itemNameTF, 0, 0);
		centerGP.add(itemCategoryTF, 0, 1);
		centerGP.add(itemSizeTF, 0, 2);
		centerGP.add(itemPriceTF, 0, 3);
		centerGP.add(errorLbl, 0, 5);
		centerGP.add(submitBtn, 0, 7);
	
		topGP.add(backBtn, 0, 0);
		topGP.add(titleLbl, 0,1);
		this.setTop(topGP);
		topGP.setAlignment(Pos.CENTER_LEFT);
		
		this.setCenter(centerGP);
		centerGP.setVgap(10);
		centerGP.setAlignment(Pos.CENTER);
	}
	
	private void setStyle() {
		titleLbl.setFont(Font.font(null, FontWeight.BOLD ,24));
		errorLbl.setTextFill(Color.RED);
	}

	private void setEvents() {
		backBtn.setOnAction(e -> {
			new HomeSellerView(stage);
		});
		
		submitBtn.setOnAction(e -> {
			String ItemId = item.getItem_id();
			String ItemName = itemNameTF.getText();
			String ItemCategory = itemCategoryTF.getText();
			String ItemSize = itemSizeTF.getText();
			String ItemPrice = itemPriceTF.getText();
			
			String errorMessage = ItemController.checkItemValidation(ItemName, ItemName, ItemSize, ItemPrice);
			
			if (errorMessage.equals("Success")) {
				String editItem = ItemController.editItem(ItemId, ItemName, ItemCategory, ItemSize, ItemPrice);
				
				if (editItem.equals("Success")) {
					System.out.println(editItem);
					new HomeSellerView(stage);
				}else {
					errorLbl.setText(editItem);
				}
			}else {
				errorLbl.setText(errorMessage);
			}
			
		});
		
		

	}
	
	public EditSellerView(Stage stage, Item item) {
		this.stage = stage;
		this.item = item;
		
		init();
		setLayout();
		setStyle();
		setEvents();
		
		Scene scene = new Scene(this, 500, 600);
		stage.setScene(scene);
		stage.show();
		
	}
}
