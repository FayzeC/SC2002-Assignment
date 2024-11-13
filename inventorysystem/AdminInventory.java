package inventorysystem;

import filemanager.FilePaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminInventory extends InventoryManagement {
    CSVInventory inventoryUpdater = new CSVInventory();
    Scanner sc = new Scanner(System.in);

    public AdminInventory() {
        super(); // Call the parameterized constructor of InventoryManagement
    }

    // Method to add a new item to the inventory
    public void addInventoryItem() {
        System.out.print("\nEnter Name of Item to be added: ");
        String name = sc.nextLine();
        System.out.print("\nEnter Number of Item to be added: ");
        String stock = sc.nextLine();
        System.out.print("\nEnter low stock level of Item to be added: ");
        String lowStockLevel = sc.nextLine();

        try {
            // Call the addInventoryItem method to add a new row
            inventoryUpdater.addInventoryItem(FilePaths.INVENTORY_LIST_PATH, name, stock, lowStockLevel);
            System.out.println("Item added successfully!");
        } catch (IOException e) {
            System.err.println("Failed to add item: " + e.getMessage());
        }
    }

    // Method to remove an item from the inventory
    public void removeInventoryItem() {
        System.out.print("\nEnter Name of Item to be removed: ");
        String name = sc.nextLine();

        try {
            // Call the removeInventoryItem method to delete the row
            inventoryUpdater.removeInventoryItem(FilePaths.INVENTORY_LIST_PATH, name);
            System.out.println("Item removed successfully!");
        } catch (IOException e) {
            System.err.println("Failed to remove item: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to update the stock level of an existing item
    public void updateInventoryItem() {
        System.out.print("\nEnter Name of Item to be updated: ");
        String name = sc.nextLine();
        System.out.print("\nEnter updated stock: ");
        String stock = sc.nextLine();

        try {
            // Update the stock column for the specified item
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Stock", stock);
            System.out.println("Stock updated successfully!");
        } catch (IOException e) {
            System.err.println("Failed to update stock: " + e.getMessage());
        }
    }

    // Method to update the low stock alert level for an existing item
    public void updateLowStockAlert() {
        System.out.print("\nEnter Name of Item to be updated: ");
        String name = sc.nextLine();
        System.out.print("\nEnter new low stock level of Item: ");
        String lowStockLevel = sc.nextLine();

        try {
            // Update the low stock alert level for the specified item
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Low Stock Level", lowStockLevel);
            System.out.println("Low stock level updated successfully!");
        } catch (IOException e) {
            System.err.println("Failed to update low stock level: " + e.getMessage());
        }
    }

    // Method to process stock replenishment
    public void processReplenishRequest() {
        List<Inventory> itemsWithRequests = new ArrayList<>();

        // Gather all items with pending replenish requests
        for (Inventory inventory : getInventoryList()) {
            try {
                String replenishRequestCount = inventoryUpdater.getReplenishValue(FilePaths.INVENTORY_LIST_PATH,
                        inventory.getMedicineName(), "Replenish Request");

                if (replenishRequestCount != null && Integer.parseInt(replenishRequestCount) > 0) {
                    itemsWithRequests.add(inventory);
                }
            } catch (IOException e) {
                System.err.println("Failed to retrieve replenish request count for " + inventory.getMedicineName()
                        + ": " + e.getMessage());
            }
        }

        // If no items have replenish requests, inform the user and return
        if (itemsWithRequests.isEmpty()) {
            System.out.println("No items have pending replenish requests.");
            return;
        }

        // Display items with pending replenish requests
        System.out.println("Items with pending replenish requests:");
        for (int i = 0; i < itemsWithRequests.size(); i++) {
            Inventory inventory = itemsWithRequests.get(i);
            System.out.println((i + 1) + ". " + inventory.getMedicineName() + " - Current Stock: "
                    + inventory.getInitialStock() + ", Low Stock Alert: " + inventory.getLowStockAlert());
        }

        // Prompt user to select an item to fulfill
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of the item you want to fulfill: ");
        int choice = sc.nextInt();

        // Validate the user's choice
        if (choice < 1 || choice > itemsWithRequests.size()) {
            System.out.println("Invalid selection. No replenishment processed.");
            return;
        }

        // Process the selected item for replenishment
        Inventory selectedInventory = itemsWithRequests.get(choice - 1);
        int newStockLevel = selectedInventory.getLowStockAlert() + 10; // Set stock to 10 units above the low stock
        // alert level

        try {
            // Update stock level in the Excel file
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, selectedInventory.getMedicineName(),
                    "Initial Stock", String.valueOf(newStockLevel));
            // Reset the replenish request count to 0 after fulfilling
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, selectedInventory.getMedicineName(),
                    "Replenish Request", "0");
            System.out.println("Replenishment processed for " + selectedInventory.getMedicineName()
                    + ". New stock level: " + newStockLevel);
        } catch (IOException e) {
            System.err.println(
                    "Failed to update inventory for " + selectedInventory.getMedicineName() + ": " + e.getMessage());
        }
    }
}