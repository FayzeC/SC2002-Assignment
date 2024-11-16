package roles;

import appointmentoutcomerecordsystem.PharmacistAORUpdate;
import appointmentoutcomerecordsystem.PharmacistAORView;
import inventorysystem.PharmaInventory;
import filemanager.FilePaths;
import java.io.IOException;

/**
 * Represents a Pharmacist user in the hospital system.
 * A pharmacist can manage medication inventory, update prescriptions, and handle replenishment requests.
 * Inherits from the {@link User} class.
 */
public class Pharmacist extends User {
    public static final int LOGOUT_OPTION = 6; // Define a constant for logout option
    private String age;
    private PharmacistAORView aorView = new PharmacistAORView();
    private PharmacistAORUpdate aorUpdate = new PharmacistAORUpdate();
    PharmaInventory pInventory = new PharmaInventory(); // Inventory manager for pharmacy

    /**
     * Constructs a new Pharmacist instance.
     *
     * @param hospitalID The hospital ID of the pharmacist
     * @param name The name of the pharmacist
     * @param password The password of the pharmacist
     * @param role The role of the user
     * @param gender The gender of the pharmacist
     * @param age The age of the pharmacist
     * @param firstLogin A flag indicating whether this is the pharmacist's first login
     */
    public Pharmacist(String hospitalID, String name, String password, String role, String gender, String age,
                      boolean firstLogin) {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
        this.aorView = new PharmacistAORView();
        this.aorUpdate = new PharmacistAORUpdate();
    }

    /**
     * Displays the menu for the pharmacist role, listing available options for the pharmacist to choose from.
     */
    @Override
    public void displayMenu() {
        System.out.println("""
                \n+======= Pharmacist Menu =======+
                1. View Appointment Outcome Record
                2. Update Prescription Status
                3. View Medication Inventory
                4. Submit Replenishment Request
                5. Change Password
                6. Logout
                """);
    }

    /**
     * Handles the option selected by the pharmacist in the menu.
     * Based on the choice, it calls the appropriate function to perform the action.
     *
     * @param option The option selected by the pharmacist
     * @throws IOException If an error occurs during file I/O
     */
    @Override
    public void handleOption(int option) throws IOException {
        switch (option) {
            case 1:
                aorView.viewAppointmentOutcomeRecord("pending");
                break;
            case 2:
                aorUpdate.approveAppointmentOutcomeRecord();
                break;
            case 3:
                pInventory.viewInventory(); // View the medication inventory
                break;
            case 4:
                pInventory.submitReplenishRequest(); // Submit a request to replenish inventory
                break;
            case 5:
                changePassword(FilePaths.STAFF_LIST_PATH); // Change password for the pharmacist
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Returns a string representation of the pharmacist, displaying their name.
     *
     * @return A string representing the pharmacist
     */
    public String toString() {
        return "Pharmacist " + getName();
    }

    /**
     * Gets the logout option for the pharmacist.
     *
     * @return The logout option number
     */
    public int getLogoutOption() {
        return LOGOUT_OPTION;
    }
}
