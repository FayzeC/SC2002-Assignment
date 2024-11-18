package inventorysystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The CSVInventory class provides methods for managing inventory stored in a CSV file.
 * It includes functionality to load, update, add, and remove inventory items as well as
 * retrieve specific data such as replenish request values.
 */
public class CSVInventory {

    /**
     * Loads the inventory from the specified CSV file and returns it as a list of Inventory objects.
     *
     * @param filePath the path to the CSV file.
     * @return a list of {@link Inventory} objects.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public static List<Inventory> loadInventory(String filePath) throws IOException {
        List<Inventory> inventoryList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false; // Skip header row
                    continue;
                }
                String[] fields = line.split(",");
                String medicineName = fields[0].trim();
                int initialStock = Integer.parseInt(fields[1].trim());
                int lowStockAlert = Integer.parseInt(fields[2].trim());

                Inventory inventory = new Inventory(medicineName, initialStock, lowStockAlert);
                inventoryList.add(inventory);
            }
        }
        return inventoryList;
    }

    /**
     * Retrieves the replenish value for a specified medicine name and header column from the CSV file.
     *
     * @param filePath     the path to the CSV file.
     * @param medicineName the name of the medicine to search for.
     * @param header       the column header to retrieve the value from.
     * @return the value from the specified column for the given medicine.
     * @throws IOException              if an I/O error occurs while reading the file.
     * @throws IllegalArgumentException if the medicine name or header is not found in the CSV file.
     */
    public String getReplenishValue(String filePath, String medicineName, String header) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Read header row
            String[] headers = line.split(",");
            int headerIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(header)) {
                    headerIndex = i;
                    break;
                }
            }

            if (headerIndex == -1) {
                throw new IllegalArgumentException("Header \"" + header + "\" not found in the CSV file.");
            }

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(medicineName)) {
                    return fields[headerIndex].trim();
                }
            }
        }
        throw new IllegalArgumentException("Medicine name \"" + medicineName + "\" not found in the CSV file.");
    }

    /**
     * Updates a specific cell in the CSV file based on the medicine name and column header.
     *
     * @param filePath     the path to the CSV file.
     * @param medicineName the name of the medicine to update.
     * @param header       the column header to update.
     * @param newValue     the new value to set in the specified cell.
     * @throws IOException if an I/O error occurs while reading or writing the file.
     */
    public static void updateInventory(String filePath, String medicineName, String header, String newValue)
            throws IOException {
        List<String[]> lines = new ArrayList<>();
        int headerIndex = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerProcessed = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (!headerProcessed) {
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].trim().equalsIgnoreCase(header)) {
                            headerIndex = i;
                            break;
                        }
                    }

                    if (headerIndex == -1) {
                        System.out.println("Header \"" + header + "\" not found in the CSV file.");
                        return;
                    }

                    headerProcessed = true;
                }

                lines.add(fields);
            }
        }

        boolean rowFound = false;
        for (String[] row : lines) {
            if (row[0].trim().equalsIgnoreCase(medicineName)) {
                row[headerIndex] = newValue;
                rowFound = true;
                break;
            }
        }

        if (!rowFound) {
            System.out.println("Medicine name \"" + medicineName + "\" not found in the CSV file.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : lines) {
                pw.println(String.join(",", row));
            }
            System.out.println("Successfully updated \"" + header + "\" for medicine \"" + medicineName + "\".");
        }
    }

    /**
     * Adds a new inventory item to the CSV file. Validates that the item does not already exist.
     *
     * @param filePath       the path to the CSV file.
     * @param medicineName   the name of the medicine to add.
     * @param stock          the initial stock level.
     * @param lowStockLevel  the low stock alert level.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    public void addInventoryItem(String filePath, String medicineName, String stock, String lowStockLevel)
            throws IOException {
        boolean exists = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(medicineName)) {
                    exists = true;
                    break;
                }
            }
        }

        if (exists) {
            System.out.println("Medicine \"" + medicineName + "\" already exists in the inventory, adding failed.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(medicineName + "," + stock + "," + lowStockLevel + "," + "0");
            bw.newLine();
            System.out.println("Item added successfully!");
        }
    }

    /**
     * Removes an inventory item from the CSV file based on the medicine name.
     *
     * @param filePath     the path to the CSV file.
     * @param medicineName the name of the medicine to remove.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    public void removeInventoryItem(String filePath, String medicineName) throws IOException {
        List<String[]> lines = new ArrayList<>();
        boolean removed = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerProcessed = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (!headerProcessed) {
                    lines.add(fields);
                    headerProcessed = true;
                } else if (!fields[0].trim().equalsIgnoreCase(medicineName)) {
                    lines.add(fields);
                } else {
                    removed = true;
                }
            }
        }

        if (!removed) {
            System.out.println("Medicine name \"" + medicineName + "\" not found. No item was removed.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : lines) {
                pw.println(String.join(",", row));
            }
        }

        System.out.println("Medicine \"" + medicineName + "\" has been successfully removed from the inventory.");
    }
}
