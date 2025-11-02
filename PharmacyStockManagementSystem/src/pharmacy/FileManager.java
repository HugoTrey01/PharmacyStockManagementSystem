package pharmacy;
import java.io.*;
import java.util.*;

public class FileManager {
	
	 public static ArrayList<Medicine> loadFromFile(String filename) {
	        ArrayList<Medicine> list = new ArrayList<>();
	        try (Scanner sc = new Scanner(new File(filename))) {
	            while (sc.hasNextLine()) {
	                String[] parts = sc.nextLine().split(",");
	                int id = Integer.parseInt(parts[0]);
	                String name = parts[1];
	                int qty = Integer.parseInt(parts[2]);
	                double price = Double.parseDouble(parts[3]);
	                list.add(new Medicine(id, name, "N/A", "N/A", qty, price));
	            }
	        } catch (Exception e) {
	            System.out.println("No existing file found or error reading file.");
	        }
	        return list;
	    }
	}

