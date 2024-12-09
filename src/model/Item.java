package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.Database;

public class Item {
	private String item_id;
	private String item_name;
	private String item_size;
	private String item_price;
	private String item_category;
	private String item_status;
	private String item_wishlist;
	private String item_offer_status;
	private static Database db = Database.getInstance();
	private static int result = 0;
	
	public Item() {};
	
	public Item(String item_id, String item_name, String item_size, String item_price, String item_category,
			String item_status, String item_wishlist, String item_offer_status) {
		super();
		this.item_id = item_id;
		this.item_name = item_name;
		this.item_size = item_size;
		this.item_price = item_price;
		this.item_category = item_category;
		this.item_status = item_status;
		this.item_wishlist = item_wishlist;
		this.item_offer_status = item_offer_status;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_size() {
		return item_size;
	}

	public void setItem_size(String item_size) {
		this.item_size = item_size;
	}

	public String getItem_price() {
		return item_price;
	}

	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}

	public String getItem_category() {
		return item_category;
	}

	public void setItem_category(String item_category) {
		this.item_category = item_category;
	}

	public String getItem_status() {
		return item_status;
	}

	public void setItem_status(String item_status) {
		this.item_status = item_status;
	}

	public String getItem_wishlist() {
		return item_wishlist;
	}

	public void setItem_wishlist(String item_wishlist) {
		this.item_wishlist = item_wishlist;
	}

	public String getItem_offer_status() {
		return item_offer_status;
	}

	public void setItem_offer_status(String item_offer_status) {
		this.item_offer_status = item_offer_status;
	}
	
	private static String generateItemId() {
		String query = "SELECT item_id FROM items WHERE item_id = ?";
		PreparedStatement ps = db.preparedStatement(query);
		String randId = null;

		try {
			randId = "IT" + UUID.randomUUID().toString().replace("-", "").substring(0,6);
			System.out.println(randId);
			
			ps.setString(1, randId);
			
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return randId;
			}
			
			String checkId = rs.getString("item_id");
			
			if (checkId.equals(randId)) {
				return generateItemId();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return randId;
	}
	
	public static int uploadItem(String item_name,  String item_category, String item_size, String item_price) {
		String id = generateItemId();
		String status = "Pending";
		String query = "INSERT INTO items(item_id, item_name, item_category, item_size, item_price, item_status) VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = db.preparedStatement(query);
		
		try {
			ps.setString(1, id);
			ps.setString(2, item_name);
			ps.setString(3, item_category);
			ps.setString(4, item_size);
			ps.setString(5, item_price);
			ps.setString(6, status);
			
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<Item> viewItem() {
		List<Item> items = new ArrayList<>();
		String query = "SELECT * FROM items WHERE item_status = ?";
		PreparedStatement ps = db.preparedStatement(query);
		
		try {
			ps.setString(1, "Approve");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				String itemId = rs.getString("item_id");
				String itemName = rs.getString("item_name");
				String itemCategory = rs.getString("item_category");
				String itemSize = rs.getString("item_size");
				String itemPrice = rs.getString("item_price");
				item.setItem_id(itemId);
				item.setItem_name(itemName);
				item.setItem_category(itemCategory);
				item.setItem_size(itemSize);
				item.setItem_price(itemPrice);
				items.add(item);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public static int editItem(String item_id, String item_name, String item_category, String item_size, String item_price) {
		String query = "UPDATE items SET item_name = ?, item_category = ?, item_size = ?, item_price = ? WHERE item_id = ?";
		PreparedStatement ps = db.preparedStatement(query);
		
		try {
			ps.setString(1, item_name);
			ps.setString(2, item_category);
			ps.setString(3, item_size);
			ps.setString(4, item_price);
			ps.setString(5, item_id);
			
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int deleteItem(String item_id) {
		String query = "DELETE FROM items WHERE item_id = ?";
		PreparedStatement ps = db.preparedStatement(query);
		
		try {
			ps.setString(1, item_id);
			result = ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}
