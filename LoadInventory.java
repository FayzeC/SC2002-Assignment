import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadInventory extends DataLoader{
    private List<Inventory> inventoryList = new ArrayList<>();

    @Override
    public void importFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header row if there is one
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Split line into fields assuming comma-separated values
                String[] fields = line.split(",");

                if (fields.length < 3) {
                    System.err.println("Skipping invalid line: " + line);
                    continue; // Skip this line if it doesn't have enough fields
                }

                // Assign each field to the appropriate variable
                String medicineName = fields[0].trim();
                int initialStock = Integer.parseInt(fields[1].trim());
                int lowStockAlert = Integer.parseInt(fields[2].trim());

                // Create a new Inventory object and add it to the list
                Inventory inventory = new Inventory(medicineName, initialStock, lowStockAlert);
                inventoryList.add(inventory);
            }

            System.out.println("Inventory data loaded successfully.");

        } catch (IOException e) {
            System.err.println("Error loading patient data: " + e.getMessage());
        }
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }
}
