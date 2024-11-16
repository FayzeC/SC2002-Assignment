package roles;

import appointmentoutcomerecordsystem.AdminAORView;
import appointmentsystem.AdminAppointmentView;
import appointmentsystem.CSVAppointment;
import filemanager.FilePaths;
import inventorysystem.AdminInventory;
import staffmanagementsystem.AdminStaffManager;
import staffmanagementsystem.CSVStaff;
import staffmanagementsystem.StaffDisplayViewer;

import java.io.IOException;

/**
 * Represents an Administrator user in the hospital system.
 * The Administrator class extends the {@link User} class and provides
 * functionalities for managing hospital staff, viewing appointment details,
 * and handling inventory and replenishment requests.
 */
public class Administrator extends User {

    public static final int LOGOUT_OPTION = 6; // Define a constant for the logout option
    private String age; // The age of the administrator
    AdministratorInvMenu invMenu = new AdministratorInvMenu();
    AdminInventory aInventory = invMenu.aInventory;
    private AdminStaffMenu staffMenu; // Menu for managing staff
    private AdminAppointmentMenu appointmentMenu; // Menu for managing appointments

    /**
     * Constructs an Administrator object with the specified details.
     *
     * @param hospitalID    The unique hospital ID of the administrator.
     * @param name          The name of the administrator.
     * @param password      The password of the administrator.
     * @param role          The role of the administrator (e.g., "Administrator").
     * @param gender        The gender of the administrator.
     * @param age           The age of the administrator.
     * @param firstLogin    Whether it's the administrator's first login.
     * @throws IOException if an I/O error occurs while initializing dependencies.
     */
    public Administrator(String hospitalID, String name, String password, String role, String gender, String age,
                         boolean firstLogin) throws IOException {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
        // Initialize AdminAppointmentMenu with required dependencies
        this.appointmentMenu = new AdminAppointmentMenu(
                new AdminAppointmentView(new CSVAppointment()),  // Appointment view service
                new AdminAORView()          // Appointment outcome record view
        );
    }

    /**
     * Displays the main menu options available to the Administrator.
     */
    @Override
    public void displayMenu() {
        System.out.println("""
                \n+======= Administrator Menu =======+
                1. View and Manage Hospital Staff
                2. View Appointment Details
                3. View and Manage Medication Inventory
                4. Approve Replenishment Requests
                5. Change Password
                6. Logout
                """);
    }

    /**
     * Handles the selection of menu options by the Administrator.
     *
     * @param option The menu option chosen by the Administrator.
     * @throws IOException if an I/O error occurs while processing the selected option.
     */
    @Override
    public void handleOption(int option) throws IOException {
        switch (option) {
            case 1:
                this.staffMenu = new AdminStaffMenu(
                        new AdminStaffManager(new CSVStaff("data/Staff_List.csv"), new StaffDisplayViewer())
                );
                staffMenu.displayMenu();
                break;
            case 2:
                appointmentMenu.displayMenu();
                break;
            case 3:
                invMenu.displayMenu();
                break;
            case 4:
                aInventory.processReplenishRequest();
                break;
            case 5:
                changePassword(FilePaths.STAFF_LIST_PATH);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Returns a string representation of the Administrator.
     *
     * @return A string containing the Administrator's name.
     */
    @Override
    public String toString() {
        return "Administrator " + getName();
    }

    /**
     * Retrieves the logout option for the Administrator.
     *
     * @return The logout option constant.
     */
    public int getLogoutOption() {
        return LOGOUT_OPTION;
    }

    /**
     * Retrieves the age of the Administrator.
     *
     * @return The age of the Administrator.
     */
    public String getAge() {
        return age;
    }
}
