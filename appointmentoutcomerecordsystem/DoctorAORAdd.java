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

/**
 * The {@code DoctorAORAdd} class is responsible for adding new appointment outcome records to the system.
 * It interacts with appointment records, inventory records, and updates statuses in the system.
 *
 * This class implements the {@code addOutcomeRecord} interface to provide functionality
 * for managing appointment outcomes, including:
 * <ul>
 *     <li>Finding appointment details based on an appointment ID.</li>
 *     <li>Gathering additional details such as medication, quantity, service, and consultation notes.</li>
 *     <li>Updating the status of the appointment to "COMPLETED".</li>
 *     <li>Adding a new appointment outcome record to the CSV file.</li>
 * </ul>
 *
 * @see filemanager.FilePaths
 * @see filemanager.CSVUpdater
 * @see inventorysystem.CSVInventory
 * @see inventorysystem.Inventory
 */
public class DoctorAORAdd implements addOutcomeRecord {

    private String patientID;
    private String appointmentDate;
    private StringBuilder medication = new StringBuilder();
    private StringBuilder quantity = new StringBuilder();
    private String service;
    private String consultationNotes;
    private String apptStatus = "";

    /**
     * Finds and verifies the details of an appointment based on the given appointment ID and doctor ID.
     *
     * @param appointmentID the unique identifier for the appointment
     * @param doctorID      the unique identifier for the doctor assigned to the appointment
     * @throws IOException              if there is an error reading the appointment file
     * @throws IllegalArgumentException if the appointment is not found, does not belong to the doctor,
     *                                  or the appointment file does not exist
     */
    private void findAppointmentDetails(String appointmentID, String doctorID) throws IOException {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);

        // Check if file exists
        if (!file.exists()) {
            throw new IllegalArgumentException (FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
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
                if (fields.length >= 4 && fields[0].trim().equals(appointmentID)) {
                    if (!fields[5].trim().equals(doctorID)) {
                        throw new IllegalArgumentException (appointmentID + " does not belong to " + doctorID + ".\n");
                    }
                    else {
                        this.patientID = fields[3].trim();     // Patient ID in 4th column
                        this.appointmentDate = fields[1].trim(); // Date in 2nd column
                        this.apptStatus = fields[7].trim();
                        appointmentFound = true;
                        System.out.println("+========= Appointment found =========+");
                        System.out.println("Patient ID: " + this.patientID);
                        System.out.println("Appointment Date: " + this.appointmentDate);
                        break;
                    }
                }
            }

            if (!appointmentFound) {
                throw new IllegalArgumentException (appointmentID + " not found in the records.\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading appointment details: " + e.getMessage());
        }
    }

    /**
     * Collects additional details such as service type, medications, quantities, and consultation notes
     * for the appointment outcome record. Also validates the user input.
     *
     * @throws IOException if there is an error loading inventory details
     */
    private void additionalDetails() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Load the list of available medicines from the inventory CSV
        CSVInventory inventoryManager = new CSVInventory();
        List<Inventory> inventoryList = inventoryManager.loadInventory(FilePaths.INVENTORY_LIST_PATH);

        // Initialize medication and quantity strings
        Set<String> selectedMedicines = new HashSet<>();
        String input = "";

        while (true) {
            System.out.println("\nSelect a Type of Service:");
            System.out.println("1. Consultation");
            System.out.println("2. Dental");
            System.out.println("3. X-Ray");
            System.out.println("4. Surgery");
            System.out.println("5. Physiotherapy");
            System.out.println("6. Therapy");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    service = "Consultation";
                    break;
                case "2":
                    service = "Dental";
                    break;
                case "3":
                    service = "X-Ray";
                    break;
                case "4":
                    service = "Surgery";
                    break;
                case "5":
                    service = "Physiotherapy";
                    break;
                case "6":
                    service = "Therapy";
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option");
                    continue; // Prompt the user again if input is invalid
            }
            break; // Exit the loop if a valid option is selected
        }

        // Allow the user to select multiple medications
        while (true) {
            // Display available medicines with a "Done" option
            System.out.println("\nAvailable medicines:");
            for (int i = 0; i < inventoryList.size(); i++) {
                System.out.println((i + 1) + ". " + inventoryList.get(i).getMedicineName());
            }
            System.out.println((inventoryList.size() + 1) + ". Exit");

            System.out.print("Select a medicine by number: ");
            input = scanner.nextLine().trim().toLowerCase();

            // Check if the user selected the "Exit" option
            if (input.equals(String.valueOf(inventoryList.size() + 1))) {
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
                    throw new IllegalArgumentException("Please select a valid number from the list.");
                }

                String selectedMedicine = inventoryList.get(medicineChoice).getMedicineName();

                // Check if the medicine was already selected
                if (selectedMedicines.contains(selectedMedicine)) {
                    System.out.println("This medicine has already been selected. Please choose a different medicine.\n");
                    continue; // Prompt the user to choose another medicine
                }

                // Prompt for quantity of the selected medicine
                System.out.print("\nEnter quantity for " + selectedMedicine + ": ");
                String qty = scanner.nextLine().trim();

                // Validate that the quantity is a integer, positive, and non-zero
                if (!qty.matches("\\d+")) {
                    throw new IllegalArgumentException("Please enter a positive integer.");
                } else if (Integer.parseInt(qty) < 0) {
                    throw new IllegalArgumentException("Quantity cannot be negative.");
                } else if (Integer.parseInt(qty) == 0) {
                    throw new IllegalArgumentException("Quantity of this medicine cannot be 0.");
                }

                // Add selected medicine and quantity to respective strings
                if (medication.length() > 0) {
                    medication.append("/"); // Add separator if it's not the first medicine
                    quantity.append("/");
                }
                selectedMedicines.add(selectedMedicine);
                medication.append(selectedMedicine);
                quantity.append(qty);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
        System.out.print("\nEnter Consultation Notes: ");
        consultationNotes = scanner.nextLine().trim();

        // If consultationNotes is empty, set it to "NIL"
        if (consultationNotes.isEmpty()) {
            consultationNotes = "NIL";
        }
    }

    /**
     * Changes the status of the specified appointment to "COMPLETED" in the appointment list CSV file.
     *
     * @param appointmentID the unique identifier for the appointment
     * @throws IOException if there is an error updating the appointment file
     */
    private void changeAppointmentStatus(String appointmentID) throws IOException {
        try {
            // Call CSVDataUpdater to change the status to "COMPLETED"
            CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH,
                    appointmentID,
                    null,
                    "Status",
                    "COMPLETED",
                    0,
                    0
            );
            System.out.println(appointmentID + " status has been successfully updated from CONFIRMED to COMPLETED" );
        } catch (IOException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }

    }

    /**
     * Adds a new appointment outcome record to the system, including:
     * <ul>
     *     <li>Validating the appointment details.</li>
     *     <li>Collecting additional details required for the outcome record.</li>
     *     <li>Updating the appointment status to "COMPLETED".</li>
     * </ul>
     *
     * @param doctorID the unique identifier for the doctor adding the outcome record
     * @throws IOException              if there is an error interacting with the CSV files
     * @throws IllegalArgumentException if invalid inputs are provided or required details are missing
     */
    public void addAppointmentOutcomeRecord(String doctorID) throws IOException {
        Scanner scanner = new Scanner(System.in);
        try{
            System.out.print("Enter Appointment ID: ");
            String appointmentID = scanner.nextLine().trim();

            try {
                findAppointmentDetails(appointmentID, doctorID);

                // Check appointment status only if findAppointmentDetails succeeds
                if (!apptStatus.equals("CONFIRMED")) {
                    System.out.println("Appointment is not CONFIRMED. Please try again with a CONFIRMED appointment.");
                    return;
                }
            } catch (Exception e) {
                // Handle any exception thrown by findAppointmentDetails
                System.out.println("Error finding appointment details: " + e.getMessage());
                return; // Exit if finding appointment details fails
            }
            additionalDetails();
            CSVAppointmentOutcomeRecord.addAppointmentOutcomeRecord(
                    FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH,
                    appointmentID,
                    patientID,
                    doctorID,
                    appointmentDate,
                    service,
                    medication.toString(),
                    quantity.toString(),
                    "PENDING",              // initial status
                    consultationNotes
            );
            System.out.println("New appointment outcome record added for " + appointmentID);
            changeAppointmentStatus(appointmentID);
        } catch (IOException e) {
            System.out.println("Error adding appointment outcome record: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}