public class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public boolean isAdmin() {
        return "Admin".equals(role);
    }

    public boolean isEmployee() {
        return "Employee".equals(role);
    }

    public boolean isCustomer() {
        return "Customer".equals(role);
    }
} 