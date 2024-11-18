package inventorysystem;

/**
 * The Inventory class represents a record for managing the stock of medicines.
 * It includes details like the medicine name, initial stock, and a low stock alert threshold.
 */
public class Inventory {
    private String medicineName;
    private int initialStock;
    private int lowStockAlert;

    /**
     * Constructs an Inventory object with the specified details.
     *
     * @param medicineName   the name of the medicine
     * @param initialStock   the initial stock level of the medicine
     * @param lowStockAlert  the stock level at which a low stock alert is triggered
     */
    public Inventory(String medicineName, int initialStock, int lowStockAlert) {
        this.medicineName = medicineName;
        this.initialStock = initialStock;
        this.lowStockAlert = lowStockAlert;
    }

    /**
     * Gets the name of the medicine.
     *
     * @return the medicine name
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Gets the initial stock level of the medicine.
     *
     * @return the initial stock level
     */
    public int getInitialStock() {
        return initialStock;
    }

    /**
     * Gets the low stock alert threshold for the medicine.
     *
     * @return the low stock alert threshold
     */
    public int getLowStockAlert() {
        return lowStockAlert;
    }

    /**
     * Prints the inventory details in a formatted output.
     * This includes the medicine name, initial stock, and low stock alert threshold.
     */
    public void print() {
        System.out.printf("%-15s %-15s %-15s\n", medicineName, initialStock, lowStockAlert);
    }
}
