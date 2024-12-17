package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class Database digunakan untuk mengatur koneksi ke database MySQL
 * dan menyediakan instance yang sama di seluruh aplikasi menggunakan
 * pola desain Singleton.
 */
public class Database {
	private String USERNAME = "root";
	private String PASSWORD = "";
	private String HOST = "localhost:3306";
	private String DATABASE = "calouself";
	private String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	Connection con = null; 
	private static Database instance = null;

	/**
	 * Constructor Database digunakan untuk membuat koneksi ke database.
	 * Jika koneksi berhasil, instance Connection akan diinisialisasi.
	 */
	public Database() {
		try {
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			// Menampilkan error jika koneksi database gagal
			e.printStackTrace();
		}
	}
	
	/**
	 * Method getInstance memastikan hanya ada satu instance Database
	 * yang digunakan di seluruh aplikasi (Singleton Pattern).
	 * Mengembalikan instance Database yang sudah ada atau baru dibuat.
	 */
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	/**
	 * Method preparedStatement digunakan untuk mempersiapkan query SQL
	 * yang akan dieksekusi dengan parameter terikat.
	 * Parameter query berisi perintah SQL dalam bentuk string.
	 * Mengembalikan PreparedStatement yang sudah siap digunakan.
	 */
	public PreparedStatement preparedStatement(String query) {
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement(query);
		} catch (SQLException e) {
			// Menampilkan error jika pembuatan PreparedStatement gagal
			e.printStackTrace();
		}
		return ps;
	}
}
