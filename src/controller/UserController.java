package controller;

import Response.LoginResponse;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import model.User;

/*
 * Kelas UserController berfungsi untuk mengelola proses pendaftaran dan login pengguna.
 * Kelas ini juga berfungsi untuk memvalidasi inputan pengguna saat pendaftaran.
 */
public class UserController {
    
    private static boolean isExist = false;  // Menyimpan status apakah pengguna sudah terdaftar atau belum
    private static String message = "";  // Menyimpan pesan kesalahan saat validasi

    /*
     * Memeriksa apakah username valid, apakah sudah ada sebelumnya dan apakah panjangnya sesuai ketentuan.
     * 
     * Parameter:
     * - username: Nama pengguna yang akan diperiksa.
     * 
     * Return:
     * - String: Pesan kesalahan jika validasi gagal, atau kosong jika valid.
     */
    private static String checkUsername(String username) {
        if (username.isEmpty()) {
            return message = "Username must be filled";
        } else if (username.length() < 3) {
            return message = "Username must be more than 3 characters ";
        } else if (User.validateUsername(username) == true) {
            return message = "Username already exist";
        }
        return message = "";
    }

    /*
     * Memeriksa apakah password mengandung karakter khusus sesuai dengan ketentuan.
     * 
     * Parameter:
     * - password: Password yang akan diperiksa.
     * 
     * Return:
     * - boolean: true jika password mengandung karakter khusus, false jika tidak.
     */
    private static boolean checkSpecialCharacter(String password) {
        String specialCharacter = "!@#$%^&*";
        for (int i = 0; i < password.length(); i ++) {
            char character = password.charAt(i);
            if(specialCharacter.indexOf(character) >= 0) {
                return true;
            }
        }
        return false;
    }

    /*
     * Memeriksa apakah password valid, termasuk panjang dan karakter khusus.
     * 
     * Parameter:
     * - password: Password yang akan diperiksa.
     * 
     * Return:
     * - String: Pesan kesalahan jika validasi gagal, atau kosong jika valid.
     */
    private static String checkPassword(String password) {
        if (password.isEmpty()) {
            return message = "Password must be filled";
        } else if (password.length() < 8) {
            return message = "Password must be more than 8 characters";
        } else if (checkSpecialCharacter(password) == false) {
            return message = "Password must include special character";
        }
        return message = "";
    }

    /*
     * Memeriksa apakah nomor telepon valid, termasuk format nomor Indonesia.
     * 
     * Parameter:
     * - phone: Nomor telepon yang akan diperiksa.
     * 
     * Return:
     * - String: Pesan kesalahan jika validasi gagal, atau kosong jika valid.
     */
    private static String checkPhoneNumber(String phone) {
        if(phone.isEmpty()) {
            return message = "Phone Number must be filled";
        }
        else if (!phone.startsWith("+62")) {
            return message = "Phone Number must starts with +62";
        } else {
            if (phone.length() != 13) {
                return message = "Phone Number must be 10 numbers long after +62";
            }
            for (int i = 3; i < phone.length(); i++) {
                char currNum = phone.charAt(i);
                if (!Character.isDigit(currNum)) {
                    return message = "Phone Number must be numeric";
                }
            }
        }
        return message = "";
    }

    /*
     * Memeriksa apakah alamat valid.
     * 
     * Parameter:
     * - address: Alamat yang akan diperiksa.
     * 
     * Return:
     * - String: Pesan kesalahan jika validasi gagal, atau kosong jika valid.
     */
    private static String checkAddress(String address) {
        if (address.isEmpty()) {
            return message = "Address must be filled";
        }
        return message = "";
    }

    /*
     * Memeriksa apakah role sudah dipilih oleh pengguna.
     * 
     * Parameter:
     * - role: ToggleGroup yang berisi pilihan role pengguna.
     * 
     * Return:
     * - String: Pesan kesalahan jika validasi gagal, atau kosong jika valid.
     */
    private static String checkRole(ToggleGroup role) {
        RadioButton selectedRole = (RadioButton) role.getSelectedToggle();
        if (selectedRole == null) {
            return message = "Please choose Roles";
        }
        return message = "";
    }

    /*
     * Memeriksa validasi semua input pengguna saat pendaftaran.
     * 
     * Parameter:
     * - username: Nama pengguna.
     * - password: Password.
     * - phone: Nomor telepon.
     * - address: Alamat.
     * - role: Role pengguna.
     * 
     * Return:
     * - String: Pesan kesalahan jika validasi gagal, atau "Success" jika valid.
     */
    public static String checkAccountValidation(String username, String password, String phone, String address,
            ToggleGroup role) {

        if (!checkUsername(username).isEmpty()) {
            return message;
        } else if (!checkPassword(password).isEmpty()) {
            return message;
        } else if (!checkPhoneNumber(phone).isEmpty()) {
            return message;
        } else if (!checkAddress(address).isEmpty()) {
            return message;
        } else if (!checkRole(role).isEmpty()) {
            return message;
        }

        return "Success";
    }

    /*
     * Melakukan proses pendaftaran pengguna.
     * 
     * Parameter:
     * - username: Nama pengguna.
     * - password: Password.
     * - phone: Nomor telepon.
     * - address: Alamat.
     * - role: Role pengguna.
     * 
     * Return:
     * - String: Pesan hasil ("Success" atau "Failed").
     */
    public static String register(String username, String password, String phone, String address, String role) {
        int result = User.register(username, password, phone, address, role);
        if (result == 0) {
            return "Failed";
        }
        return "Success";
    }

    /*
     * Melakukan proses login pengguna.
     * 
     * Parameter:
     * - username: Nama pengguna.
     * - password: Password.
     * 
     * Return:
     * - LoginResponse: Objek yang berisi status login (berhasil/gagal), pesan, dan role pengguna.
     */
    public static LoginResponse login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return new LoginResponse(false, "All fields must be filled", null);
        }

        boolean isExist = User.getUserByUsernameAndPassword(username, password);
        if (!isExist) {
            return new LoginResponse(false, "Credentials are incorrect", null);
        }

        User user = User.login(username, password);
        if (user != null) {
            return new LoginResponse(true, "Login successful", user.getRoles());
        }

        return new LoginResponse(false, "Login failed", null);
    }
}

