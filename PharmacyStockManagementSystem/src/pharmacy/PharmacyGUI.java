package pharmacy;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Enumeration;

public class PharmacyGUI {
	// ---------------------- GLOBAL FONT SETTINGS ----------------------
	private static void setGlobalFont(int size) {
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof FontUIResource) {
	            FontUIResource orig = (FontUIResource) value;
	            // keep original font family and style, but change size
	            String fontFamily = orig.getFamily();
	            int fontStyle = orig.getStyle();
	            UIManager.put(key, new FontUIResource(new Font(fontFamily, fontStyle, size)));
	        }
	    }
	}



    private PharmacyInventory inventory;
    private JFrame loginFrame, mainFrame;
    private User currentUser;
    private ArrayList<User> userList;
    private final String USER_FILE = "users.txt";

    public PharmacyGUI(PharmacyInventory inventory) {
    	setGlobalFont(16);
        this.inventory = inventory;
        this.userList = new ArrayList<>();

        loadUsersFromFile();
        if (userList.isEmpty()) {
            // Default users
            userList.add(new User("admin", "admin123", "Administrator",
                    new String[]{"add", "update", "delete", "view", "report", "manage_users"}));
            userList.add(new User("pharmacist", "pharma123", "Pharmacist",
                    new String[]{"add", "update", "view"}));
            userList.add(new User("manager", "manage123", "Manager",
                    new String[]{"view", "report"}));
            saveUsersToFile();
        }

        showLogin();
    }

    // ---------------------- LOGIN ----------------------
    private void showLogin() {
        loginFrame = new JFrame("Pharmacy System Login");
        loginFrame.setSize(400, 250);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        loginFrame.add(new JLabel("")); 
        loginFrame.add(new JLabel(" TOTAL HOUSE CLINIC -PSMS", SwingConstants.CENTER));
        loginFrame.add(userLabel); loginFrame.add(userField);
        loginFrame.add(passLabel); loginFrame.add(passField);
        loginFrame.add(new JLabel("")); loginFrame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            boolean loggedIn = false;

            for (User user : userList) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = user;
                    loggedIn = true;
                    break;
                }
            }

            if (loggedIn) {
                loginFrame.dispose();
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setVisible(true);
    }

    // ---------------------- MAIN MENU ----------------------
    private void showMainMenu() {
        mainFrame = new JFrame("Pharmacy System - " + currentUser.getRole());
        mainFrame.setSize(400, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new GridLayout(8, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getRole(), SwingConstants.CENTER);
        JButton addBtn = new JButton("Add Medicine");
        JButton viewBtn = new JButton("View Stock");
        JButton updateBtn = new JButton("Update Medicine");
        JButton deleteBtn = new JButton("Delete Medicine");
        JButton reportBtn = new JButton("Generate Low Stock Report");
        JButton manageUsersBtn = new JButton("Manage Users");
        JButton logoutBtn = new JButton("Logout");

        mainFrame.add(welcomeLabel);
        mainFrame.add(addBtn);
        mainFrame.add(viewBtn);
        mainFrame.add(updateBtn);
        mainFrame.add(deleteBtn);
        mainFrame.add(reportBtn);

        // Only show Manage Users if user has that permission
        if (hasPermission("manage_users")) {
            mainFrame.add(manageUsersBtn);
        }

        mainFrame.add(logoutBtn);

        addBtn.addActionListener(e -> {
            if (hasPermission("add")) addMedicineWindow();
            else showPermissionDenied();
        });

        viewBtn.addActionListener(e -> {
            if (hasPermission("view")) viewStockWindow();
            else showPermissionDenied();
        });

        updateBtn.addActionListener(e -> {
            if (hasPermission("update")) updateMedicineWindow();
            else showPermissionDenied();
        });

        deleteBtn.addActionListener(e -> {
            if (hasPermission("delete")) deleteMedicine();
            else showPermissionDenied();
        });

        reportBtn.addActionListener(e -> {
            if (hasPermission("report")) inventory.generateReport();
            else showPermissionDenied();
        });

        manageUsersBtn.addActionListener(e -> manageUsersWindow());

        logoutBtn.addActionListener(e -> {
            mainFrame.dispose();
            showLogin();
        });

        mainFrame.setVisible(true);
    }

    // ---------------------- PERMISSIONS ----------------------
    private boolean hasPermission(String action) {
        if (currentUser == null) return false;
        return currentUser.hasPermission(action);
    }

    private void showPermissionDenied() {
        JOptionPane.showMessageDialog(null, 
            "Access Denied! You do not have permission to perform this action.", 
            "Permission Error", 
            JOptionPane.WARNING_MESSAGE);
    }

    // ---------------------- MANAGE USERS ----------------------
    private void manageUsersWindow() {
        JFrame userFrame = new JFrame("Manage Users");
        userFrame.setSize(500, 500);
        userFrame.setLocationRelativeTo(null);
        userFrame.setLayout(new GridLayout(10, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField roleField = new JTextField();

        JCheckBox addPerm = new JCheckBox("Add");
        JCheckBox updatePerm = new JCheckBox("Update");
        JCheckBox deletePerm = new JCheckBox("Delete");
        JCheckBox viewPerm = new JCheckBox("View");
        JCheckBox reportPerm = new JCheckBox("Generate Report");
        JCheckBox managePerm = new JCheckBox("Manage Users");

        JButton addUserBtn = new JButton("Create User");
        JButton viewUsersBtn = new JButton("View Users");

        userFrame.add(new JLabel("Username:")); userFrame.add(usernameField);
        userFrame.add(new JLabel("Password:")); userFrame.add(passwordField);
        userFrame.add(new JLabel("Role (Custom):")); userFrame.add(roleField);

        userFrame.add(addPerm); userFrame.add(updatePerm);
        userFrame.add(deletePerm); userFrame.add(viewPerm);
        userFrame.add(reportPerm); userFrame.add(managePerm);
        userFrame.add(new JLabel("")); userFrame.add(addUserBtn);
        userFrame.add(new JLabel("")); userFrame.add(viewUsersBtn);

        addUserBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(userFrame, "Please fill all fields!");
                return;
            }

            ArrayList<String> perms = new ArrayList<>();
            if (addPerm.isSelected()) perms.add("add");
            if (updatePerm.isSelected()) perms.add("update");
            if (deletePerm.isSelected()) perms.add("delete");
            if (viewPerm.isSelected()) perms.add("view");
            if (reportPerm.isSelected()) perms.add("report");
            if (managePerm.isSelected()) perms.add("manage_users");

            userList.add(new User(username, password, role, perms.toArray(new String[0])));
            saveUsersToFile();

            JOptionPane.showMessageDialog(userFrame, "User created successfully!");
            usernameField.setText(""); passwordField.setText(""); roleField.setText("");
        });

        viewUsersBtn.addActionListener(e -> {
            JFrame tableFrame = new JFrame("All Users");
            tableFrame.setSize(500, 300);
            tableFrame.setLocationRelativeTo(null);

            String[] columns = {"Username", "Role", "Permissions"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            for (User user : userList) {
                model.addRow(new Object[]{user.getUsername(), user.getRole(), String.join(", ", user.getPermissions())});
            }

            JTable userTable = new JTable(model);
            JScrollPane scroll = new JScrollPane(userTable);
            tableFrame.add(scroll);
            tableFrame.setVisible(true);
        });

        userFrame.setVisible(true);
    }

    // ---------------------- SAVE / LOAD USERS ----------------------
    private void saveUsersToFile() {
        try (FileWriter writer = new FileWriter(USER_FILE)) {
            for (User user : userList) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole() + "," +
                             String.join(";", user.getPermissions()) + "\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving users: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) return;

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length >= 4) {
                    String[] perms = data[3].split(";");
                    userList.add(new User(data[0], data[1], data[2], perms));
                }
            }
            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading users: " + e.getMessage());
        }
    }

    // ---------------------- MEDICINE OPERATIONS ----------------------
    private void addMedicineWindow() {
        JFrame addFrame = new JFrame("Add Medicine");
        addFrame.setSize(400, 400);
        addFrame.setLayout(new GridLayout(7, 2, 10, 10));
        addFrame.setLocationRelativeTo(null);

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField batchField = new JTextField();
        JTextField expiryField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField priceField = new JTextField();
        JButton saveBtn = new JButton("Save");

        addFrame.add(new JLabel("ID:")); addFrame.add(idField);
        addFrame.add(new JLabel("Name:")); addFrame.add(nameField);
        addFrame.add(new JLabel("Batch Number:")); addFrame.add(batchField);
        addFrame.add(new JLabel("Expiry Date:")); addFrame.add(expiryField);
        addFrame.add(new JLabel("Quantity:")); addFrame.add(qtyField);
        addFrame.add(new JLabel("Unit Price:")); addFrame.add(priceField);
        addFrame.add(new JLabel("")); addFrame.add(saveBtn);

        saveBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String batch = batchField.getText();
                String expiry = expiryField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double price = Double.parseDouble(priceField.getText());

                Medicine med = new Medicine(id, name, batch, expiry, qty, price);
                inventory.addMedicine(med);
                JOptionPane.showMessageDialog(addFrame, "Medicine added successfully!");
                addFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addFrame, "Enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addFrame.setVisible(true);
    }

    private void viewStockWindow() {
        JFrame viewFrame = new JFrame("View Stock");
        viewFrame.setSize(600, 400);
        viewFrame.setLocationRelativeTo(null);

        String[] columns = {"ID", "Name", "Batch", "Expiry", "Quantity", "Unit Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Medicine med : inventory.getAllMedicines()) {
            tableModel.addRow(new Object[]{
                    med.getId(), med.getName(), med.getBatchNumber(),
                    med.getExpiryDate(), med.getQuantity(), med.getUnitPrice()
            });
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane);
        viewFrame.setVisible(true);
    }

    private void updateMedicineWindow() {
        try {
            String idInput = JOptionPane.showInputDialog("Enter Medicine ID to update:");
            int id = Integer.parseInt(idInput);
            String qtyInput = JOptionPane.showInputDialog("Enter new quantity:");
            int newQty = Integer.parseInt(qtyInput);
            inventory.updateMedicine(id, newQty);
            JOptionPane.showMessageDialog(null, "Medicine updated successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMedicine() {
        try {
            String idInput = JOptionPane.showInputDialog("Enter Medicine ID to delete:");
            int id = Integer.parseInt(idInput);
            inventory.deleteMedicine(id);
            JOptionPane.showMessageDialog(null, "Medicine deleted successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid ID number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------- MAIN ----------------------
    public static void main(String[] args) {
        PharmacyInventory inventory = new PharmacyInventory();
        new PharmacyGUI(inventory);
    }
}
