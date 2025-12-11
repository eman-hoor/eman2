/**
 * Represents an administrator account in the library system.
 * Each admin has a username, password, and email address.
 *
 * @author hoor
 * @version 1.0
 */

public class Admin {
	/**
     * The unique username of the admin.
     */
    private String username;
    /**
     * The password used for authentication.
     */
    private String password;
    /**
     * The email address of the admin.
     */
    private String email;
    /**
     * Constructs a new Admin with a default email address
     * based on the username (username@library.com).
     *
     * @param username the admin's username
     * @param password the admin's password
     */

  
    public Admin(String username, String password) {
        this(username, password, username + "@library.com");
    }
    /**
     * Constructs a new Admin with the specified username, password, and email.
     *
     * @param username the admin's username
     * @param password the admin's password
     * @param email    the admin's email address
     */


    public Admin(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    /**
     * Gets the admin's username.
     *
     * @return the username
     */

    // Getters
    public String getUsername() { 
        return username; 
    }
    /**
     * Gets the admin's password.
     *
     * @return the password
     */

    public String getPassword() { 
        return password; 
    }
    /**
     * Gets the admin's email address.
     *
     * @return the email address
     */

    public String getEmail() { 
        return email; 
    }
}
