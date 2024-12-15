package controller;

import javafx.collections.ObservableList;
import model.Item;
import model.Transaction;

public class TransactionController {
	public static String purchaseItem(String user_id, String item_id) {
		int result = Transaction.purchaseItem(user_id, item_id);
		
		if(result == 0) {
			return "Failed";
		}
		
		return "Success";
	}
	
	public static ObservableList<Transaction> viewPurchaseHistory(String userId) {
	    ObservableList<Transaction> transactions = Transaction.viewPurchaseHistory(userId); 
	    
	    if (transactions != null) {
	        return transactions;
	    }
	    return null;
	}
}
