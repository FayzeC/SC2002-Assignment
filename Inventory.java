public class Inventory {
    private String medicineName;
    private int initialStock;
    private int lowStockAlert;

    public Inventory(String medicineName, int initialStock, int lowStockAlert) {
        this.medicineName = medicineName;
        this.initialStock = initialStock;
        this.lowStockAlert = lowStockAlert;
    }

    public String toString() { // Remove this when submitting final version
        return "Inventory{" +
                "Medicine Name='" + medicineName + '\'' +
                ", Initial Stock='" + initialStock + '\'' +
                ", Low Stock Alert=" + lowStockAlert +
                '}';
    }
}
