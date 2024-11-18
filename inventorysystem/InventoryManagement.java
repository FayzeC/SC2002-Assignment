package inventorysystem;

import filemanager.FilePaths;

import java.io.IOException;
import java.util.List;

/**
 * The InventoryManagement class is responsible for managing a list of inventory items.
 * It provides functionality to load, view, and access inventory data from a CSV file.
 */
public class InventoryManagement {
    private List<Inventory> inventoryList;
    CSVInventory inventoryUpdater = new CSVInventory();

    /**
     * Constructs an InventoryManagement object and initializes the inventory list
     * by loading it from the CSV file specified in {@link FilePaths#INVENTORY_LIST_PATH}.
     */
    public InventoryManagement() {
        try {
            this.inventoryList = CSVInventory.loadInventory(FilePaths.INVENTORY_LIST_PATH); // Load inventory list from CSV
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    /**
     * Displays the inventory list in a formatted table.
     * Reloads the inventory data from the CSV file to ensure the latest information is displayed.
     */
    public void viewInventory() {
        try {
            // Reload the inventory list from the CSV file
            this.inventoryList = CSVInventory.loadInventory(FilePaths.INVENTORY_LIST_PATH);
        } catch (IOException e) {
            System.err.println("Failed to load inventory: " + e.getMessage());
            return; // Exit if loading fails
        }

        // Display the inventory items
        System.out.println("\n+======= Inventory List =======+");
        System.out.printf("%-15s %-15s %-15s\n", "Medicine Name", "Initial Stock", "Low Stock Alert");
        for (Inventory item : inventoryList) {
            item.print(); // Assuming Inventory has a print method for viewing details
        }
    }

    /**
     * Returns the current inventory list.
     *
     * @return a list of {@link Inventory} items.
     */
    public List<Inventory> getInventoryList() {
        return inventoryList;
    }
}
