package inventorysystem;

import filemanager.FilePaths;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;

/**
 * The PharmaInventory class extends {@link InventoryManagement} and provides
 * additional functionality specific to managing pharmaceutical inventory.
 * This includes the ability to send replenish requests for low-stock items.
 */
public class PharmaInventory extends InventoryManagement {

    /**
     * Constructs a PharmaInventory object by initializing the inventory management system.
     */
    public PharmaInventory() {
        super();
    }

    /**
     * Sends a replenish request for a specific item in the inventory if its stock is below the low stock alert threshold.
     * The replenish request value is incremented by 1 in the inventory record.
     *
     * Steps:
     * <ul>
     *     <li>Prompts the user to enter the name of the item to update.</li>
     *     <li>Checks if the item exists in the inventory list.</li>
     *     <li>If the stock is below the low stock alert, increments the "Replenish Request" value in the inventory file.</li>
     *     <li>Handles errors such as file read/write issues.</li>
     * </ul>
     */
    public void sendReplenishRequest() {
        Scanner sc = new Scanner(System.in);

        // Prompt the user to enter the item name
        System.out.print("\nEnter Name of Item to be updated: ");
        String name = sc.nextLine();
        List<Inventory> inventoryList = getInventoryList();
        boolean itemFound = false;

        // Iterate through the inventory list to find the specified item
        for (Inventory inventory : inventoryList) {
            if (inventory.getMedicineName().equalsIgnoreCase(name)) { // Case-insensitive comparison
                itemFound = true;

                // Check if the item's stock is below the low stock alert level
                if (inventory.getInitialStock() < inventory.getLowStockAlert()) {
                    try {
                        // Retrieve the current replenish request value
                        String currentReplenishValue = inventoryUpdater.getReplenishValue(FilePaths.INVENTORY_LIST_PATH, name, "Replenish Request");
                        int updatedReplenishValue = Integer.parseInt(currentReplenishValue) + 1; // Increment the value by 1

                        // Update the replenish request value in the inventory file
                        inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Replenish Request", String.valueOf(updatedReplenishValue));

                        System.out.println("Replenish request sent successfully.");
                    } catch (IOException e) {
                        System.err.println("Failed to send replenish request: " + e.getMessage());
                    }
                } else {
                    System.out.println("Stock is not below low stock alert!");
                }
                break; // Exit the loop after processing the item
            }
        }

        // If the item was not found in the inventory
        if (!itemFound) {
            System.out.println("Item not found in inventory.");
        }
    }
}
