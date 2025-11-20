package pharmacy;

public class Medicine {
    private int id;
    private String name;
    private String batchNumber;
    private String expiryDate;
    private int quantity;
    private double unitPrice;

    // ✅ Constructor
    public Medicine(int id, String name, String batchNumber, String expiryDate, int quantity, double unitPrice) {
        this.id = id;
        this.name = name;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // ✅ Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBatchNumber() { return batchNumber; }
    public String getExpiryDate() { return expiryDate; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }

    // ✅ Setters (useful for updates)
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    // ✅ Total value (for reporting)
    public double getTotalValue() {
        return quantity * unitPrice;
    }

    // ✅ ToString (for console or file output)
    @Override
    public String toString() {
        return id + " | " + name + " | " + batchNumber + " | " + expiryDate +
               " | Qty: " + quantity + " | Unit Price: GHS " + unitPrice;
    }
}
