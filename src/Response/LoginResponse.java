package Response;

/*
 * Kelas LoginResponse digunakan sebagai objek respons untuk proses autentikasi pengguna.
 * Kelas ini menyimpan informasi tentang status login, pesan, dan peran pengguna.
 * 
 * Kelas ini biasanya digunakan untuk mengirimkan respons dari server ke klien 
 * setelah proses login dilakukan.
 */
public class LoginResponse {
    private boolean success;  // Menyimpan status keberhasilan login (true jika berhasil, false jika gagal)
    private String message;   // Pesan terkait hasil login (misalnya "Login berhasil" atau "Password salah")
    private String role;      // Peran pengguna setelah login berhasil (misalnya "Buyer", "Seller", "Admin")

    /*
     * Konstruktor untuk menginisialisasi LoginResponse dengan nilai success, message, dan role.
     * 
     * Parameter:
     * - success: Status keberhasilan login.
     * - message: Pesan yang akan diberikan terkait hasil login.
     * - role: Peran pengguna jika login berhasil.
     */
    public LoginResponse(boolean success, String message, String role) {
        this.success = success;
        this.message = message;
        this.role = role;
    }

    /*
     * Method isSuccess digunakan untuk mendapatkan status keberhasilan login.
     * 
     * Return:
     * - boolean: true jika login berhasil, false jika gagal.
     */
    public boolean isSuccess() {
        return success;
    }

    /*
     * Method getMessage digunakan untuk mendapatkan pesan hasil login.
     * 
     * Return:
     * - String: Pesan terkait hasil login (misalnya "Login berhasil" atau "Username tidak ditemukan").
     */
    public String getMessage() {
        return message;
    }

    /*
     * Method getRole digunakan untuk mendapatkan peran pengguna setelah login berhasil.
     * 
     * Return:
     * - String: Peran pengguna, misalnya "Buyer", "Seller", atau "Admin".
     */
    public String getRole() {
        return role;
    }
}

