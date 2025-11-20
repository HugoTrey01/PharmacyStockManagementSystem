package pharmacy;

import java.util.*;

public class PharmacyApp {

    private static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        PharmacyInventory inventory = new PharmacyInventory();

        // Default system users
        User pharmacist = new User("pharmacist", "pharma123", "Pharmacist");
        pharmacist.addPermission("Add");
        pharmacist.addPermission("View");
        pharmacist.addPermission("Update");
        pharmacist.addPermission("Report");
        pharmacist.addPermission("Save");

        User manager = new User("manager", "manage123", "Manager");
        manager.addPermission("View");
        manager.addPermission("Report");

        User admin = new User("admin", "admin123", "Administrator");
        admin.addPermission("Add");
        admin.addPermission("View");
        admin.addPermission("Update");
        admin.addPermission("Delete");
        admin.addPermission("Report");
        admin.addPermission("Save");
        admin.addPermission("CreateUser");

        users.add(pharmacist);
        users.add(manager);
        users.add(admin);

        boolean systemRunning = true;

        // Whole system loop (so logout returns to login screen)
        while (systemRunning) {
            User currentUser = loginUser(input);
            if (currentUser == null) {
                System.out.println("Too many failed attempts. Exiting system...");
                break;
            }

            System.out.println("\nLogin successful! Welcome, " + currentUser.getRole() + ".");

            int choice;
            boolean loggedIn = true;

            // Main menu loop for logged-in user
            while (loggedIn) {
                System.out.println("\n=== TOTAL HOUSE CLINIC - PHARMACY STOCK MANAGEMENT SYSTEM ===");
                System.out.println("1. Add Medicine");
                System.out.println("2. View Stock");
                System.out.println("3. Update Medicine");
                System.out.println("4. Delete Medicine");
                System.out.println("5. Generate Low Stock Report");
                System.out.println("6. Save Data");
                System.out.println("7. Create New User (Admin Only)");
                System.out.println("8. Logout");
                System.out.print("Enter choice: ");

                choice = input.nextInt();
                input.nextLine(); // clear buffer

                switch (choice) {
                    case 1:
                        if (currentUser.hasPermission("Add")) {
                            System.out.print("Enter ID: ");
                            int id = input.nextInt();
                            input.nextLine();
                            System.out.print("Enter Name: ");
                            String name = input.nextLine();
                            System.out.print("Enter Batch Number: ");
                            String batch = input.nextLine();
                            System.out.print("Enter Expiry Date: ");
                            String expiry = input.nextLine();
                            System.out.print("Enter Quantity: ");
                            int qty = input.nextInt();
                            System.out.print("Enter Unit Price: ");
                            double price = input.nextDouble();
                            Medicine m = new Medicine(id, name, batch, expiry, qty, price);
                            inventory.addMedicine(m);
                        } else {
                            System.out.println("Access Denied: You are not allowed to add medicines.");
                        }
                        break;

                    case 2:
                        if (currentUser.hasPermission("View")) {
                            inventory.viewStock();
                        } else {
                            System.out.println("Access Denied: You are not allowed to view stock.");
                        }
                        break;

                    case 3:
                        if (currentUser.hasPermission("Update")) {
                            System.out.print("Enter Medicine ID to update: ");
                            int uid = input.nextInt();
                            System.out.print("Enter new quantity: ");
                            int newQty = input.nextInt();
                            inventory.updateMedicine(uid, newQty);
                        } else {
                            System.out.println("Access Denied: You are not allowed to update stock.");
                        }
                        break;

                    case 4:
                        if (currentUser.hasPermission("Delete")) {
                            System.out.print("Enter Medicine ID to delete: ");
                            int did = input.nextInt();
                            inventory.deleteMedicine(did);
                        } else {
                            System.out.println("Access Denied: Only users with Delete permission can do this.");
                        }
                        break;

                    case 5:
                        if (currentUser.hasPermission("Report")) {
                            inventory.generateReport();
                        } else {
                            System.out.println("Access Denied: You cannot generate reports.");
                        }
                        break;

                    case 6:
                        if (currentUser.hasPermission("Save")) {
                            inventory.saveToFile("pharmacy_data.txt");
                            System.out.println("Data saved successfully!");
                        } else {
                            System.out.println("Access Denied: You are not allowed to save data.");
                        }
                        break;

                    case 7:
                        if (currentUser.hasPermission("CreateUser")) {
                            createUser(input);
                        } else {
                            System.out.println("Access Denied: Only Administrators can create new user accounts.");
                        }
                        break;

                    case 8:
                        System.out.println("Logging out... Goodbye, " + currentUser.getUsername() + "!");
                        loggedIn = false; // Exit current user's session
                        break;

                    default:
                        System.out.println("Invalid choice, try again.");
                }
            }
        }

        input.close();
    }

    // ---------------- Helper Methods ----------------

    // Login method (returns User object or null)
    private static User loginUser(Scanner input) {
        User currentUser = null;
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.println("\n=== LOGIN PAGE ===");
            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();

            currentUser = authenticateUser(username, password);
            if (currentUser != null) {
                return currentUser;
            } else {
                System.out.println("Invalid credentials. Attempts left: " + (2 - attempts));
            }
        }
        return null;
    }

    // Authenticate user
    private static User authenticateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // Admin creates new user + assigns permissions
    private static void createUser(Scanner input) {
        System.out.println("\n--- Create New User Account ---");
        System.out.print("Enter new username: ");
        String newUsername = input.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = input.nextLine();
        System.out.print("Enter role (Pharmacist / Manager / Administrator): ");
        String newRole = input.nextLine();

        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(newUsername)) {
                System.out.println("Error: Username already exists.");
                return;
            }
        }

        User newUser = new User(newUsername, newPassword, newRole);

        // Assign permissions
        System.out.println("\nAssign permissions to this user:");
        String[] actions = {"Add", "View", "Update", "Delete", "Report", "Save", "CreateUser"};
        for (String action : actions) {
            System.out.print("Allow " + action + "? (y/n): ");
            String response = input.nextLine();
            if (response.equalsIgnoreCase("y")) {
                newUser.addPermission(action);
            }
        }

        users.add(newUser);
        System.out.println("✅ New user created successfully!");
        newUser.listPermissions();
    }
}
