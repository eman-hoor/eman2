import java.util.HashMap;
import java.util.Map;

/**
 * Service class that manages administrator authentication.
 * Provides functionality to add admins, login, logout,
 * and track the currently logged-in admin.
 *
 * @author hoor
 * @version 1.0
 */

public class AuthService {
	/**
     * Map of registered admins keyed by their username (lowercased).
     */
	private Map<String, Admin> admins = new HashMap<>();
	/**
     * The currently logged-in admin, or {@code null} if no one is logged in.
     */
    private Admin currentAdmin; // track which admin is logged in
    /**
     * Constructs a new AuthService with two example admin accounts.
     * <p>
     * Default accounts:
     * <ul>
     *   <li>hoor / hoor122</li>
     *   <li>eman / eman123</li>
     * </ul>
     */

    public AuthService() { 
        // Example:
        admins.put("hoor", new Admin("hoor", "hoor122"));
        admins.put("eman", new Admin("eman", "eman123"));
    }
    /**
     * Adds a new admin account to the system.
     *
     * @param username the admin's username
     * @param password the admin's password
     * @throws IllegalArgumentException if an admin with the same username already exists
     */

    // Add a new admin account
    public void addAdmin(String username, String password) {
        if (admins.containsKey(username.toLowerCase())) {
            throw new IllegalArgumentException("Admin with this username already exists.");
        }
        admins.put(username.toLowerCase(), new Admin(username, password));
    }
    /**
     * Attempts to log in with the given credentials.
     *
     * @param username the admin's username
     * @param password the admin's password
     * @return {@code true} if login succeeds, {@code false} otherwise
     */

    // Login with credentials
    public boolean login(String username, String password) {
        Admin a = admins.get(username.toLowerCase());
        if (a != null && a.getPassword().equals(password)) {
            currentAdmin = a;
            return true;
        }
        return false;
    }
 
    /**
     * Logs out the currently logged-in admin.
     */
    // Logout current admin
    public void logout() {
        currentAdmin = null;
    }
    /**
     * Checks if an admin is currently logged in.
     *
     * @return {@code true} if an admin is logged in, {@code false} otherwise
     */

    // Check if someone is logged in
    public boolean isLoggedIn() {
        return currentAdmin != null;
    }

    /**
     * Gets the currently logged-in admin.
     *
     * @return the current admin, or {@code null} if no one is logged in
     */
    // Get the currently logged-in admin
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }
}
