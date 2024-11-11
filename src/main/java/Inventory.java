public class Inventory {
    private String medicineName;
    private int initialStock;
    private int lowStockAlert;

    public Inventory(String medicineName, int initialStock, int lowStockAlert) {
        this.medicineName = medicineName;
        this.initialStock = initialStock;
        this.lowStockAlert = lowStockAlert;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getInitialStock() {
        return initialStock;
    }

    public int getLowStockAlert() {
        return lowStockAlert;
    }

    public void print() { // Remove after final submission this is just to check if data is loaded
                          // correctly
        System.out.println("Medicine Name: " + medicineName);
        System.out.println("Initial Stock: " + initialStock);
        System.out.println("Low Stock Alert: " + lowStockAlert);
    }
}
