package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
	private String USERNAME = "root";
	private String PASSWORD = "";
	private String HOST = "localhost:3306";
	private String DATABASE = "calouself";
	private String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	Connection con = null;
	private static Database instance = null;
	public Database() {
		try {
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	public PreparedStatement preparedStatement(String query) {
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	
}
