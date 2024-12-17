package controller;

import javafx.collections.ObservableList;
import model.Item;
import model.Transaction;

/*
 * Kelas TransactionController berfungsi untuk mengelola transaksi dalam aplikasi.
 * Kelas ini berisi metode untuk memproses pembelian item oleh pengguna dan menampilkan riwayat transaksi.
 */
public class TransactionController {

    /*
     * Memproses pembelian item oleh pembeli.
     * Fungsi ini akan memanggil metode purchaseItem dari kelas Transaction untuk memasukkan data transaksi ke dalam database.
     * 
     * Parameter:
     * - user_id: ID pengguna yang melakukan pembelian.
     * - item_id: ID item yang dibeli.
     * 
     * Return:
     * - String: Pesan hasil ("Success" atau "Failed").
     */
    public static String purchaseItem(String user_id, String item_id) {
        int result = Transaction.purchaseItem(user_id, item_id);
        
        if(result == 0) {
            return "Failed";  // Jika terjadi kesalahan saat pemrosesan transaksi.
        }
        
        return "Success";  // Jika transaksi berhasil diproses.
    }
    
    /*
     * Menampilkan riwayat pembelian berdasarkan user_id.
     * Fungsi ini memanggil metode viewPurchaseHistory dari kelas Transaction untuk mendapatkan data transaksi yang relevan.
     * 
     * Parameter:
     * - userId: ID pengguna yang riwayat transaksinya ingin ditampilkan.
     * 
     * Return:
     * - ObservableList<Transaction>: Daftar transaksi yang dilakukan oleh pengguna, atau null jika tidak ada transaksi.
     */
    public static ObservableList<Transaction> viewPurchaseHistory(String userId) {
        ObservableList<Transaction> transactions = Transaction.viewPurchaseHistory(userId); 
        
        if (transactions != null) {
            return transactions;  // Mengembalikan daftar transaksi jika ada.
        }
        return null;  // Mengembalikan null jika tidak ada transaksi.
    }
}

