package filemanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUpdater {
    public static void updater(String filePath, String id1, String id2, String header, String newValue, int idColumnIndex1, int idColumnIndex2) throws IOException {
        List<String[]> lines = new ArrayList<>();
        int headerColumnIndex = -1;

        // Read the file and identify the header and target row
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerProcessed = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                // Process the header to find the column index
                if (!headerProcessed) {
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].equalsIgnoreCase(header)) {
                            headerColumnIndex = i;
                            break;
                        }
                    }

                    if (headerColumnIndex == -1) {
                        throw new IllegalArgumentException("Header \"" + header + "\" not found in the CSV file.");
                    }

                    headerProcessed = true;
                }

                // Add each row to our lines list for potential updates
                lines.add(fields);
            }
        }

        // Update the cell if the id and header are found
        boolean rowFound = false;
        for (String[] row : lines) {
            boolean id1Matches = row[idColumnIndex1].equals(id1);
            boolean id2Matches = (id2 == null) ? !row[idColumnIndex2].isEmpty() : row[idColumnIndex2].equals(id2);

            if (id1Matches && id2Matches) {  // Match either both IDs or only id1 with a non-empty idColumnIndex2
                row[headerColumnIndex] = newValue;  // Update the target cell with the new value
                rowFound = true;
                break;
            }
        }

        if (!rowFound) {
            throw new IllegalArgumentException("Record with ID1 \"" + id1 + "\" and ID2 \"" + id2 + "\" not found in the CSV file.");
        }

        // Write the updated data back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : lines) {
                pw.println(String.join(",", row));
            }
        }
    }

    // New function to update a specific cell in the CSV file
    public static void updateCSVCell(String filePath, String keyColumn, String keyValue, String targetColumn, String newValue) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Error: File does not exist at " + filePath);
            return;
        }

        // Temporary storage for file contents
        List<String[]> fileContents = new ArrayList<>();
        String[] headers;
        boolean isUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            headers = br.readLine().split(",");  // Read header row
            int keyColumnIndex = -1;
            int targetColumnIndex = -1;

            // Identify the indices of the key column and target column
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(keyColumn)) {
                    keyColumnIndex = i;
                }
                if (headers[i].trim().equalsIgnoreCase(targetColumn)) {
                    targetColumnIndex = i;
                }
            }

            if (keyColumnIndex == -1 || targetColumnIndex == -1) {
                System.err.println("Error: Column names not found in the header.");
                return;
            }

            // Read each row, updating the target cell if the key matches
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > Math.max(keyColumnIndex, targetColumnIndex) &&
                        fields[keyColumnIndex].trim().equals(keyValue)) {
                    fields[targetColumnIndex] = newValue;  // Update the cell
                    isUpdated = true;
                }
                fileContents.add(fields);
            }
        }

        // If no update was made, output a message and exit
        if (!isUpdated) {
            System.out.println("Key value not found: No updates made.");
            return;
        }

        // Write the updated contents back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(String.join(",", headers));
            bw.newLine();
            for (String[] row : fileContents) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
    }
}
