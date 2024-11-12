package base;

import java.io.IOException;

/**
 * Main class for the Hospital Management System (HMS). This class controls the initialization
 * of the system, manages authentication, and provides access to user-specific features.
 * It is implemented as a Singleton to ensure only one instance of the system runs at a time.
 */
public class HospitalManagementClass {
    private static HospitalManagementClass instance;
    private AuthService authService;

    /**
     * Private constructor to initialize the authentication service and any other
     * required components of the Hospital Management System.
     * This constructor is private to enforce the Singleton pattern.
     */
    private HospitalManagementClass() {
        authService = new AuthService();
    }

    /**
     * Provides a single instance of the base.HospitalManagementClass.
     * This method ensures that only one instance of the Hospital Management System is created.
     *
     * @return The single instance of base.HospitalManagementClass.
     */
    public static HospitalManagementClass getInstance() {
        if (instance == null) {
            instance = new HospitalManagementClass();
        }
        return instance;
    }

    /**
     * Starts the Hospital Management System. This method displays a welcome message
     * and enters an infinite loop to prompt user login. Upon successful login,
     * it provides access to user-specific features through the base.RoleAccessController.
     *
     * @throws IOException If an error occurs during login or data loading.
     */
    public void start() throws IOException {
        System.out.println("Welcome to Hospital Management System (HMS)\nPlease login:");
        while (true) { // Loop to allow multiple logins
            User user = authService.login(); // Calls login and returns a base.User if successful
            if (user != null) {
                RoleAccessController.accessUserFeatures(user); // Display user-specific menu
            }
        }
    }

    /**
     * Main method to launch the Hospital Management System. It initializes
     * the system using the Singleton instance and starts the main functionality.
     *
     * @param args Command-line arguments (not used in this application).
     * @throws IOException If an error occurs during initialization or login.
     */
    public static void main(String[] args) throws IOException {
        HospitalManagementClass hospitalSystem = HospitalManagementClass.getInstance();
        hospitalSystem.start();
    }
}