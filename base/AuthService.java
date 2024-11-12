package base;

import filemanager.CSVDataLoader;
import filemanager.FilePaths;
import inventorysystem.Inventory;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class AuthService {
    private List<Patient> patientList;
    private List<Doctor> doctorList = new ArrayList<>();
    private List<Pharmacist> pharmacistList = new ArrayList<>();
    private List<Administrator> administratorList = new ArrayList<>();
    private List<AppointmentOutcomeRecord> apptOutcomeRecordList = new ArrayList<>();
    private List<Appointment> appointmentList = new ArrayList<>();
    private List<Inventory> inventoryList = new ArrayList<>();

    public AuthService() {
        loadData(); // Load data initially
    }

    // Method to load data from Excel
    private void loadData() {
        try {
            // Clear the existing lists
            if(patientList != null ) { patientList.clear(); }
            if(doctorList != null ) { doctorList.clear(); }
            if(pharmacistList != null ) { pharmacistList.clear(); }
            if(administratorList != null ) { administratorList.clear(); }
            if(apptOutcomeRecordList != null ) { apptOutcomeRecordList.clear(); }
            if(appointmentList != null ) { appointmentList.clear(); }
            if(inventoryList != null ) { inventoryList.clear(); }

            patientList = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);
            CSVDataLoader.loadStaff(FilePaths.STAFF_LIST_PATH, pharmacistList, doctorList, administratorList);
            apptOutcomeRecordList = CSVDataLoader.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);
            appointmentList = CSVDataLoader.loadAppointments(FilePaths.APPOINTMENT_LIST_PATH);
            inventoryList = CSVDataLoader.loadInventory(FilePaths.INVENTORY_LIST_PATH);
        } catch (IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }

    public User login() throws IOException {
        // Reload data every time a login attempt is made
        loadData();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Hospital ID: ");
        String hospitalID = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = null;

        // Determine the list to search based on the prefix of the ID
        if (hospitalID.startsWith("D")) {
            user = getUserById(hospitalID, doctorList);
        } else if (hospitalID.startsWith("A")) {
            user = getUserById(hospitalID, administratorList);
        } else if (hospitalID.startsWith("P")) {
            // Check both patient and pharmacist lists if ID starts with 'P'
            user = getUserById(hospitalID, patientList);
            if (user == null) {  // If not found in patient list, check pharmacist list
                user = getUserById(hospitalID, pharmacistList);
            }
        }

        // Authenticate if user is found
        if (user != null && user.authenticate(password)) {
            String choice;
            System.out.println("Login successful. Welcome " + user + "!");

            if (user.getFirstLogin()) { // If this is the first login
                String filename = "";
                System.out.println("This is your first login. We recommend you change your password for security purposes.");

                // Prompt user to change password
                do {
                    System.out.print("Do you want to change your password? (Y/N): ");
                    choice = sc.nextLine();

                    if(user.getRole().equals("base.Patient")) {
                        filename = FilePaths.PATIENT_LIST_PATH;
                    }else{
                        filename = FilePaths.STAFF_LIST_PATH;
                    }

                    if (choice.equalsIgnoreCase("Y")) {
                        user.changePassword(filename);
                        user.setFirstLogin(filename);
                        break;
                    } else if (choice.equalsIgnoreCase("N")) {
                        user.setFirstLogin(filename);
                        break;
                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                } while (true);
            }
            return user;
        } else {
            System.out.println("Invalid credentials.\n");
            return null;
        }
    }

    // Helper method to get user by ID from a specific list of Users
    private User getUserById(String id, List<? extends User> userList) {
        for (User user : userList) {
            if (user.getHospitalID().equals(id)) {
                return user;
            }
        }
        return null; // Return null if no user is found
    }
}
