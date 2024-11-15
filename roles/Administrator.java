package roles;

import filemanager.FilePaths;
import inventorysystem.AdminInventory;
import java.io.IOException;

/**
 * Represents an Administrator user in the hospital system.
 * Inherits from the {@link User} class and provides functionality to manage hospital staff,
 * appointment details, and medication inventory.
 */
public class Administrator extends User {

    public static final int LOGOUT_OPTION = 6; // Define a constant for logout option
    private String age;
    AdministratorInvMenu invMenu = new AdministratorInvMenu();
    AdminInventory aInventory = invMenu.aInventory;

    /**
     * Constructs an Administrator object with the specified details.
     *
     * @param hospitalID    the hospital ID of the administrator
     * @param name          the name of the administrator
     * @param password      the password of the administrator
     * @param role          the role of the administrator
     * @param gender        the gender of the administrator
     * @param age           the age of the administrator
     * @param firstLogin    whether it's the administrator's first login
     */
    public Administrator(String hospitalID, String name, String password, String role, String gender, String age,
                         boolean firstLogin) {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
    }

    /**
     * Displays the Administrator's menu options.
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
     * Handles the selection of menu options by the administrator.
     *
     * @param option the menu option chosen by the administrator
     * @throws IOException if there is an error during file handling
     */
    @Override
    public void handleOption(int option) throws IOException {
        switch (option) {
            case 1:
                break;
            case 2:
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
     * @return the name of the Administrator
     */
    public String toString() {
        return "Administrator " + getName();
    }

    /**
     * Gets the logout option for the Administrator.
     *
     * @return the logout option constant
     */
    public int getLogoutOption() {
        return LOGOUT_OPTION;
    }
}
