package inventorysystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVInventory {

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

    public String getReplenishValue(String filePath, String medicineName, String header) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Read header row
            String[] headers = line.split(",");
            int headerIndex = -1;

            // Find index of the header
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(header)) {
                    headerIndex = i;
                    break;
                }
            }

            if (headerIndex == -1) {
                throw new IllegalArgumentException("Header \"" + header + "\" not found in the CSV file.");
            }

            // Search for medicine name in subsequent rows
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(medicineName)) {
                    return fields[headerIndex].trim(); // Return replenish value
                }
            }
        }
        throw new IllegalArgumentException("Medicine name \"" + medicineName + "\" not found in the CSV file.");
    }

    public void updateInventory(String filePath, String medicineName, String header, String newValue)
            throws IOException {
        List<String[]> lines = new ArrayList<>();
        int headerIndex = -1;

        // Read the file and identify the header and target row
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerProcessed = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                // Process the header to find the column index
                if (!headerProcessed) {
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].trim().equalsIgnoreCase(header)) {
                            headerIndex = i;
                            break;
                        }
                    }

                    if (headerIndex == -1) {
                        System.out.println("Header \"" + header + "\" not found in the CSV file.");
                        return; // Header not found, return without doing anything
                    }

                    headerProcessed = true;
                }

                // Add each row to our lines list for potential updates
                lines.add(fields);
            }
        }

        // Update the cell if the medicine name and header are found
        boolean rowFound = false;
        for (String[] row : lines) {
            if (row[0].trim().equalsIgnoreCase(medicineName)) { // Assuming medicine name is in the first column
                row[headerIndex] = newValue; // Update the target cell with the new value
                rowFound = true;
                break;
            }
        }

        if (!rowFound) {
            System.out.println("Medicine name \"" + medicineName + "\" not found in the CSV file.");
            return; // Medicine name not found, return without doing anything
        }

        // Write the updated data back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : lines) {
                pw.println(String.join(",", row));
            }
            System.out.println("Successfully updated \"" + header + "\" for medicine \"" + medicineName + "\".");
        }


    }

    public void addInventoryItem(String filePath, String medicineName, String stock, String lowStockLevel)
            throws IOException {
        boolean exists = false;

        // Check if the medicine already exists in the file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header row
            br.readLine();

            // Check each line for the medicine name
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(medicineName)) {
                    exists = true;
                    break;
                }
            }
        }

        // If the medicine already exists, return without adding it
        if (exists) {
            System.out.println("Medicine \"" + medicineName + "\" already exists in the inventory, adding failed.");
            return;
        }

        // If it doesn't exist, add the new inventory item
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(medicineName + "," + stock + "," + lowStockLevel + "," + "0");
            bw.newLine();
            System.out.println("Item added successfully!");
        }
    }


    public void removeInventoryItem(String filePath, String medicineName) throws IOException {
        List<String[]> lines = new ArrayList<>();
        boolean removed = false;

        // Read the file and store its content
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerProcessed = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                // Add each row to our lines list for potential updates
                if (!headerProcessed) {
                    lines.add(fields); // Add the header row
                    headerProcessed = true;
                } else if (!fields[0].trim().equalsIgnoreCase(medicineName)) {
                    lines.add(fields); // Add rows that don't match the medicineName
                } else {
                    removed = true; // Mark as removed if we find the medicineName
                }
            }
        }

        if (!removed) {
            // Print a meaningful message if the item is not found
            System.out.println("Medicine name \"" + medicineName + "\" not found. No item was removed.");
            return; // Medicine name not found, return without doing anything
        }

        // Write the updated data back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : lines) {
                pw.println(String.join(",", row));
            }
        }

        // Print a success message after removing the item
        System.out.println("Medicine \"" + medicineName + "\" has been successfully removed from the inventory.");
    }
}