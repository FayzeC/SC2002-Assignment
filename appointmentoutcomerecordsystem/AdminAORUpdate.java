package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import inventorysystem.CSVInventory;
import inventorysystem.Inventory;
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
    public void dispenseMedicationAndUpdateInventory() {
        Scanner scanner = new Scanner(System.in);

        try {
            // Prompt for appointment ID
            System.out.print("Enter the Appointment ID: ");
            String appointmentID = scanner.nextLine().trim();

            // Load appointment outcome records
            List<AppointmentOutcomeRecord> records = CSVAppointmentOutcomeRecord.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);
            AppointmentOutcomeRecord targetRecord = null;

            for (AppointmentOutcomeRecord record : records) {
                if (record.getAppointmentID().equals(appointmentID)) {
                    if(record.getStatus().equalsIgnoreCase("APPROVED")) {
                        targetRecord = record;
                        break;
                    } else if (record.getStatus().equalsIgnoreCase("DISPENSED")) {
                        throw new IllegalArgumentException("Medication has already been dispensed for Appointment ID " + appointmentID);
                    } else if (record.getStatus().equalsIgnoreCase("PENDING")) {
                        throw new IllegalArgumentException("Medication has not been approved for Appointment ID " + appointmentID);
                    }
                }
            }

            if (targetRecord == null) {
                throw new IllegalArgumentException ("Error: Appointment ID not found.");
            }

            // Display medication and quantity
            String medication = targetRecord.getMedication();
            String quantity = targetRecord.getQuantity();

            if ("none".equalsIgnoreCase(medication)) {
                System.out.println("No medication found for Appointment ID " + appointmentID);
                updateAppointmentOutcomeRecord(appointmentID, "Status", "DISPENSED");
                return;
            }

            System.out.println("Medication: " + medication);
            System.out.println("Quantity: " + quantity);

            // Confirm with the user
            System.out.print("Do you want to dispense this medication? Type 'yes' to confirm: ");
            String confirmation = scanner.nextLine().trim();

            if (!"yes".equalsIgnoreCase(confirmation)) {
                System.out.println("Operation cancelled.");
                return;
            }

            // Load inventory and update stock
            List<Inventory> inventoryList = CSVInventory.loadInventory(FilePaths.INVENTORY_LIST_PATH);
            String[] medications = medication.split("/");
            String[] quantities = quantity.split("/");

            for (int i = 0; i < medications.length; i++) {
                String med = medications[i].trim();
                int qty = Integer.parseInt(quantities[i].trim());
                boolean medicineFound = false;

                for (Inventory item : inventoryList) {
                    if (item.getMedicineName().equalsIgnoreCase(med)) {
                        if (item.getInitialStock() < qty) {
                            throw new IllegalArgumentException ("Insufficient stock for " + med);
                        }

                        // Update inventory
                        int newStock = item.getInitialStock() - qty;
                        CSVInventory.updateInventory(FilePaths.INVENTORY_LIST_PATH, med, "Initial Stock", String.valueOf(newStock));
                        System.out.println("Dispensed " + qty + " units of " + med + ". Updated stock: " + newStock);
                        medicineFound = true;
                        break;
                    }
                }

                if (!medicineFound) {
                    throw new IllegalArgumentException ("Medicine " + med + " not found in the inventory.");
                }
            }

            // Update the appointment outcome record status to DISPENSED
            updateAppointmentOutcomeRecord(appointmentID, "Status", "DISPENSED");

        } catch (IOException e) {
            System.out.println("Error processing request: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error in medication quantity format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
