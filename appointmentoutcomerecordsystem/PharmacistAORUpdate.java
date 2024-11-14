package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class PharmacistAORUpdate implements updateOutcomeRecord {

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

    // Update status from "pending" to "approved"
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
                System.out.println("Error: " +appointmentID + " not found in the records.");
                return;
            }

            // Check if the current status is "pending" before updating to "approved"
            if (!targetRecord.getStatus().equalsIgnoreCase("pending")) {
                System.out.println(appointmentID + " has already been approved.");
            } else {
                // Update the status to "dispensed"
                updateAppointmentOutcomeRecord(appointmentID, "Status", "approved");
            }
        } catch (IOException e) {
            System.out.println("Error loading appointment outcome records: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}