package controller;

import javafx.collections.ObservableList;
import model.Item;

public class ItemController {
	private static String message = "";
	private static String checkItemName(String item_name) {
		if (item_name.isEmpty()) {
			return message = "Item name cannot be empty";
		}else if (item_name.length() < 3) {
			return message = "Item name must be at least 3 character long";
		}
		return message = "";
	}
	
	private static String checkItemCategory(String item_category) {
		if (item_category.isEmpty()) {
			return message = "Item category cannot be empty";
		}else if (item_category.length() < 3) {
			return message = "Item category must be at least 3 character long";
		}
		return message = "";
	}
	
	private static String checkItemSize(String item_size) {
		if (item_size.isEmpty()) {
			return message = "Item size cannot be empty";
		}
		return message = "";
	}
	
	private static String checkItemPrice(String item_price) {
		if (item_price.isEmpty()) {
			return message = "Item price cannot be empty";
		}else if (item_price.equals("0")) {
			return message = "Item price cannot be 0";
		}else {
			for (int i = 0; i < item_price.length(); i++) {
				char currPrice = item_price.charAt(i);
				if (!Character.isDigit(currPrice)) {
					return message = "Item price must in number";
				}
			}
		}
		return message = "";
	}
	
	public static String checkItemValidation(String item_name, String item_category, String item_size, String item_price) {
		if (!checkItemName(item_name).isEmpty()) {
			return message;
		}else if (!checkItemCategory(item_category).isEmpty()) {
			return message;
		}else if (!checkItemSize(item_size).isEmpty()) {
			return message;
		}else if (!checkItemPrice(item_price).isEmpty()) {
			return message;
		}
		
		return "Success";
	}
	
	
//	public static String uploadItem(String item_name, String item_category, String item_size, String item_price) {
//		int result = Item.uploadItem(item_name, item_category, item_size, item_price);
//		if (result == 0) {
//			return "Failed";
//		}
//		
//		return "Success";
//	}
	
	public static String deleteItem(String item_id) {
		int result = Item.deleteItem(item_id);
		if (result == 0) {
			return "Failed";
		}
		return "Success";
	}
	
	public static String approveItem(String item_id) {
	    int result = Item.approveItem(item_id);
	    if (result == 0) {
	        return "Failed to approve item";
	    }
	    return "Item approved successfully";
	}

	
	public static ObservableList<Item> viewItem(String currentUserId, String currentUserRole) {
	    ObservableList<Item> items = Item.viewItem(currentUserId, currentUserRole); 
	    
	    if (items != null) {
	        return items;
	    }
	    return null;
	}

	public static String uploadItem(String sellerId, String item_name, String item_category, String item_size, String item_price) {
	    // Validasi item sebelum upload
	    String validationResult = checkItemValidation(item_name, item_category, item_size, item_price);
	    int result = Item.uploadItem(sellerId, item_name, item_category, item_size, item_price);
	    
	    if (result == 0) {
	        return "Failed";  // Gagal validasi
	    }
	    
	    return "Success";
	}
	
//	public static ObservableList<Item> viewItem() {
//		ObservableList<Item> items = Item.viewItem(); 
//		
//		if (items != null) {
//			return items;
//		}
//		return null;
//	}
	
	
	
	public static String editItem(String item_id, String item_name, String item_category, String item_size, String item_price) {
		int result = Item.editItem(item_id, item_name, item_category, item_size, item_price);
		
		if (result == 0) {
			return "Failed";
		}
		
		return "Success";
	}
	
	public static String updatePurchase(String item_id) {
		int result = Item.updatePurchase(item_id);
		
		if (result == 0) {
			return "Failed";
		}
		
		return "Success";
	}
}
