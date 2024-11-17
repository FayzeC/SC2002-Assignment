package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import java.io.IOException;
import java.util.List;

/**
 * The {@code AdminAORView} class implements the {@code viewOutcomeRecord} interface
 * and provides functionality for viewing appointment outcome records.
 *
 * <p>This class focuses on displaying records that meet specific status criteria,
 * such as appointments that need medication dispensed.
 */
public class AdminAORView implements viewOutcomeRecord {

    /**
     * Displays appointment outcome records with a specified status.
     *
     * <p>This method:
     * <ul>
     *   <li>Loads all appointment outcome records from the CSV file.</li>
     *   <li>Filters and displays records that match the given status (case-insensitive).</li>
     *   <li>Formats and prints the record details, including appointment ID, doctor assigned,
     *       date, service, medication, quantity, status, and consultation notes.</li>
     *   <li>Displays an appropriate message if no matching records are found.</li>
     * </ul>
     *
     * @param status the status of the records to filter and display (e.g., "Approved").
     */
    @Override
    public void viewAppointmentOutcomeRecord(String status) {
        try {
            // Load all appointment outcome records from CSV
            List<AppointmentOutcomeRecord> outcomeRecords = CSVAppointmentOutcomeRecord.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);

            // Display only records that match the specified status
            System.out.println("\n+======= Appointment Outcome Records that Need Medication Dispensed =======+\n");
            boolean recordFound = false;
            int counter = 0;

            for (AppointmentOutcomeRecord record : outcomeRecords) {
                if ("Approved".equalsIgnoreCase(record.getStatus())) {
                    counter++;
                    System.out.println("--------------- Appointment Outcome Record: " + counter + " ----------------");
                    System.out.println("Appointment ID: " + record.getAppointmentID());
                    System.out.println("Doctor Assigned: " + record.getDoctorAssigned());
                    System.out.println("Appointment Date: " + record.getAppointmentDate());
                    System.out.println("Service: " + record.getService());
                    System.out.println("Medication: " + record.getMedication());
                    System.out.println("Quantity: " + record.getQuantity());
                    System.out.println("Status: " + record.getStatus());
                    System.out.println("Consultation Notes: " + record.getConsultationNotes());
                    System.out.println("--------------------------------------------------------------\n");
                    recordFound = true;
                }
            }

            if (!recordFound) {
                System.out.println("No records found that need medication dispensed.");
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
