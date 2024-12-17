package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import database.Database;

/*
 * Kelas User digunakan untuk merepresentasikan data pengguna dalam sistem.
 * Kelas ini mencakup atribut dasar pengguna seperti ID pengguna, username, password,
 * nomor telepon, alamat, dan peran pengguna.
 * 
 * Kelas ini juga menyediakan metode statis untuk menangani fitur autentikasi
 * seperti registrasi, login, dan validasi data pengguna.
 */
public class User {

    private String user_id;   // ID unik untuk setiap pengguna
    private String username;  // Username pengguna
    private String password;  // Password pengguna
    private String phone;     // Nomor telepon pengguna
    private String address;   // Alamat pengguna
    private String roles;     // Peran pengguna (Admin, Seller, Buyer)

    private static Database db = Database.getInstance(); // Instance database untuk koneksi
    private static boolean exists = false; // Digunakan untuk memeriksa keberadaan data

    /*
     * Konstruktor default untuk kelas User.
     */
    public User() {}

    /*
     * Konstruktor untuk menginisialisasi objek User dengan parameter.
     * Parameter:
     * - user_id: ID unik pengguna.
     * - username: Username pengguna.
     * - password: Password pengguna.
     * - phone: Nomor telepon pengguna.
     * - address: Alamat pengguna.
     * - roles: Peran pengguna.
     */
    public User(String user_id, String username, String password, String phone, String address, String roles) {
        super();
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.roles = roles;
    }

    // Getter dan Setter untuk atribut kelas User.
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    /*
     * Method generateUserId digunakan untuk membuat ID pengguna secara acak.
     * ID ini akan dicek ke database untuk memastikan tidak ada ID yang duplikat.
     * 
     * Return: String, ID unik pengguna.
     */
    private static String generateUserId() {
        String query = "SELECT user_id FROM users WHERE user_id = ?";
        PreparedStatement ps = db.preparedStatement(query);

        String randId = null;

        try {
            // Membuat ID unik dengan format "US" + karakter acak
            randId = "US" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
            System.out.println(randId);

            ps.setString(1, randId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return randId;
            }

            String checkId = rs.getString("user_id");

            if (checkId.equals(randId)) {
                return generateUserId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return randId;
    }

    /*
     * Method register digunakan untuk mendaftarkan pengguna baru ke database.
     * Parameter:
     * - username: Username pengguna.
     * - password: Password pengguna.
     * - phone: Nomor telepon pengguna.
     * - address: Alamat pengguna.
     * - roles: Peran pengguna (Admin, Buyer, Seller).
     * 
     * Return: Integer, 1 jika registrasi berhasil, 0 jika gagal.
     */
    public static int register(String username, String password, String phone, String address, String roles) {
        String id = generateUserId();
        String query = "INSERT INTO users (user_id, username, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = db.preparedStatement(query);
        int result = 0;

        try {
            ps.setString(1, id);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setString(6, roles);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
     * Method login digunakan untuk memverifikasi kredensial pengguna dari database.
     * Parameter:
     * - username: Username pengguna.
     * - password: Password pengguna.
     * 
     * Return: User, objek pengguna jika kredensial valid, null jika tidak valid.
     */
    public static User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement ps = db.preparedStatement(query);
        User user = null;

        try {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String Id = rs.getString("user_id");
                String Username = rs.getString("username");
                String Password = rs.getString("password");
                String PhoneNumber = rs.getString("phone_number");
                String Address = rs.getString("address");
                String Roles = rs.getString("role");
                user = new User(Id, Username, Password, PhoneNumber, Address, Roles);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /*
     * Method validateUsername digunakan untuk memeriksa apakah username sudah ada di database.
     * Parameter:
     * - username: Username yang akan divalidasi.
     * 
     * Return: Boolean, true jika username sudah ada, false jika belum.
     */
    public static boolean validateUsername(String username) {
        String query = "SELECT username FROM users WHERE username = ?";
        PreparedStatement ps = db.preparedStatement(query);

        try {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /*
     * Method getUserByUsernameAndPassword digunakan untuk mengecek keberadaan pengguna
     * dengan kombinasi username dan password tertentu.
     * Parameter:
     * - username: Username pengguna.
     * - password: Password pengguna.
     * 
     * Return: Boolean, true jika pengguna ditemukan, false jika tidak.
     */
    public static boolean getUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT username, password FROM users WHERE username = ? AND password = ?";
        PreparedStatement ps = db.preparedStatement(query);
        try {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
