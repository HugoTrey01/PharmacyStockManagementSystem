package pharmacy;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PharmacyInventory {
    private ArrayList<Medicine> medicines;

    public PharmacyInventory() {
        medicines = new ArrayList<>();
    }

    // ✅ Add new medicine
    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }

    // ✅ Get all medicines (for JTable in GUI)
    public ArrayList<Medicine> getAllMedicines() {
        return medicines;
    }

    // ✅ Search for a medicine by ID
    public Medicine searchMedicine(int id) {
        for (Medicine med : medicines) {
            if (med.getId() == id) {
                return med;
            }
        }
        return null;
    }

    // ✅ Update medicine quantity
    public void updateMedicine(int id, int newQuantity) {
        Medicine med = searchMedicine(id);
        if (med != null) {
            med.setQuantity(newQuantity);
        } else {
            JOptionPane.showMessageDialog(null, "Medicine not found!");
        }
    }

    // ✅ Delete medicine
    public void deleteMedicine(int id) {
        Medicine med = searchMedicine(id);
        if (med != null) {
            medicines.remove(med);
        } else {
            JOptionPane.showMessageDialog(null, "Medicine not found!");
        }
    }

    // ✅ Generate report of low stock items
    public void generateReport() {
        StringBuilder report = new StringBuilder("Low Stock Medicines:\n\n");
        boolean hasLowStock = false;

        for (Medicine med : medicines) {
            if (med.getQuantity() < 5) { // Example: less than 5 is low
                hasLowStock = true;
                report.append(med.getName())
                      .append(" - Qty: ")
                      .append(med.getQuantity())
                      .append("\n");
            }
        }

        if (!hasLowStock) {
            JOptionPane.showMessageDialog(null, "No low-stock medicines!");
        } else {
            JOptionPane.showMessageDialog(null, report.toString());
        }
    }

//✅ Save data to file
public void saveToFile(String filename) {
 try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename))) {
     for (Medicine med : medicines) {
         writer.println(
             med.getId() + "," +
             med.getName() + "," +
             med.getBatchNumber() + "," +
             med.getExpiryDate() + "," +
             med.getQuantity() + "," +
             med.getUnitPrice()
         );
     }
     javax.swing.JOptionPane.showMessageDialog(null, "Data saved successfully!");
 } catch (Exception e) {
     javax.swing.JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
 }
}

//✅ Print all medicines in console view
public void viewStock() {
 System.out.println("\n---- Current Stock ----");
 for (Medicine med : medicines) {
     System.out.println(med.toString());
 }
 System.out.println("-----------------------");
}
}

