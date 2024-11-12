package inventorysystem;

import base.Inventory;
import filemanager.FilePaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManagement {
    private List<Inventory> inventoryList;

    public InventoryManagement() {
        this.inventoryList = loadInventoryList(); // Load inventory list from Excel
    }

    // Load the inventory list from Excel file
    private List<Inventory> loadInventoryList() {
        CSVInventory excelInv = new CSVInventory();
        List<Inventory> loadedInventory = new ArrayList<>();
        try {
            loadedInventory = excelInv.loadInventory(FilePaths.INVENTORY_LIST_PATH); // Replace with the correct path
        } catch (IOException e) {
            System.err.println("Failed to load inventory from Csv: " + e.getMessage());
        }
        return loadedInventory;
    }

    public void viewInventory() {
        for (Inventory item : inventoryList) {
            item.print(); // Assuming base.Inventory has a print method for viewing details
        }
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

}