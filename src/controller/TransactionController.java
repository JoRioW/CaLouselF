package controller;

import model.Transaction;

public class TransactionController {
	public static String purchaseItem(String user_id, String item_id) {
		int result = Transaction.purchaseItem(user_id, item_id);
		
		if(result == 0) {
			return "Failed";
		}
		
		return "Success";
	}
}
