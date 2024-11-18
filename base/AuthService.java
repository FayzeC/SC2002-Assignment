package base;

import filemanager.CSVDataLoader;
import filemanager.FilePaths;
import roles.*;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The AuthService class handles user authentication and data loading.
 * It provides methods to load user data from CSV files and manage login attempts.
 * Users are authenticated based on their hospital ID and password.
 */
public class AuthService {
    private List<Patient> patientList;
    private List<Doctor> doctorList = new ArrayList<>();
    private List<Pharmacist> pharmacistList = new ArrayList<>();
    private List<Administrator> administratorList = new ArrayList<>();

    /**
     * Constructs an AuthService instance and loads the initial data from CSV files.
     */
    public AuthService() {
        loadData(); // Load data initially
    }

    /**
     * Loads user data from CSV files.
     * Clears the existing lists before loading new data.
     *
     * @throws IOException if an error occurs during data loading
     */
    private void loadData() {
        try {
            // Clear the existing lists
            if(patientList != null ) { patientList.clear(); }
            if(doctorList != null ) { doctorList.clear(); }
            if(pharmacistList != null ) { pharmacistList.clear(); }
            if(administratorList != null ) { administratorList.clear(); }

            // Load data from CSV files
            patientList = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);
            CSVDataLoader.loadStaff(FilePaths.STAFF_LIST_PATH, pharmacistList, doctorList, administratorList);
        } catch (IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }

    /**
     * Handles the login process for users. Prompts the user for a hospital ID and password,
     * authenticates the user, and returns the corresponding user object if the login is successful.
     *
     * @return the authenticated User object, or null if authentication fails
     * @throws IOException if an error occurs during data loading
     */
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
            System.out.println("Login successful. Welcome " + user + "!");

            // Check if it is the user's first login
            if (user.getFirstLogin()) {
                String filename = "";
                System.out.println("This is your first login. We recommend you change your password for security purposes.");

                // Determine the appropriate filename based on user role
                if(user.getRole().equals("roles.Patient")) {
                    filename = FilePaths.PATIENT_LIST_PATH;
                } else {
                    filename = FilePaths.STAFF_LIST_PATH;
                }

                // Prompt the user to change their password
                user.changePassword(filename);
                user.setFirstLogin(filename);
            }
            return user; // Return the authenticated user
        } else {
            System.out.println("Invalid credentials.\n");
            return null; // Return null if authentication fails
        }
    }

    /**
     * Helper method to search for a user by their hospital ID in a specific list.
     *
     * @param id the hospital ID to search for
     * @param userList the list of users to search through
     * @return the User object if found, or null if not found
     */
    private User getUserById(String id, List<? extends User> userList) {
        for (User user : userList) {
            if (user.getHospitalID().equals(id)) {
                return user; // Return the user if a match is found
            }
        }
        return null; // Return null if no user is found
    }
}
