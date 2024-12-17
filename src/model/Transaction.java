package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * Kelas Transaction digunakan untuk merepresentasikan data transaksi dalam sistem.
 * Setiap transaksi mencatat informasi seperti ID transaksi, ID pengguna, ID item,
 * serta detail terkait item seperti nama, kategori, ukuran, dan harga.
 */
public class Transaction {

    private String transaction_id; // ID unik untuk setiap transaksi
    private String user_id;        // ID buyer yang melakukan transaksi
    private String item_id;        // ID item yang dibeli
    private String item_name;      // Nama item
    private String item_category;  // Kategori item
    private String item_size;      // Ukuran item
    private String item_price;     // Harga item

    private static Database db = Database.getInstance(); // Instance database untuk koneksi

    /*
     * Konstruktor default untuk kelas Transaction.
     */
    public Transaction() {}

    /*
     * Konstruktor dengan parameter untuk inisialisasi objek Transaction.
     * Parameter:
     * - transaction_id: ID transaksi.
     * - user_id: ID buyer.
     * - item_id: ID item yang terkait dengan transaksi.
     */
    public Transaction(String transaction_id, String user_id, String item_id) {
        super();
        this.transaction_id = transaction_id;
        this.user_id = user_id;
        this.item_id = item_id;
    }

    // Getter dan Setter untuk atribut kelas Transaction.
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

    /*
     * Method generateTransactionId digunakan untuk membuat ID transaksi unik secara acak.
     * ID yang dihasilkan akan dicek ke database agar tidak duplikat.
     * 
     * Return: String, ID transaksi yang unik.
     */
    private static String generateTransactionId() {
        String query = "SELECT transaction_id FROM transaction WHERE transaction_id = ?";
        PreparedStatement ps = db.preparedStatement(query);
        String randId = null;

        try {
            // Membuat ID transaksi dengan awalan "TS" dan 6 karakter acak
            randId = "TS" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);

            ps.setString(1, randId);

            ResultSet rs = ps.executeQuery();

            // Jika ID tidak ditemukan di database, kembalikan ID
            if (!rs.next()) {
                return randId;
            }

            String checkId = rs.getString("transaction_id");

            // Jika ID ditemukan, generate ulang
            if (checkId.equals(randId)) {
                return generateTransactionId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return randId;
    }

    /*
     * Method viewPurchaseHistory digunakan untuk mengambil riwayat transaksi buyer tertentu.
     * Parameter:
     * - user_id: ID pengguna yang riwayat transaksinya ingin dilihat.
     * 
     * Return: ObservableList<Transaction>, daftar transaksi yang dilakukan pengguna.
     */
    public static ObservableList<Transaction> viewPurchaseHistory(String user_id) {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = "SELECT transaction.transaction_id, items.item_name, items.item_category, items.item_size, items.item_price " +
                       "FROM transaction " +
                       "JOIN items ON transaction.item_id = items.item_id " +
                       "WHERE transaction.user_id = ?";
        PreparedStatement ps = db.preparedStatement(query);
        try {
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Buat objek Transaction dan tambahkan ke daftar
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

    /*
     * Method purchaseItem digunakan untuk menyimpan transaksi pembelian item oleh buyer.
     * Parameter:
     * - user_id: ID pengguna yang membeli item.
     * - item_id: ID item yang dibeli.
     * 
     * Return: Integer, 1 jika pembelian berhasil disimpan, 0 jika gagal.
     */
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

