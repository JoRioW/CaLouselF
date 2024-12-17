package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* 
 * Class Item digunakan untuk merepresentasikan entitas item. 
 * Item mencakup atribut seperti ID, nama, ukuran, harga, kategori, status,
 * wishlist, status penawaran, dan harga penawaran.
 * Class ini juga menyediakan operasi CRUD pada item serta fitur transaksi dan penawaran.
 */
public class Item {
    private String item_id;
    private String item_name;
    private String item_size;
    private String item_price;
    private String item_category;
    private String item_status;
    private String item_wishlist;
    private String item_offer_status;
    private String offered_price;
    private static Database db = Database.getInstance();
    private static int result = 0;

    /* 
     * Constructor default untuk inisialisasi objek kosong.
     */
    public Item() {}

    /*
     * Constructor untuk membuat instance Item dengan semua atribut.
     * Parameter:
     * - item_id: ID untuk item.
     * - item_name: Nama dari item.
     * - item_size: Ukuran item.
     * - item_price: Harga item.
     * - item_category: Kategori item.
     * - item_status: Status dari item seperti 'Pending', 'Approved', atau 'Purchased'.
     * - item_wishlist: Status wishlist item.
     * - item_offer_status: Status penawaran item, misalnya 'Pending' atau 'Accepted'.
     * - offered_price: Harga yang ditawarkan oleh buyer.
     */
    public Item(String item_id, String item_name, String item_size, String item_price, String item_category,
                String item_status, String item_wishlist, String item_offer_status, String offered_price) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_size = item_size;
        this.item_price = item_price;
        this.item_category = item_category;
        this.item_status = item_status;
        this.item_wishlist = item_wishlist;
        this.item_offer_status = item_offer_status;
        this.offered_price = offered_price;
    }

    // Getter dan Setter untuk atribut item
    public String getItem_id() { return item_id; }
    public void setItem_id(String item_id) { this.item_id = item_id; }

    public String getItem_name() { return item_name; }
    public void setItem_name(String item_name) { this.item_name = item_name; }

    public String getItem_size() { return item_size; }
    public void setItem_size(String item_size) { this.item_size = item_size; }

    public String getItem_price() { return item_price; }
    public void setItem_price(String item_price) { this.item_price = item_price; }

    public String getItem_category() { return item_category; }
    public void setItem_category(String item_category) { this.item_category = item_category; }

    public String getItem_status() { return item_status; }
    public void setItem_status(String item_status) { this.item_status = item_status; }

    public String getItem_wishlist() { return item_wishlist; }
    public void setItem_wishlist(String item_wishlist) { this.item_wishlist = item_wishlist; }

    public String getItem_offer_status() { return item_offer_status; }
    public void setItem_offer_status(String item_offer_status) { this.item_offer_status = item_offer_status; }

    public String getOffered_price() { return offered_price; }
    public void setOffered_price(String offered_price) { this.offered_price = offered_price; }

    /* 
     * Method generateItemId digunakan untuk membuat ID unik untuk item.
     * ID akan di-generate dengan format 'IT' diikuti oleh 6 karakter acak.
     */
    private static String generateItemId() {
        String query = "SELECT item_id FROM items WHERE item_id = ?";
        PreparedStatement ps = db.preparedStatement(query);
        String randId = null;

        try {
            randId = "IT" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
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
            e.printStackTrace();
        }
        return randId;
    }

    /* 
     * Method uploadItem digunakan untuk menambahkan item baru ke dalam database.
     * Parameter:
     * - sellerId: ID seller yang mengunggah item.
     * - item_name: Nama item.
     * - item_category: Kategori item.
     * - item_size: Ukuran item.
     * - item_price: Harga item.
     * 
     * Return: Integer, menunjukkan hasil operasi (1 jika sukses, 0 jika gagal).
     */
    public static int uploadItem(String sellerId, String item_name, String item_category, String item_size, String item_price) {
        String id = generateItemId();
        String status = "Pending";
        String query = "INSERT INTO items(item_id, seller_id, item_name, item_category, item_size, item_price, item_status) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = db.preparedStatement(query);

        try {
            ps.setString(1, id);
            ps.setString(2, sellerId);
            ps.setString(3, item_name);
            ps.setString(4, item_category);
            ps.setString(5, item_size);
            ps.setString(6, item_price);
            ps.setString(7, status);

            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /* 
     * Method approveItem digunakan untuk memperbarui status item menjadi 'Approved'.
     * Parameter:
     * - item_id: ID item yang akan diperbarui.
     * 
     * Return: Integer, hasil dari operasi update.
     */
    public static int approveItem(String item_id) {
        String query = "UPDATE items SET item_status = 'Approve' WHERE item_id = ?";
        PreparedStatement ps = db.preparedStatement(query);

        try {
            ps.setString(1, item_id);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
     * Method viewItem digunakan untuk melihat daftar item berdasarkan peran pengguna.
     * Parameter:
     * - currentUserId: ID pengguna saat ini.
     * - currentUserRole: Peran pengguna ('Buyer', 'Seller', atau 'Admin').
     * 
     * Return: ObservableList<Item>, daftar item sesuai dengan filter.
     */
    public static ObservableList<Item> viewItem(String currentUserId, String currentUserRole) {
        ObservableList<Item> items = FXCollections.observableArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query;

            switch (currentUserRole) {
                case "Seller":
                    query = "SELECT * FROM items WHERE seller_id = ? AND item_status = ?";
                    ps = db.preparedStatement(query);
                    ps.setString(1, currentUserId);
                    ps.setString(2, "Approve");
                    break;

                case "Admin":
                    query = "SELECT * FROM items WHERE item_status = ?";
                    ps = db.preparedStatement(query);
                    ps.setString(1, "Pending");
                    break;

                case "Buyer":
                default:
                    query = "SELECT * FROM items WHERE item_status = ?";
                    ps = db.preparedStatement(query);
                    ps.setString(1, "Approve");
                    break;
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                Item item = new Item(
                    rs.getString("item_id"),
                    rs.getString("item_name"),
                    rs.getString("item_size"),
                    rs.getString("item_price"),
                    rs.getString("item_category"),
                    rs.getString("item_status"),
                    rs.getString("item_wishlist"),
                    rs.getString("item_offer_status"),
                    rs.getString("offered_price")
                );
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return items;
    }
    
    /*
     * Method editItem digunakan untuk mengubah detail item yang ada di database.
     * Parameter:
     * - item_id: ID item yang ingin diubah.
     * - item_name: Nama baru dari item.
     * - item_category: Kategori baru dari item.
     * - item_size: Ukuran baru dari item.
     * - item_price: Harga baru dari item.
     * 
     * Return: Integer, 1 jika operasi berhasil, 0 jika gagal.
     */
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

    /*
     * Method deleteItem digunakan untuk menghapus item berdasarkan ID item.
     * Parameter:
     * - item_id: ID item yang akan dihapus.
     * 
     * Return: Integer, 1 jika operasi berhasil, 0 jika gagal.
     */
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

    /*
     * Method updatePurchase digunakan untuk memperbarui status item menjadi 'Purchased'.
     * Parameter:
     * - item_id: ID item yang akan diperbarui.
     * 
     * Return: Integer, 1 jika operasi berhasil, 0 jika gagal.
     */
    public static int updatePurchase(String item_id) {
        String query = "UPDATE items SET item_status = 'Purchased' WHERE item_id = ?";
        PreparedStatement ps = db.preparedStatement(query);

        try {
            ps.setString(1, item_id);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
     * Method acceptOffer digunakan oleh seller untuk menerima penawaran dari pembeli.
     * Setelah diterima, status item diperbarui ke 'Purchased' dan transaksi baru akan dibuat.
     * Parameter:
     * - item_id: ID item yang diterima.
     * - offered_price: Harga yang disepakati.
     * 
     * Return: String, pesan keberhasilan atau kegagalan operasi.
     */
    public static String acceptOffer(String item_id, String offered_price) {
        String getBuyerIdQuery = "SELECT buyer_id FROM items WHERE item_id = ?";
        String updateItemQuery = "UPDATE items SET item_status = 'Purchased', item_price = ?, item_offer_status = 'Offered' WHERE item_id = ?";
        String insertTransactionQuery = "INSERT INTO transaction(transaction_id, user_id, item_id) VALUES(?, ?, ?)";

        PreparedStatement psBuyerId = db.preparedStatement(getBuyerIdQuery), psUpdateItem, psInsertTransaction;

        try {
            psBuyerId.setString(1, item_id);
            ResultSet rs = psBuyerId.executeQuery();
            if (rs.next()) {

                // Generate transaction_id
                String buyer_id = rs.getString("buyer_id");
                String transactionId = "TS" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);

                // Update item status to Purchased and set new price
                psUpdateItem = db.preparedStatement(updateItemQuery);
                psUpdateItem.setString(1, offered_price);
                psUpdateItem.setString(2, item_id);
                psUpdateItem.executeUpdate();

                // Insert the transaction
                psInsertTransaction = db.preparedStatement(insertTransactionQuery);
                psInsertTransaction.setString(1, transactionId);
                psInsertTransaction.setString(2, buyer_id);
                psInsertTransaction.setString(3, item_id);
                psInsertTransaction.executeUpdate();

                return "Offer accepted, transaction created.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Failed to accept offer.";
    }

    /*
     * Method declineOffer digunakan untuk menolak penawaran dari buyer.
     * Parameter:
     * - item_id: ID item yang penawarannya akan ditolak.
     * 
     * Return: String, pesan keberhasilan atau kegagalan operasi.
     */
    public static String declineOffer(String item_id) {
        String query = "UPDATE items SET item_offer_status = 'Not Offered' WHERE item_id = ?";
        PreparedStatement ps;

        try {
            ps = db.preparedStatement(query);
            ps.setString(1, item_id);
            ps.executeUpdate();
            return "Offer declined.";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Failed to decline offer.";
    }

    /*
     * Method makeOffer digunakan oleh buyer untuk mengajukan penawaran pada item.
     * Penawaran baru hanya akan diterima jika lebih tinggi dari penawaran sebelumnya.
     * Parameter:
     * - item_id: ID item yang ditawarkan.
     * - buyer_id: ID buyer yang memberikan penawaran.
     * - new_offer_price: Harga penawaran baru.
     * 
     * Return: Integer, kode hasil operasi:
     *         - 1: Penawaran berhasil diajukan.
     *         - 0: Penawaran gagal.
     *         - -1: Harga penawaran tidak boleh 0 atau kurang.
     *         - -2: Harga penawaran harus lebih tinggi dari penawaran sebelumnya.
     */
    public static int makeOffer(String item_id, String buyer_id, String new_offer_price) {
        String queryCheck = "SELECT offered_price FROM items WHERE item_id = ?";
        String queryUpdate = "UPDATE items SET offered_price = ?, item_offer_status = 'Pending', buyer_id = ? WHERE item_id = ?";
        PreparedStatement psCheck, psUpdate;
        ResultSet rs;

        try {
            psCheck = db.preparedStatement(queryCheck);
            psCheck.setString(1, item_id);
            rs = psCheck.executeQuery();

            if (rs.next()) {
                int currentOffer = rs.getInt("offered_price");
                int newOffer = Integer.parseInt(new_offer_price);

                if (newOffer <= 0) {
                    return -1;  // Offer price must be greater than 0.
                } else if (newOffer <= currentOffer) {
                    return -2;  // Offer must be higher than the current offer.
                }

                // Update the item with the new offer price
                psUpdate = db.preparedStatement(queryUpdate);
                psUpdate.setInt(1, newOffer);
                psUpdate.setString(2, buyer_id);
                psUpdate.setString(3, item_id);
                psUpdate.executeUpdate();
                return 1;  // Offer submitted successfully
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }

        return 0;  // Failed to submit offer
    }

    /*
     * Method viewOfferedItems digunakan oleh seller untuk melihat daftar item dengan penawaran yang berstatus 'Pending'.
     * Parameter:
     * - seller_id: ID seller yang ingin melihat penawaran.
     * 
     * Return: ObservableList<Item>, daftar item dengan penawaran 'Pending'.
     */
    public static ObservableList<Item> viewOfferedItems(String seller_id) {
        ObservableList<Item> items = FXCollections.observableArrayList();
        String query = "SELECT * FROM items WHERE seller_id = ? AND item_offer_status = 'Pending'";
        PreparedStatement ps = db.preparedStatement(query);
        ResultSet rs;

        try {
            ps.setString(1, seller_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                Item item = new Item(
                    rs.getString("item_id"),
                    rs.getString("item_name"),
                    rs.getString("item_size"),
                    rs.getString("item_price"),
                    rs.getString("item_category"),
                    rs.getString("item_status"),
                    rs.getString("item_wishlist"),
                    rs.getString("item_offer_status"),
                    rs.getString("offered_price")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
