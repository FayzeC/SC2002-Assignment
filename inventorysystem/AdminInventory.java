package inventorysystem;

import filemanager.FilePaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map; // Add this line



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

        String stock;
        while (true) {
            System.out.print("\nEnter Number of Items to be added (integer only): ");
            stock = sc.nextLine();

            // Validate if the input is an integer
            try {
                Integer.parseInt(stock);
                break; // Exit loop if the input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        String lowStockLevel;
        while (true) {
            System.out.print("\nEnter low stock level of Item to be added (integer only): ");
            lowStockLevel = sc.nextLine();

            // Validate if the input is an integer
            try {
                Integer.parseInt(lowStockLevel);
                break; // Exit loop if the input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        try {
            // Call the addInventoryItem method to add a new row
            inventoryUpdater.addInventoryItem(FilePaths.INVENTORY_LIST_PATH, name, stock, lowStockLevel);
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

        String stock;
        while (true) {
            System.out.print("\nEnter updated stock (integer only): ");
            stock = sc.nextLine();

            // Validate if the input is an integer
            try {
                Integer.parseInt(stock);
                break; // Exit loop if the input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        try {
            // Update the stock column for the specified item
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Initial Stock", stock);
        } catch (IOException e) {
            System.err.println("Failed to update stock: " + e.getMessage());
        }
    }


    // Method to update the low stock alert level for an existing item
    public void updateLowStockAlert() {
        System.out.print("\nEnter Name of Item to be updated: ");
        String name = sc.nextLine();

        String lowStockLevel;
        while (true) {
            System.out.print("\nEnter new low stock level of Item (integer only): ");
            lowStockLevel = sc.nextLine();

            // Validate if the input is an integer
            try {
                Integer.parseInt(lowStockLevel);
                break; // Exit loop if the input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        try {
            // Update the low stock alert level for the specified item
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Low Stock Level Alert", lowStockLevel);
        } catch (IOException e) {
            System.err.println("Failed to update low stock level: " + e.getMessage());
        }
    }


    // Method to process stock replenishment
    public void processReplenishRequest() {
        Map<Inventory, Integer> itemsWithRequests = new HashMap<>();

        // Gather all items with pending replenish requests
        for (Inventory inventory : getInventoryList()) {
            try {
                String replenishRequestCount = inventoryUpdater.getReplenishValue(FilePaths.INVENTORY_LIST_PATH,
                        inventory.getMedicineName(), "Replenish Request");

                if (replenishRequestCount != null && Integer.parseInt(replenishRequestCount) > 0) {
                    itemsWithRequests.put(inventory, Integer.parseInt(replenishRequestCount));
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
        List<Inventory> inventoryList = new ArrayList<>(itemsWithRequests.keySet());
        for (int i = 0; i < inventoryList.size(); i++) {
            Inventory inventory = inventoryList.get(i);
            int replenishCount = itemsWithRequests.get(inventory);
            System.out.println((i + 1) + ". " + inventory.getMedicineName() + " - Current Stock: "
                    + inventory.getInitialStock() + ", Low Stock Alert: " + inventory.getLowStockAlert()
                    + ", Amount of requests: " + replenishCount);
        }

        // Prompt user to select an item to fulfill
        System.out.print("Enter the number of the item you want to fulfill: ");
        int choice = sc.nextInt();

        // Validate the user's choice
        if (choice < 1 || choice > inventoryList.size()) {
            System.out.println("Invalid selection. No replenishment processed.");
            return;
        }

        // Process the selected item for replenishment
        Inventory selectedInventory = inventoryList.get(choice - 1);
        int newStockLevel = selectedInventory.getLowStockAlert() + 10; // Set stock to 10 units above the low stock alert level

        try {
            // Update stock level in the file
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