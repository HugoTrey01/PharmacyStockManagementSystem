package pharmacy;

public class Medicine {


	    private int id;
	    private String name;
	    private String batchNumber;
	    private String expiryDate;
	    private int quantity;
	    private double price;

	    public Medicine(int id, String name, String batchNumber, String expiryDate, int quantity, double price) {
	        this.id = id;
	        this.name = name;
	        this.batchNumber = batchNumber;
	        this.expiryDate = expiryDate;
	        this.quantity = quantity;
	        this.price = price;
	    }

	    public int getId() { return id; }
	    public String getName() { return name; }
	    public int getQuantity() { return quantity; }
	    public double getPrice() { return price; }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }
	        public double getTotalValue() {
	            return quantity * price;
	        }
   
	    

	    
	    public String toString() {
	        double totalValue = getTotalValue();
	        return id + " | " + name + " | " + batchNumber + " | " + expiryDate +
	               " | Qty: " + quantity + " | Unit Price: GHS " + price +
	               " | Total Value: GHS " + String.format("%.2f", totalValue);
	    }

	}


