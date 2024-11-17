package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code AdminAORUpdate} class implements the {@code updateOutcomeRecord} interface
 * to provide functionality for updating appointment outcome records.
 *
 * <p>This class includes methods for:
 * <ul>
 *   <li>Updating the status of an appointment outcome record based on a specified header and value.</li>
 *   <li>Changing the status of an appointment from "APPROVED" to "DISPENSED".</li>
 * </ul>
 */
public class AdminAORUpdate implements updateOutcomeRecord {

    /**
     * Updates an appointment outcome record by modifying a specified field.
     *
     * @param appointmentID the unique identifier of the appointment to update.
     * @param header the column header representing the field to update (e.g., "Status").
     * @param newValue the new value to assign to the specified field.
     * @throws IOException if an error occurs during file operations or data processing.
     */
    @Override
    public void updateAppointmentOutcomeRecord(String appointmentID, String header, String newValue) throws IOException {
        try {
            // Call setAppointmentOutcome to update the status in the CSV file
            CSVAppointmentOutcomeRecord.setAppointmentOutcome(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH, appointmentID, header, newValue);
            System.out.println("Appointment ID " + appointmentID + " status updated to " + newValue);
        } catch (IOException e) {
            System.out.println("Error updating appointment outcome record: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the status of an appointment outcome record to "DISPENSED"
     * if the current status is "APPROVED".
     *
     * <p>This method performs the following steps:
     * <ul>
     *   <li>Prompts the user to enter the appointment ID to update.</li>
     *   <li>Loads the list of appointment outcome records to locate the specified record.</li>
     *   <li>Validates the current status of the appointment (e.g., checks for "APPROVED").</li>
     *   <li>Updates the status to "DISPENSED" if the conditions are met.</li>
     * </ul>
     *
     * @throws IOException if an error occurs while reading or writing the appointment outcome records.
     */
    public void dispenseAppointmentOutcomeRecord() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the Appointment ID to dispense: ");
            String appointmentID = scanner.nextLine().trim();

            // Load appointment outcome records to find the record with the specified appointment ID
            List<AppointmentOutcomeRecord> records = CSVAppointmentOutcomeRecord.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);
            AppointmentOutcomeRecord targetRecord = null;

            for (AppointmentOutcomeRecord record : records) {
                if (record.getAppointmentID().equals(appointmentID)) {
                    targetRecord = record;
                    break;
                }
            }

            if (targetRecord == null) {
                System.out.println("Error: " + appointmentID + " not found in the records.");
                return;
            }

            // Check if the current status is "APPROVED" before updating to "DISPENSED"
            if (targetRecord.getStatus().equalsIgnoreCase("PENDING")) {
                System.out.println(appointmentID + " has yet to be APPROVED.");
            } else if (targetRecord.getStatus().equalsIgnoreCase("DISPENSED")) {
                System.out.println(appointmentID + " has already been DISPENSED.");
            } else {
                // Update the status to "DISPENSED"
                updateAppointmentOutcomeRecord(appointmentID, "Status", "DISPENSED");
            }

        } catch (IOException e) {
            System.out.println("Error loading appointment outcome records: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
