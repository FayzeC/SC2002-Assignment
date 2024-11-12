package inventorymanagement.example;

public interface adminInventoryManagement {
    void addInventoryItem();
    void removeInventoryItem();
    void updateInventoryItem();
    void updateLowStockAlert();
    void processReplenishRequest();
}
