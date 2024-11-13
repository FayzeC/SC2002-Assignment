package base;

import filemanager.FilePaths;
import inventorysystem.AdminInventory;
import java.io.IOException;

public class Administrator extends User {
    public static final int LOGOUT_OPTION = 6; // Define a constant for logout option
    private String age;
    AdministratorInvMenu invMenu = new AdministratorInvMenu();
    AdminInventory aInventory = invMenu.aInventory;

    public Administrator(String hospitalID, String name, String password, String role, String gender, String age,
                         boolean firstLogin) {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
    }

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

    public String toString() {
        return "Administrator " + getName();
    }

    // Getters
    public int getLogoutOption() {
        return LOGOUT_OPTION;
    }
}