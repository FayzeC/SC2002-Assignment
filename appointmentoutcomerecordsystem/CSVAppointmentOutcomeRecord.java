package appointmentoutcomerecordsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVAppointmentOutcomeRecord {

    // Load all appointment outcome records from the CSV file
    public static List<AppointmentOutcomeRecord> loadApptOutcomeRecord(String filePath) throws IOException {
        List<AppointmentOutcomeRecord> apptOutcomeRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String appointmentID = fields[0].trim();
                String patientID = fields[1].trim();
                String doctorAssigned = fields[2].trim();
                String appointmentDate = fields[3].trim();
                String service = fields[4].trim();
                String medication = fields[5].trim();
                String quantity = fields[6].trim();
                String status = fields[7].trim();
                String consultationNotes = fields[8].trim();

                AppointmentOutcomeRecord appointmentOutcomeRecord = new AppointmentOutcomeRecord(appointmentID, patientID, doctorAssigned, appointmentDate, service, medication, quantity, status, consultationNotes);
                apptOutcomeRecords.add(appointmentOutcomeRecord);
            }
        }
        return apptOutcomeRecords;
    }

    // Get a specific field value for a given appointment date and header
    public String getOutcomeValue(String filePath, String appointmentDate, String header) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Read header row
            String[] headers = line.split(",");
            int headerIndex = -1;

            // Find index of the specified header
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(header)) {
                    headerIndex = i;
                    break;
                }
            }

            if (headerIndex == -1) {
                throw new IllegalArgumentException("Header \"" + header + "\" not found in the CSV file.");
            }

            // Search for appointment date in subsequent rows
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(appointmentDate)) {
                    return fields[headerIndex].trim(); // Return requested field value
                }
            }
        }
        throw new IllegalArgumentException("Appointment date \"" + appointmentDate + "\" not found in the CSV file.");
    }

    // Update a specific field value for a given appointment date and header
    public static void setAppointmentOutcome(String filePath, String appointmentId, String header, String newValue) throws IOException {
        List<String[]> lines = new ArrayList<>();
        int headerColumnIndex = -1;

        // Read the file to find the header index and add all rows to the list for editing
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerProcessed = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                // Identify the header column index for the specified header
                if (!headerProcessed) {
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].trim().equalsIgnoreCase(header)) {
                            headerColumnIndex = i;
                            break;
                        }
                    }
                    if (headerColumnIndex == -1) {
                        throw new IllegalArgumentException("Header \"" + header + "\" not found in the CSV file.");
                    }
                    headerProcessed = true;
                }

                // Add each row to the lines list for potential updates
                lines.add(fields);
            }
        }

        // Update the cell if the appointment ID is found in the first column
        boolean rowFound = false;
        for (String[] row : lines) {
            if (row[0].trim().equals(appointmentId)) {  // Assuming the appointment ID is in the first column
                row[headerColumnIndex] = newValue;  // Update the specified cell
                rowFound = true;
                break;
            }
        }

        if (!rowFound) {
            throw new IllegalArgumentException("Appointment ID \"" + appointmentId + "\" not found in the CSV file.");
        }

        // Write the updated data back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : lines) {
                pw.println(String.join(",", row));
            }
        }
    }

    // Add a new appointment outcome record to the CSV file
    public static void addAppointmentOutcomeRecord(String filePath, String appointmentId, String patientId, String doctorId, String date, String service, String medication, String quantity, String status, String consultationNotes)
            throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(appointmentId + "," + patientId + "," + doctorId + "," + date + "," + service + "," + medication + "," + quantity + "," + status + "," + consultationNotes);
            bw.newLine();
        }
    }

    // Remove an appointment outcome record by appointment date
    public void removeAppointmentOutcomeRecord(String filePath, String appointmentDate) throws IOException {
        File tempFile = new File("temp.csv");
        boolean removed = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line = br.readLine();
            bw.write(line); // Write header to temp file
            bw.newLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (!fields[0].trim().equalsIgnoreCase(appointmentDate)) {
                    bw.write(line);
                    bw.newLine();
                } else {
                    removed = true;
                }
            }
        }

        if (!removed) {
            throw new IllegalArgumentException("Appointment date \"" + appointmentDate + "\" not found in the CSV file.");
        }

        // Replace original file with updated temp file
        if (!tempFile.renameTo(new File(filePath))) {
            throw new IOException("Failed to replace the original CSV file.");
        }
    }
}
