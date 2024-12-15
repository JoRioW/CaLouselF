package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Transaction {
	private String transaction_id;
	private String user_id;
	private String item_id;
	private String item_name;
    private String item_category;
    private String item_size;
    private String item_price;
	private static Database db = Database.getInstance();
	
	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_category() {
		return item_category;
	}

	public void setItem_category(String item_category) {
		this.item_category = item_category;
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

	public Transaction() {}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
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

	public Transaction(String transaction_id, String user_id, String item_id) {
		super();
		this.transaction_id = transaction_id;
		this.user_id = user_id;
		this.item_id = item_id;
	};
	
	private static String generateTransactionId() {
		String query = "SELECT transaction_id FROM transaction WHERE transaction_id = ?";
		PreparedStatement ps = db.preparedStatement(query);
		String randId = null;

		try {
			randId = "TS" + UUID.randomUUID().toString().replace("-", "").substring(0,6);
			
			ps.setString(1, randId);
			
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return randId;
			}
			
			String checkId = rs.getString("transaction_id");
			
			if (checkId.equals(randId)) {
				return generateTransactionId();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return randId;
	}
	
	public static ObservableList<Transaction> viewPurchaseHistory(String user_id) {
	    ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	    String query = "SELECT t.transaction_id, items.item_name, items.item_category, items.item_size, items.item_price " +
	                   "FROM transaction t " +
	                   "JOIN items ON t.item_id = items.item_id " +
	                   "WHERE t.user_id = ?";
	    PreparedStatement ps = db.preparedStatement(query);
	    try {
	        ps.setString(1, user_id);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	        	Transaction transaction = new Transaction();
	            transaction.setTransaction_id(rs.getString("transaction_id"));
	            transaction.setItem_name(rs.getString("item_name"));
	            transaction.setItem_category(rs.getString("item_category"));
	            transaction.setItem_size(rs.getString("item_size"));
	            transaction.setItem_price(rs.getString("item_price"));
	            transactions.add(transaction);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return transactions;
	}

	
	public static int purchaseItem(String user_id, String item_id) {
		String id = generateTransactionId();
		String query = "INSERT INTO transaction(transaction_id, user_id, item_id) VALUES(?, ?, ?)";
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
	
}
