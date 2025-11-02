package pharmacy;

import java.util.*;
import java.io.*;

public class PharmacyInventory {
	 private ArrayList<Medicine> medicines = new ArrayList<>();

	    // Add new medicine
	    public void addMedicine(Medicine m) {
	        medicines.add(m);
	        System.out.println("Medicine added successfully!");
	    }

	    // View all medicines
	    public void viewStock() {
	        if (medicines.isEmpty()) {
	            System.out.println("No medicines in stock.");
	        } else {
	            double grandTotal = 0;
	            System.out.println("\n--- Current Stock ---");
	            for (Medicine m : medicines) {
	                System.out.println(m);
	                grandTotal += m.getTotalValue();
	            }
	            System.out.println("-------------------------------");
	            System.out.println("Total Stock Value: GHS " + String.format("%.2f", grandTotal));
	        }
	    }

	    // Update quantity
	    public void updateMedicine(int id, int newQty) {
	        for (Medicine m : medicines) {
	            if (m.getId() == id) {
	                m.setQuantity(newQty);
	                System.out.println("Quantity updated successfully!");
	                return;
	            }
	        }
	        System.out.println("Medicine not found!");
	    }

	    // Delete medicine
	    public void deleteMedicine(int id) {
	        medicines.removeIf(m -> m.getId() == id);
	        System.out.println("Medicine deleted successfully!");
	    }

	    // Generate low-stock report
	    public void generateReport() {
	        System.out.println("Low Stock Medicines (less than 5):");
	        for (Medicine m : medicines) {
	            if (m.getQuantity() < 5) {
	                System.out.println(m);
	            }
	        }
	    }

	    // Save data to file
	    public void saveToFile(String filename) {
	        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
	            for (Medicine m : medicines) {
	                pw.println(m.getId() + "," + m.getName() + "," + m.getQuantity() + "," + m.getPrice());
	            }
	            System.out.println("Data saved successfully!");
	        } catch (IOException e) {
	            System.out.println("Error saving file: " + e.getMessage());
	        }
	    }
	}

