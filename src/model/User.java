package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;

public class User {
	private String user_id;
	private String username;
	private String password;
	private String phone;
	private String address;
	private String roles;
	private static Database db = Database.getInstance();
	private static boolean exists = false;

	public User(String user_id, String username, String password, String phone, String address, String roles) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.roles = roles;
	}

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

	private static String generateUserId() {
		String query = "SELECT user_id FROM users ORDER BY user_id DESC";
		PreparedStatement ps = db.preparedStatement(query);

		try {
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return "US001";
			}

			String lastId = rs.getString("user_id");

			if (!lastId.isEmpty()) {
				int lastIdToNumber = Integer.parseInt(lastId.substring(3));
				lastIdToNumber++;
				return String.format("US%03d", lastIdToNumber);
			} else {
				return "US001";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "US001";

	}

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

	public static User login(String username, String password) {
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";
		PreparedStatement ps = db.preparedStatement(query);
		User user = null;
		try {
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String id = rs.getString("user_id");
				String Username = rs.getString("username");
				String Password = rs.getString("password");
				String PhoneNumber = rs.getString("phone_number");
				String Address = rs.getString("address");
				String Roles = rs.getString("role");
				user = new User(id, Username, Password, PhoneNumber, Address, Roles);
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
	}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
	}

}
