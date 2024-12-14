package controller;

import javafx.collections.ObservableList;
import model.Item;
import model.Wishlist;

public class WishlistController {
	public static ObservableList<Item> viewWishlist(String user_id) {
	    ObservableList<Item> wishlistItem = Wishlist.viewWishlist(user_id);
	    
	    if (wishlistItem != null) {
	        return wishlistItem;
	    }
	    return null;
	}
	
	public static String addWishlist(String user_id, String item_id) {
		int result = Wishlist.addWishlist(user_id, item_id);
		
		if(result == 0) {
			return "Failed";
		}
		
		return "Success";
	}
	
	public static String removeWishlist(String wishlist_id) {
		int result = Wishlist.removeWishlist(wishlist_id);
		
		if (result == 0) {
			return "Failed";
		}
		
		return "Success";
	}
}
