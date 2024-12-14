package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import database.Database;

public class Transaction {
	private String transaction_id;
	private String user_id;
	private String item_id;
	private static Database db = Database.getInstance();
	
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
