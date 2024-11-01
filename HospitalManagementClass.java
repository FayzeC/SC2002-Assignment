import java.util.*;

public class HospitalManagementClass {
    private static HospitalManagementClass instance;
    private AuthService authService; // Login handling
    private List<Doctor> doctorList;
    private List<Pharmacist> pharmacistList;
    private List<Administrator> administratorList;
    private List<Patient> patientList;
    private List<Inventory> inventoryList;

    private HospitalManagementClass() {
        initialiseSystem();
        authService = new AuthService(patientList, doctorList, pharmacistList, administratorList);
    }

    public static HospitalManagementClass getInstance() {
        if (instance == null) {
            instance = new HospitalManagementClass();
        }
        return instance;
    }

    private void initialiseSystem() {
        LoadStaff staffLoader = new LoadStaff();
        staffLoader.importFile("Data/Staff_List.txt");
        doctorList = staffLoader.getDoctorList();
        pharmacistList = staffLoader.getPharmacistList();
        administratorList = staffLoader.getAdministratorList();
//        printList();

        LoadPatient patientLoader = new LoadPatient();
        patientLoader.importFile("Data/Patient_List.txt");
        patientList = patientLoader.getPatientList();
//        printList();

        LoadInventory inventoryLoader = new LoadInventory();
        inventoryLoader.importFile("Data/Medicine_List.txt");
        inventoryList = inventoryLoader.getInventoryList();
//        printList();
    }

    // Method to print the loaded lists (Change the variable names accordingly for each list)
    // Remove in final version
    private void printList() {
        System.out.println("Doctor List:");
        for (Doctor doctor : doctorList) {
            System.out.println(doctor);
        }
    }

    public void start() {
        System.out.println("Welcome to Hospital Management System (HMS)\nPlease login:");
        while (true) { // Loop to allow multiple logins
            User user = authService.login(); // Calls login and returns a User if successful
            if (user != null) {
                RoleAccessController.accessUserFeatures(user); // Display user-specific menu
            }
        }
    }

    public static void main(String[] args) {
        HospitalManagementClass hospitalSystem = HospitalManagementClass.getInstance();
        hospitalSystem.start();
    }
}