package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Wishlist {
	private String wishlist_id;
	private String user_id;
	private String item_id;
	private static Database db = Database.getInstance();
	
	public Wishlist() {};
	
	public Wishlist(String wishlist_id, String user_id, String item_id) {
		super();
		this.wishlist_id = wishlist_id;
		this.user_id = user_id;
		this.item_id = item_id;
	}
	public String getWishlist_id() {
		return wishlist_id;
	}
	public void setWishlist_id(String wishlist_id) {
		this.wishlist_id = wishlist_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	
	public static ObservableList<Item> viewWishlist(String user_id) {
		ObservableList<Item> items = FXCollections.observableArrayList();
		String query = "SELECT items.item_id, items.item_name, items.item_category, items.item_size, items.item_price FROM wishlist "
				+ "JOIN items "
				+ "ON wishlist.item_id = items.item_id "
				+ "WHERE wishlist.user_id = ? AND items.item_status = 'Approve'";
		PreparedStatement ps = db.preparedStatement(query);
		
		try {
			ps.setString(1, user_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				item.setItem_id(rs.getString("item_id"));
	            item.setItem_name(rs.getString("item_name"));
	            item.setItem_category(rs.getString("item_category"));
	            item.setItem_size(rs.getString("item_size"));
	            item.setItem_price(rs.getString("item_price"));
	            items.add(item);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return items;
	}
		
		private static String generateWishlistId() {
			String query = "SELECT wishlist_id FROM wishlist WHERE wishlist_id = ?";
			PreparedStatement ps = db.preparedStatement(query);
			String randId = null;

			try {
				randId = "WL" + UUID.randomUUID().toString().replace("-", "").substring(0,6);
				
				ps.setString(1, randId);
				
				ResultSet rs = ps.executeQuery();

				if (!rs.next()) {
					return randId;
				}
				
				String checkId = rs.getString("wishlist_id");
				
				if (checkId.equals(randId)) {
					return generateWishlistId();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return randId;
		}
		
		public static int addWishlist(String user_id, String item_id) {
			String id = generateWishlistId();
			String query = "INSERT INTO wishlist(wishlist_id, user_id, item_id) VALUES(?, ?, ?)";
			PreparedStatement ps = db.preparedStatement(query);
			int result = 0;
			
			try {
		        ps.setString(1, id);
		        ps.setString(2, user_id);
		        ps.setString(3, item_id);
		        
		        result = ps.executeUpdate();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return result;
		}
		
		public static int removeWishlist(String user_id, String item_id) {
			String query = "DELETE FROM wishlist WHERE user_id = ? AND item_id = ?";
			PreparedStatement ps = db.preparedStatement(query);
			int result = 0;
			
			try {
				ps.setString(1, user_id);
				ps.setString(2, item_id);
				result = ps.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}
	
	
}
