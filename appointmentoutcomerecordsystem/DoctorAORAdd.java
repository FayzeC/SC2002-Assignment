package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import filemanager.CSVUpdater;
import inventorysystem.CSVInventory;
import inventorysystem.Inventory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;


public class DoctorAORAdd implements addOutcomeRecord {

    private String patientID;
    private String appointmentDate;
    private StringBuilder medication = new StringBuilder();
    private StringBuilder quantity = new StringBuilder();
    private String consultationNotes;
    private String apptStatus;

    private void findAppointmentDetails(String appointmentID) {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);

        // Check if file exists
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;
            boolean appointmentFound = false;

            // Read each line in the CSV
            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 4 && fields[0].trim().equals(appointmentID)) { // Appointment ID is in the 1st column
                    this.patientID = fields[3].trim();     // Patient ID in 4th column
                    this.appointmentDate = fields[1].trim(); // Date in 2nd column
                    this.apptStatus = fields[7].trim();
                    appointmentFound = true;
                    System.out.println("Appointment found:");
                    System.out.println("Patient ID: " + this.patientID);
                    System.out.println("Appointment Date: " + this.appointmentDate);
                    break;
                }
            }

            if (!appointmentFound) {
                System.out.println("No appointment found with ID: " + appointmentID);
            }
        } catch (IOException e) {
            System.err.println("Error reading appointment details: " + e.getMessage());
        }
    }

    private void additionalDetails() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Load the list of available medicines from the inventory CSV
        CSVInventory inventoryManager = new CSVInventory();
        List<Inventory> inventoryList = inventoryManager.loadInventory(FilePaths.INVENTORY_LIST_PATH);

        /// Initialize medication and quantity strings
        Set<String> selectedMedicines = new HashSet<>();
        String input = "";

        // Allow the user to select multiple medications
        while (true) {
            // Display available medicines with a "Done" option
            System.out.println("Available medicines:");
            for (int i = 0; i < inventoryList.size(); i++) {
                System.out.println((i + 1) + ". " + inventoryList.get(i).getMedicineName());
            }
            System.out.println((inventoryList.size() + 1) + ". Done");

            System.out.print("Select a medicine by number: ");
            input = scanner.nextLine().trim().toLowerCase();

            // Check if the user selected the "Done" option
            if (input.equals(String.valueOf(inventoryList.size() + 1)) || input.equals("done")) {
                // If no medications were added, set defaults
                if (medication.length() == 0) {
                    medication.append("none");
                    quantity.append("0");
                }
                break;
            }

            try {
                int medicineChoice = Integer.parseInt(input) - 1;

                // Validate the user's choice
                if (medicineChoice < 0 || medicineChoice >= inventoryList.size()) {
                    throw new IllegalArgumentException("Invalid choice. Please select a valid number from the list.");
                }

                String selectedMedicine = inventoryList.get(medicineChoice).getMedicineName();

                // Check if the medicine was already selected
                if (selectedMedicines.contains(selectedMedicine)) {
                    System.out.println("This medicine has already been selected. Please choose a different medicine.");
                    continue; // Prompt the user to choose another medicine
                } else { selectedMedicines.add(selectedMedicine);}

                // Prompt for quantity of the selected medicine
                System.out.print("Enter quantity for " + selectedMedicine + ": ");
                String qty = scanner.nextLine().trim();

                // Validate that the quantity is a positive integer
                if (!qty.matches("\\d+") || Integer.parseInt(qty) <= 0) {
                    System.out.println("Invalid quantity. Please enter a positive integer.");
                    continue; // Restart the loop to select a valid quantity
                }

                // Add selected medicine and quantity to respective strings
                if (medication.length() > 0) {
                    medication.append("/"); // Add separator if it's not the first medicine
                    quantity.append("/");
                }
                medication.append(selectedMedicine);
                quantity.append(qty);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        System.out.print("Enter Consultation Notes: ");
        consultationNotes = scanner.nextLine().trim();

        // Validate inputs (basic validation)
        if (medication.isEmpty() || quantity.isEmpty() || consultationNotes.isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled. Please try again.");
        }
    }

    private void changeAppointmentStatus(String appointmentID) throws IOException {
        try {
            // Call CSVDataUpdater's updateCSVCell to change the status to "COMPLETED"
            CSVUpdater.updateCSVCell(
                    FilePaths.APPOINTMENT_LIST_PATH,  // Path to the appointment list CSV file
                    "Appointment ID",                  // Column to search by
                    appointmentID,                     // Value to match in Appointment ID column
                    "Status",                          // Column to update
                    "COMPLETED"                        // New status value
            );
            System.out.println("Appointment Status has been successfully updated from CONFIRMED to COMPLETED for " + appointmentID );
        } catch (IOException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }

    }

    public void addAppointmentOutcomeRecord(String doctorID) throws IOException {
        Scanner scanner = new Scanner(System.in);
        try{
            System.out.print("Enter Appointment ID: ");
            String appointmentID = scanner.nextLine().trim();
            findAppointmentDetails(appointmentID);
            if (!apptStatus.equals("CONFIRMED")) {
                System.out.println("Appointment is not CONFIRMED. Please try again with a CONFIRMED appointment.");
                return;
            }
            additionalDetails();
            CSVAppointmentOutcomeRecord.addAppointmentOutcomeRecord(
                    FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH,
                    appointmentID,
                    patientID,
                    doctorID,
                    appointmentDate,
                    "test",
                    medication.toString(),
                    quantity.toString(),
                    "pending",              // initial status
                    consultationNotes
            );
            System.out.println("New appointment outcome record added for Appointment ID " + appointmentID);
            changeAppointmentStatus(appointmentID);
        } catch (IOException e) {
            System.out.println("Error adding appointment outcome record: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}