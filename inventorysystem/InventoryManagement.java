package inventorysystem;

import filemanager.FilePaths;

import java.io.IOException;
import java.util.ArrayList;
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
        for (Inventory item : inventoryList) {
            item.print(); // Assuming inventorysystem.Inventory has a print method for viewing details
        }
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

}