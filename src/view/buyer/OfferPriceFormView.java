package view.buyer;

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
import model.Item;
import model.User;
import view.seller.HomeSellerView;

public class OfferPriceFormView extends BorderPane {

	private Stage stage;
	private TextField itemNameTF, itemCategoryTF, itemSizeTF, itemPriceTF, offerPriceTF;
	private Label errorLbl;
	private Button backBtn, submitBtn;
	
	private User user;
	private Item item;
	
	private GridPane topGP, centerGP;
	
	private void init() {
	    topGP = new GridPane();
	    centerGP = new GridPane();

	    itemNameTF = new TextField(item.getItem_name());
	    itemNameTF.setEditable(false);

	    itemCategoryTF = new TextField(item.getItem_category());
	    itemCategoryTF.setEditable(false);  // Set to non-editable

	    itemSizeTF = new TextField(item.getItem_size());
	    itemSizeTF.setEditable(false);  // Set to non-editable

	    itemPriceTF = new TextField(item.getItem_price());
	    itemPriceTF.setEditable(false);  // Set to non-editable
	    
	    errorLbl = new Label();
	    
	    offerPriceTF = new TextField(item.getOffered_price());  // TextField for buyer to input offer price

	    backBtn = new Button("Back");
	    submitBtn = new Button("Submit");
	}

	
	private void setLayout() {
		topGP.add(backBtn, 0, 0);
		
		centerGP.add(itemNameTF, 0, 0);
		centerGP.add(itemCategoryTF, 0, 1);
		centerGP.add(itemSizeTF, 0, 2);
		centerGP.add(itemPriceTF, 0, 3);
		centerGP.add(offerPriceTF, 0, 4);
		centerGP.add(errorLbl, 0, 6);
		centerGP.add(submitBtn, 0, 8);
		
		this.setCenter(centerGP);
		centerGP.setAlignment(Pos.CENTER);
		centerGP.setVgap(10);
		
		this.setTop(topGP);
		topGP.setAlignment(Pos.CENTER_LEFT);
	}
	
	public void setEvents() {
		backBtn.setOnAction(e -> {
			new HomeBuyerView(stage, user);
		});
		
		submitBtn.setOnAction(e -> {
		    String itemId = item.getItem_id();
		    String offerPrice = offerPriceTF.getText();  // Ambil harga tawaran dari field

		    // Cek apakah offerPrice kosong atau tidak valid
		    if (offerPrice.isEmpty()) {
		        errorLbl.setText("Offer price cannot be empty.");
		        return;  // Jangan lanjutkan jika kosong
		    }

		    try {
		        // Coba untuk mengonversi offerPrice menjadi angka
		        int newOfferPrice = Integer.parseInt(offerPrice);

		        // Panggil controller untuk memproses tawaran harga
		        String result = ItemController.makeOffer(itemId, user.getUser_id(), String.valueOf(newOfferPrice));
		        errorLbl.setText(result);

		        // Jika tawaran berhasil, refresh halaman
		        if (result.equals("Offer submitted successfully.")) {
		            new HomeBuyerView(stage, user);  // Navigasi ke halaman buyer setelah tawaran berhasil
		        }
		    } catch (NumberFormatException ex) {
		        // Tangani kasus jika offerPrice bukan angka
		        errorLbl.setText("Offer price must be a valid number.");
		    }
		});


	}
	
	private void setStyle() {
		errorLbl.setTextFill(Color.RED);
	}
	
	public OfferPriceFormView(Stage stage, User user, Item item) {
		this.stage = stage;
		this.user = user;
		this.item = item;
		init();
		setLayout();
		setEvents();
		setStyle();
		
		Scene scene = new Scene(this, 500, 500);
		stage.setScene(scene);
		stage.show();
	}

}
