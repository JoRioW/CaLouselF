package controller;

import javafx.collections.ObservableList;
import model.Item;

/*
 * Kelas ItemController mengelola berbagai fungsi terkait item yang digunakan dalam aplikasi.
 * Kelas ini menangani validasi data item, mengatur operasi CRUD (Create, Read, Update, Delete) untuk item,
 * serta memproses penawaran harga item oleh pembeli.
 */
public class ItemController {
    private static String message = "";  // Variabel untuk menyimpan pesan error atau sukses yang dikembalikan dari fungsi

    /*
     * Validasi nama item.
     * Mengecek apakah nama item kosong atau kurang dari 3 karakter.
     * 
     * Return:
     * - String: Pesan error jika nama item tidak valid, atau string kosong jika valid.
     */
    private static String checkItemName(String item_name) {
        if (item_name.isEmpty()) {
            return message = "Item name cannot be empty";
        } else if (item_name.length() < 3) {
            return message = "Item name must be at least 3 character long";
        }
        return message = "";
    }

    /*
     * Validasi kategori item.
     * Mengecek apakah kategori item kosong atau kurang dari 3 karakter.
     * 
     * Return:
     * - String: Pesan error jika kategori item tidak valid, atau string kosong jika valid.
     */
    private static String checkItemCategory(String item_category) {
        if (item_category.isEmpty()) {
            return message = "Item category cannot be empty";
        } else if (item_category.length() < 3) {
            return message = "Item category must be at least 3 character long";
        }
        return message = "";
    }

    /*
     * Validasi ukuran item.
     * Mengecek apakah ukuran item kosong.
     * 
     * Return:
     * - String: Pesan error jika ukuran item kosong, atau string kosong jika valid.
     */
    private static String checkItemSize(String item_size) {
        if (item_size.isEmpty()) {
            return message = "Item size cannot be empty";
        }
        return message = "";
    }

    /*
     * Validasi harga item.
     * Mengecek apakah harga item kosong atau 0, dan memastikan harga item berupa angka.
     * 
     * Return:
     * - String: Pesan error jika harga item tidak valid, atau string kosong jika valid.
     */
    private static String checkItemPrice(String item_price) {
        if (item_price.isEmpty()) {
            return message = "Item price cannot be empty";
        } else if (item_price.equals("0")) {
            return message = "Item price cannot be 0";
        } else {
            for (int i = 0; i < item_price.length(); i++) {
                char currPrice = item_price.charAt(i);
                if (!Character.isDigit(currPrice)) {
                    return message = "Item price must be a number";
                }
            }
        }
        return message = "";
    }

    /*
     * Fungsi untuk memvalidasi data item secara keseluruhan.
     * Memanggil semua metode validasi dan mengembalikan pesan error jika ada.
     * 
     * Return:
     * - String: "Success" jika semua validasi berhasil, atau pesan error jika ada yang tidak valid.
     */
    public static String checkItemValidation(String item_name, String item_category, String item_size, String item_price) {
        if (!checkItemName(item_name).isEmpty()) {
            return message;
        } else if (!checkItemCategory(item_category).isEmpty()) {
            return message;
        } else if (!checkItemSize(item_size).isEmpty()) {
            return message;
        } else if (!checkItemPrice(item_price).isEmpty()) {
            return message;
        }

        return "Success";
    }

    /*
     * Menghapus item berdasarkan item_id.
     * 
     * Parameter:
     * - item_id: ID item yang akan dihapus.
     * 
     * Return:
     * - String: "Success" jika item berhasil dihapus, "Failed" jika terjadi kesalahan.
     */
    public static String deleteItem(String item_id) {
        int result = Item.deleteItem(item_id);
        if (result == 0) {
            return "Failed";
        }
        return "Success";
    }

    /*
     * Menyetujui item berdasarkan item_id.
     * 
     * Parameter:
     * - item_id: ID item yang akan disetujui.
     * 
     * Return:
     * - String: Pesan hasil, bisa "Item approved successfully" atau "Failed to approve item".
     */
    public static String approveItem(String item_id) {
        int result = Item.approveItem(item_id);
        if (result == 0) {
            return "Failed to approve item";
        }
        return "Item approved successfully";
    }

    /*
     * Menampilkan item berdasarkan ID pengguna dan peran pengguna.
     * 
     * Parameter:
     * - currentUserId: ID pengguna yang sedang login.
     * - currentUserRole: Peran pengguna (misalnya Seller, Admin, Buyer).
     * 
     * Return:
     * - ObservableList<Item>: Daftar item yang sesuai dengan role dan statusnya.
     */
    public static ObservableList<Item> viewItem(String currentUserId, String currentUserRole) {
        ObservableList<Item> items = Item.viewItem(currentUserId, currentUserRole);

        if (items != null) {
            return items;
        }
        return null;
    }

    /*
     * Mengupload item baru oleh seller.
     * Validasi dilakukan terlebih dahulu sebelum item di-upload ke database.
     * 
     * Parameter:
     * - sellerId: ID penjual yang mengupload item.
     * - item_name: Nama item.
     * - item_category: Kategori item.
     * - item_size: Ukuran item.
     * - item_price: Harga item.
     * 
     * Return:
     * - String: Pesan hasil ("Success" atau "Failed").
     */
    public static String uploadItem(String sellerId, String item_name, String item_category, String item_size, String item_price) {
        // Validasi item sebelum upload
        String validationResult = checkItemValidation(item_name, item_category, item_size, item_price);
        int result = Item.uploadItem(sellerId, item_name, item_category, item_size, item_price);

        if (result == 0) {
            return "Failed";  // Gagal validasi
        }

        return "Success";
    }

    /*
     * Mengedit item berdasarkan item_id.
     * 
     * Parameter:
     * - item_id: ID item yang akan diubah.
     * - item_name: Nama item.
     * - item_category: Kategori item.
     * - item_size: Ukuran item.
     * - item_price: Harga item.
     * 
     * Return:
     * - String: Pesan hasil ("Success" atau "Failed").
     */
    public static String editItem(String item_id, String item_name, String item_category, String item_size, String item_price) {
        int result = Item.editItem(item_id, item_name, item_category, item_size, item_price);

        if (result == 0) {
            return "Failed";
        }

        return "Success";
    }

    /*
     * Memperbarui status pembelian item menjadi "Purchased".
     * 
     * Parameter:
     * - item_id: ID item yang dibeli.
     * 
     * Return:
     * - String: Pesan hasil ("Success" atau "Failed").
     */
    public static String updatePurchase(String item_id) {
        int result = Item.updatePurchase(item_id);

        if (result == 0) {
            return "Failed";
        }

        return "Success";
    }

    /*
     * Mengajukan tawaran harga untuk sebuah item.
     * 
     * Parameter:
     * - item_id: ID item yang ditawarkan.
     * - buyer_id: ID pembeli yang mengajukan tawaran.
     * - new_offer_price: Harga tawaran baru.
     * 
     * Return:
     * - String: Pesan hasil tawaran ("Offer submitted successfully", "Offer price must be greater than 0", etc.).
     */
    public static String makeOffer(String item_id, String buyer_id, String new_offer_price) {
        int result = Item.makeOffer(item_id, buyer_id, new_offer_price);

        switch (result) {
            case -1:
                return "Offer price must be greater than 0.";
            case -2:
                return "Offer price must be higher than the current offer.";
            case 1:
                return "Offer submitted successfully.";
            default:
                return "Failed to submit offer.";
        }
    }

    /*
     * Menerima tawaran harga untuk item tertentu.
     * 
     * Parameter:
     * - item_id: ID item yang tawarannya diterima.
     * - offered_price: Harga tawaran yang diterima.
     * 
     * Return:
     * - String: Pesan hasil ("Offer accepted, transaction created." atau "Failed to accept offer.").
     */
    public static String acceptOffer(String item_id, String offered_price) {
        return Item.acceptOffer(item_id, offered_price);
    }

    /*
     * Menolak tawaran harga untuk item tertentu.
     * 
     * Parameter:
     * - item_id: ID item yang tawarannya ditolak.
     * 
     * Return:
     * - String: Pesan hasil ("Offer declined." atau "Failed to decline offer.").
     */
    public static String declineOffer(String item_id) {
        return Item.declineOffer(item_id);
    }

    /*
     * Menampilkan daftar item yang memiliki tawaran yang tertunda (Pending) oleh seller.
     * 
     * Parameter:
     * - seller_id: ID seller yang itemnya memiliki tawaran tertunda.
     * 
     * Return:
     * - ObservableList<Item>: Daftar item yang memiliki tawaran tertunda.
     */
    public static ObservableList<Item> viewOfferedItems(String seller_id) {
        return Item.viewOfferedItems(seller_id);
    }
}

