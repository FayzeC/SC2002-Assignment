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
        File tempFile = new File("temp.csv");
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

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

            bw.write(line); // Write header to temp file
            bw.newLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(medicineName)) {
                    fields[headerIndex] = newValue; // Update the field with new value
                    updated = true;
                }
                bw.write(String.join(",", fields));
                bw.newLine();
            }
        }

        if (!updated) {
            throw new IllegalArgumentException("Medicine name \"" + medicineName + "\" not found in the CSV file.");
        }

        // Replace original file with updated temp file
        if (!tempFile.renameTo(new File(filePath))) {
            throw new IOException("Failed to replace the original CSV file.");
        }
    }

    public void addInventoryItem(String filePath, String medicineName, String stock, String lowStockLevel)
            throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(medicineName + "," + stock + "," + lowStockLevel);
            bw.newLine();
        }
    }

    public void removeInventoryItem(String filePath, String medicineName) throws IOException {
        File tempFile = new File("temp.csv");
        boolean removed = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line = br.readLine();
            bw.write(line); // Write header
            bw.newLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (!fields[0].trim().equalsIgnoreCase(medicineName)) {
                    bw.write(line);
                    bw.newLine();
                } else {
                    removed = true;
                }
            }
        }

        if (!removed) {
            throw new IllegalArgumentException("Medicine name \"" + medicineName + "\" not found in the CSV file.");
        }

        // Replace original file with updated temp file
        if (!tempFile.renameTo(new File(filePath))) {
            throw new IOException("Failed to replace the original CSV file.");
        }
    }
}
