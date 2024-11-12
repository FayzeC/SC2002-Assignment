package inventorymanagement.example;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import login.example.*;

public class PharmacyInventory extends InventoryManagement {

    public PharmacyInventory() {
        super(); // Call the parameterized constructor of InventoryManagement
    }

    public void submitReplenishRequest() {
        sendReplenishRequest(); // Call the private method
    }

    private void sendReplenishRequest() {
        CSVInventory inventoryUpdater = new CSVInventory();
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Name of Item to be updated: ");
        String name = sc.nextLine();
        List<Inventory> inventoryList = getInventoryList();
        boolean itemFound = false;

        for (Inventory inventory : inventoryList) { // Corrected for loop syntax
            if (inventory.getMedicineName().equalsIgnoreCase(name)) { // Use .equalsIgnoreCase for String comparison
                itemFound = true;
                if (inventory.getInitialStock() < inventory.getLowStockAlert()) { // Check if stock is below alert level
                    try {
                        inventoryUpdater.updateInventory(FilePaths.INVENTORY_LIST_PATH, name, "Replenish Request", "1");
                        System.out.println("Replenish request sent successfully.");
                    } catch (IOException e) {
                        System.err.println("Failed to send replenish request: " + e.getMessage());
                    }
                } else {
                    System.out.println("Stock is not below low stock alert!");
                }
                break;
            }
        }

        if (!itemFound) {
            System.out.println("Item not found in inventory.");
        }
    }
}