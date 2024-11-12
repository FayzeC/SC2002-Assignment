package login.example;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUpdater {
    public static void updater(String filePath, String id, String header, String newValue) throws IOException {
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
            if (row[0].equals(id)) {  // Assuming id is in the first column
                row[headerColumnIndex] = newValue;  // Update the target cell with the new value
                rowFound = true;
                break;
            }
        }

        if (!rowFound) {
            throw new IllegalArgumentException("ID \"" + id + "\" not found in the CSV file.");
        }

        // Write the updated data back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : lines) {
                pw.println(String.join(",", row));
            }
        }
    }
}
