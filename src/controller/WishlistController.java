package controller;

import javafx.collections.ObservableList;
import model.Item;
import model.Wishlist;

/*
 * Kelas WishlistController berfungsi untuk mengelola data wishlist pengguna.
 * Kelas ini berinteraksi dengan model Wishlist untuk menambahkan, menghapus, dan menampilkan item wishlist.
 */
public class WishlistController {

    /*
     * Menampilkan daftar item yang ada di wishlist pengguna.
     * 
     * Parameter:
     * - user_id: ID pengguna yang wishlist-nya ingin ditampilkan.
     * 
     * Return:
     * - ObservableList<Item>: Daftar item yang ada di wishlist pengguna.
     *         Jika tidak ada item, mengembalikan null.
     */
    public static ObservableList<Item> viewWishlist(String user_id) {
        ObservableList<Item> wishlistItem = Wishlist.viewWishlist(user_id);
        
        if (wishlistItem != null) {
            return wishlistItem;
        }
        return null;
    }

    /*
     * Menambahkan item ke wishlist pengguna.
     * 
     * Parameter:
     * - user_id: ID pengguna yang ingin menambahkan item ke wishlist.
     * - item_id: ID item yang akan ditambahkan ke wishlist.
     * 
     * Return:
     * - String: "Success" jika item berhasil ditambahkan, "Failed" jika gagal.
     */
    public static String addWishlist(String user_id, String item_id) {
        int result = Wishlist.addWishlist(user_id, item_id);
        
        if(result == 0) {
            return "Failed";  // Menandakan proses gagal
        }
        
        return "Success";  // Menandakan item berhasil ditambahkan ke wishlist
    }

    /*
     * Menghapus item dari wishlist pengguna.
     * 
     * Parameter:
     * - user_id: ID pengguna yang item-nya ingin dihapus dari wishlist.
     * - item_id: ID item yang akan dihapus dari wishlist.
     * 
     * Return:
     * - String: "Success" jika item berhasil dihapus, "Failed" jika gagal.
     */
    public static String removeWishlist(String user_id, String item_id) {
        int result = Wishlist.removeWishlist(user_id, item_id);
        
        if (result == 0) {
            return "Failed";  // Menandakan proses gagal
        }
        
        return "Success";  // Menandakan item berhasil dihapus dari wishlist
    }
}

