package pharmacy;

import java.util.*;

public class User {
    private String username;
    private String password;
    private String role;
    private Set<String> permissions = new HashSet<>();

    // Constructor for default users
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructor for users with permissions (used when loading from file or creating new ones)
    public User(String username, String password, String role, String[] permissionsArray) {
        this.username = username;
        this.password = password;
        this.role = role;
        for (String perm : permissionsArray) {
            if (!perm.isEmpty()) {
                permissions.add(perm.toLowerCase());
            }
        }
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Set<String> getPermissions() { return permissions; }

    // Permission Management
    public void addPermission(String permission) {
        permissions.add(permission.toLowerCase());
    }

    public boolean hasPermission(String action) {
        return permissions.contains(action.toLowerCase());
    }

    public void listPermissions() {
        System.out.println("Permissions for " + username + ": " + permissions);
    }

    // Utility method for saving to file (turns set into semicolon-separated list)
    public String getPermissionsAsString() {
        return String.join(";", permissions);
    }
}
