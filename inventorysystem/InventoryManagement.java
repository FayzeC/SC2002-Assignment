package inventorysystem;

import filemanager.FilePaths;

import java.io.IOException;
import java.util.List;

public class InventoryManagement {
    private List<Inventory> inventoryList;

    public InventoryManagement() {
        try {
            this.inventoryList = CSVInventory.loadInventory(FilePaths.INVENTORY_LIST_PATH); // Load inventory list from CSV
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

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

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

}