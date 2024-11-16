package appointmentoutcomerecordsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing appointment outcome records stored in a CSV file.
 */
public class CSVAppointmentOutcomeRecord {

    /**
     * Loads all appointment outcome records from the specified CSV file.
     *
     * @param filePath the path to the CSV file.
     * @return a list of {@link AppointmentOutcomeRecord} objects.
     * @throws IOException if an I/O error occurs while reading the file.
     */
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

    /**
     * Updates a specific field value for a given appointment ID and header.
     *
     * @param filePath   the path to the CSV file.
     * @param appointmentId the appointment ID to search for.
     * @param header     the column header to update.
     * @param newValue   the new value for the field.
     * @throws IOException if an I/O error occurs while accessing the file.
     * @throws IllegalArgumentException if the header or appointment ID is not found.
     */
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

    /**
     * Adds a new appointment outcome record to the CSV file.
     *
     * @param filePath           the path to the CSV file.
     * @param appointmentId      the appointment ID.
     * @param patientId          the patient ID.
     * @param doctorId           the doctor ID.
     * @param date               the appointment date.
     * @param service            the service provided.
     * @param medication         the medication prescribed.
     * @param quantity           the quantity of medication.
     * @param status             the status of the appointment outcome.
     * @param consultationNotes  the consultation notes.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    public static void addAppointmentOutcomeRecord(String filePath, String appointmentId, String patientId, String doctorId, String date, String service, String medication, String quantity, String status, String consultationNotes)
            throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(appointmentId + "," + patientId + "," + doctorId + "," + date + "," + service + "," + medication + "," + quantity + "," + status + "," + consultationNotes);
            bw.newLine();
        }
    }
}