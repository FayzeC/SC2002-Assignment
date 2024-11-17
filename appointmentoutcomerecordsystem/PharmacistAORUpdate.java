package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code PharmacistAORUpdate} class provides functionality for pharmacists to update
 * appointment outcome records, such as changing the status of an appointment from "PENDING"
 * to "APPROVED" or other statuses.
 *
 * This class implements the {@code updateOutcomeRecord} interface and provides two key functionalities:
 * <ul>
 *     <li>General-purpose updates to specific fields of an appointment outcome record in the CSV file.</li>
 *     <li>Specialized approval workflow for appointments with a "PENDING" status.</li>
 * </ul>
 *
 * @see AppointmentOutcomeRecord
 * @see CSVAppointmentOutcomeRecord
 * @see filemanager.FilePaths
 */
public class PharmacistAORUpdate implements updateOutcomeRecord {

    /**
     * Updates a specific field of an appointment outcome record in the CSV file.
     *
     * <p>
     * The method locates the record based on the given {@code appointmentID} and updates the specified
     * {@code header} field with the provided {@code newValue}.
     * </p>
     *
     * @param appointmentID the unique identifier of the appointment to update
     * @param header the field name to update (e.g., "Status")
     * @param newValue the new value to set for the specified field
     * @throws IOException if an error occurs during file I/O operations
     * @see CSVAppointmentOutcomeRecord#setAppointmentOutcome
     */
    @Override
    public void updateAppointmentOutcomeRecord(String appointmentID, String header, String newValue) throws IOException {
        try {
            CSVAppointmentOutcomeRecord.setAppointmentOutcome(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH, appointmentID, header, newValue);
            System.out.println(appointmentID + " status updated from PENDING to " + newValue);
        } catch (IOException e) {
            System.out.println("Error updating appointment outcome record: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the status of an appointment outcome record from "PENDING" to "APPROVED."
     *
     * <p>
     * This method prompts the pharmacist to enter an {@code appointmentID} and checks the status
     * of the corresponding record. If the status is "PENDING," it is updated to "APPROVED."
     * </p>
     *
     * The method performs the following steps:
     * <ul>
     *     <li>Loads all appointment outcome records from the CSV file.</li>
     *     <li>Searches for the record with the given {@code appointmentID}.</li>
     *     <li>Checks if the status is already "APPROVED" or "DISPENSED" and notifies the pharmacist.</li>
     *     <li>Updates the status to "APPROVED" if it is currently "PENDING."</li>
     * </ul>
     *
     * @see CSVAppointmentOutcomeRecord#loadApptOutcomeRecord
     */
    public void approveAppointmentOutcomeRecord() {
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
                System.out.println(appointmentID + " not found in the records.");
                return;
            }

            // Check if the current status is "PENDING" before updating to "APPROVED"
            if (targetRecord.getStatus().equalsIgnoreCase("APPROVED")) {
                System.out.println(appointmentID + " has already been APPROVED.");
            } else if (targetRecord.getStatus().equalsIgnoreCase("DISPENSED")) {
                System.out.println(appointmentID + " has already been DISPENSED.");
            } else {
                // Update the status to "APPROVED"
                updateAppointmentOutcomeRecord(appointmentID, "Status", "APPROVED");
            }
        } catch (IOException e) {
            System.out.println("Error loading appointment outcome records: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
