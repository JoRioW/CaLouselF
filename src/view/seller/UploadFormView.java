package view.seller;

import controller.ItemController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class UploadFormView extends BorderPane{
	private TextField itemNameTF, itemCategoryTF, itemSizeTF, itemPriceTF;
	private Label errorLbl, titleLbl;
	private Button submitBtn, backBtn;
	
	
	private GridPane centerGP, topGP;
	private Stage stage;
	private void init() {
		topGP = new GridPane();
		centerGP = new GridPane();
		
		backBtn = new Button("Back");
		titleLbl = new Label("Upload Items");
		
		itemNameTF = new TextField();
		itemCategoryTF = new TextField();
		itemSizeTF = new TextField();
		itemPriceTF = new TextField();
		itemNameTF.setPromptText("Item Name");
		itemCategoryTF.setPromptText("Item Category");
		itemSizeTF.setPromptText("Item Size");
		itemPriceTF.setPromptText("Item Price");
		
		errorLbl = new Label();
		
		submitBtn = new Button("Submit");
	}

	private void setLayout() {
		topGP.add(backBtn, 0, 0);
		topGP.add(titleLbl, 1, 0);
		
		
		centerGP.add(itemNameTF, 0, 0);
		centerGP.add(itemCategoryTF, 0, 1);
		centerGP.add(itemSizeTF, 0, 2);
		centerGP.add(itemPriceTF, 0, 3);
		centerGP.add(errorLbl, 0, 5);
		centerGP.add(submitBtn, 0, 7);
		
		this.setCenter(centerGP);
		centerGP.setAlignment(Pos.CENTER);
		centerGP.setVgap(10);
		
		this.setTop(topGP);
		topGP.setAlignment(Pos.CENTER_LEFT);
		
	}

	private void setEvents() {
		backBtn.setOnAction(e -> {
			new HomeSellerView(stage);
		});
		
		submitBtn.setOnAction(e -> {
			String ItemName = itemNameTF.getText();
			String ItemCategory = itemCategoryTF.getText();
			String ItemSize = itemSizeTF.getText();
			String ItemPrice = itemPriceTF.getText();
			
			String validationMessage = ItemController.checkItemValidation(ItemName, ItemCategory, ItemSize, ItemPrice);
			
			if (validationMessage.equals("Success")) {
				System.out.println(validationMessage);
				
				String uploadItemMessage = ItemController.uploadItem(ItemName, ItemCategory, ItemSize, ItemPrice);
				
				if (uploadItemMessage.equals("Success")) {
					System.out.println(uploadItemMessage);
					new HomeSellerView(stage);
				}else {
					errorLbl.setText(uploadItemMessage);
				}
			}else {
				errorLbl.setText(validationMessage);
			}
		});
	}

	private void setStyle() {
		titleLbl.setFont(Font.font(null, FontWeight.BOLD ,24));
		errorLbl.setTextFill(Color.RED);
	}

	public UploadFormView(Stage stage) {
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
