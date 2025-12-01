public class Admin {

    private String username;
    private String password;
    private String email;

  
    public Admin(String username, String password) {
        this(username, password, username + "@library.com");
    }


    public Admin(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters
    public String getUsername() { 
        return username; 
    }

    public String getPassword() { 
        return password; 
    }

    public String getEmail() { 
        return email; 
    }
}
