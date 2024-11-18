package inventorysystem;

import filemanager.FilePaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

/**
 * The AdminInventory class extends {@link InventoryManagement} and provides
 * additional administrative functions for managing inventory. These include
 * adding, removing, updating inventory items, and processing stock replenishment requests.
 */
public class AdminInventory extends InventoryManagement {

    private Scanner sc = new Scanner(System.in);

    /**
     * Constructs an AdminInventory object by initializing the inventory management system.
     */
    public AdminInventory() {
        super(); // Call the constructor of InventoryManagement
    }

    /**
     * Adds a new item to the inventory. Prompts the user to input the name, initial stock,
     * and low stock alert level for the new item and updates the inventory file.
     */
    public void addInventoryItem() {
        System.out.print("\nEnter Name of Item to be added: ");
        String name = sc.nextLine();

        String stock;
        while (true) {
            System.out.print("\nEnter Number of Items to be added (integer only): ");
            stock = sc.nextLine();
            try {
                Integer.parseInt(stock); // Validate if input is an integer
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        String lowStockLevel;
        while (true) {
            System.out.print("\nEnter low stock level of Item to be added (integer only): ");
            lowStockLevel = sc.nextLine();
            try {
                Integer.parseInt(lowStockLevel); // Validate if input is an integer
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        try {
            inventoryUpdater.addInventoryItem(FilePaths.INVENTORY_LIST_PATH, name, stock, lowStockLevel);
        } catch (IOException e) {
            System.err.println("Failed to add item: " + e.getMessage());
        }
    }

    /**
     * Removes an item from the inventory. Prompts the user to input the name of the item
     * to be removed and updates the inventory file.
     */
    public void removeInventoryItem() {
        System.out.print("\nEnter Name of Item to be removed: ");
        String name = sc.nextLine();

        try {
            inventoryUpdater.removeInventoryItem(FilePaths.INVENTORY_LIST_PATH, name);
        } catch (IOException e) {
            System.err.println("Failed to remove item: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the stock level of an existing inventory item. Prompts the user to input the
     * name of the item and the updated stock level and reflects the changes in the inventory file.
     */
    public void updateInventoryItem() {
        System.out.print("\nEnter Name of Item to be updated: ");
        String name = sc.nextLine();

        String stock;
        while (true) {
            System.out.print("\nEnter updated stock (integer only): ");
            stock = sc.nextLine();
            try {
                Integer.parseInt(stock); // Validate if input is an integer
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        try {
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Initial Stock", stock);
        } catch (IOException e) {
            System.err.println("Failed to update stock: " + e.getMessage());
        }
    }

    /**
     * Updates the low stock alert level for an existing inventory item. Prompts the user
     * to input the name of the item and the new alert level and updates the inventory file.
     */
    public void updateLowStockAlert() {
        System.out.print("\nEnter Name of Item to be updated: ");
        String name = sc.nextLine();

        String lowStockLevel;
        while (true) {
            System.out.print("\nEnter new low stock level of Item (integer only): ");
            lowStockLevel = sc.nextLine();
            try {
                Integer.parseInt(lowStockLevel); // Validate if input is an integer
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        try {
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Low Stock Level Alert", lowStockLevel);
        } catch (IOException e) {
            System.err.println("Failed to update low stock level: " + e.getMessage());
        }
    }

    /**
     * Processes stock replenishment requests for items that have pending replenish requests.
     * Updates the stock level for the selected item and resets the replenish request count.
     */
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

        System.out.print("Enter the number of the item you want to fulfill: ");
        int choice = sc.nextInt();

        if (choice < 1 || choice > inventoryList.size()) {
            System.out.println("Invalid selection. No replenishment processed.");
            return;
        }

        Inventory selectedInventory = inventoryList.get(choice - 1);
        int newStockLevel = selectedInventory.getLowStockAlert() + 10;

        try {
            inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, selectedInventory.getMedicineName(),
                    "Initial Stock", String.valueOf(newStockLevel));
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
