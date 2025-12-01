import java.util.HashMap;
import java.util.Map;

public class AuthService {
	private Map<String, Admin> admins = new HashMap<>();
    private Admin currentAdmin; // track which admin is logged in

    public AuthService() {
        // Example:
        admins.put("hoor", new Admin("hoor", "hoor122"));
        admins.put("eman", new Admin("eman", "eman123"));
    }

    // Add a new admin account
    public void addAdmin(String username, String password) {
        if (admins.containsKey(username.toLowerCase())) {
            throw new IllegalArgumentException("Admin with this username already exists.");
        }
        admins.put(username.toLowerCase(), new Admin(username, password));
    }

    // Login with credentials
    public boolean login(String username, String password) {
        Admin a = admins.get(username.toLowerCase());
        if (a != null && a.getPassword().equals(password)) {
            currentAdmin = a;
            return true;
        }
        return false;
    }

    // Logout current admin
    public void logout() {
        currentAdmin = null;
    }

    // Check if someone is logged in
    public boolean isLoggedIn() {
        return currentAdmin != null;
    }

    // Get the currently logged-in admin
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }
}
