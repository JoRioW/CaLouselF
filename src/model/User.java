package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Database;

public class User {
	private int user_id;
	private String username;
	private String password;
	private String phone;
	private String address;
	private String roles;
	private static Database db = Database.getInstance();
	
	public User(int user_id, String username, String password, String phone, String address, String roles) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.roles = roles;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
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

	public static int register(
			String username,
			String password,
			String phone,
			String address,
			String roles
			)
	{
		String query = "INSERT INTO users (username, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = db.preparedStatement(query);
		int result = 0;
		
		try {
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, phone);
			ps.setString(4, address);
			ps.setString(5, roles);
			//berapa row yg berubah
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static User login(
			String username,
			String password,
			String phone,
			String address,
			String roles
			)
	{
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		PreparedStatement ps = db.preparedStatement(query);
		User user = null;
		try {
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("user_id");
				String Username = rs.getString("username");
				String Password = rs.getString("password");
				String PhoneNumber = rs.getString("phone_number");
				String Address = rs.getString("address");
				String Roles = rs.getString("role");
				user = new User(id, Username, Password, PhoneNumber, Address, Roles);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	public static boolean validateUsername(String username) {
		boolean exists = false;
		String query = "SELECT username FROM users WHERE username = ?";
		PreparedStatement ps = db.preparedStatement(query);
		
		try {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
		
	}
	
}
