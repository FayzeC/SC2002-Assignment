package basic;

import java.io.IOException;

public class HospitalManagementClass {
    private static HospitalManagementClass instance;
    private AuthService authService;

    private HospitalManagementClass() {
        authService = new AuthService();

    }

    public static HospitalManagementClass getInstance() {
        if (instance == null) {
            instance = new HospitalManagementClass();
        }
        return instance;
    }

    public void start() throws IOException {
        System.out.println("Welcome to Hospital Management System (HMS)\nPlease login:");
        while (true) { // Loop to allow multiple logins
            User user = authService.login(); // Calls login and returns a User if successful
            if (user != null) {
                RoleAccessController.accessUserFeatures(user); // Display user-specific menu
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HospitalManagementClass hospitalSystem = HospitalManagementClass.getInstance();
        hospitalSystem.start();
    }
}
