package filemanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The CSVUpdater class provides functionality to update specific values in a CSV file.
 * The updater method updates a cell in a CSV file based on matching values in specific columns
 * (e.g., IDs), and writes the updated content back to the file.
 */
public class CSVUpdater {

    /**
     * Updates a specific value in the CSV file based on matching ID values and a specified header.
     *
     * @param filePath the path to the CSV file to be updated
     * @param id1 the value to match in the first ID column (idColumnIndex1)
     * @param id2 the value to match in the second ID column (idColumnIndex2). Can be null to only match id1.
     * @param header the column header where the value needs to be updated
     * @param newValue the new value to set in the specified header column
     * @param idColumnIndex1 the index of the first ID column in the CSV
     * @param idColumnIndex2 the index of the second ID column in the CSV
     * @throws IOException if an error occurs while reading or writing the file
     * @throws IllegalArgumentException if the header is not found or the specified record is not found
     */
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
}
