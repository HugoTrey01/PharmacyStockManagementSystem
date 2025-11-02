package pharmacy;

import java.util.*;

public class User {
    private String username;
    private String password;
    private String role;
    private Set<String> permissions = new HashSet<>();

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public boolean hasPermission(String action) {
        return permissions.contains(action);
    }

    public void listPermissions() {
        System.out.println("Permissions for " + username + ": " + permissions);
    }
}
