package inventorysystem;

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

    public void print() {
        System.out.printf("%-15s %-15s %-15s\n", medicineName, initialStock, lowStockAlert);
    }
}
