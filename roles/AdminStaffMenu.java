package roles;

import staffmanagementsystem.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * The AdminStaffMenu class provides a user interface for administrators to manage hospital staff.
 * Administrators can:
 * - View and filter staff.
 * - Add new staff members.
 * - Update existing staff information.
 * - Remove staff members.
 * This class interacts with the {@link AdminStaffManager} to perform staff management operations.
 */
public class AdminStaffMenu {

    /**
     * The {@link AdminStaffManager} instance responsible for managing staff data and operations.
     */
    private final AdminStaffManager sManager;

    /**
     * Constructs an AdminStaffMenu with the specified AdminStaffManager instance.
     *
     * @param sManager The AdminStaffManager instance for handling staff operations.
     * @throws IOException If an I/O error occurs during initialization.
     */
    public AdminStaffMenu(AdminStaffManager sManager) throws IOException {
        this.sManager = sManager;
    }

    /**
     * Displays the staff management menu and processes user input.
     * The menu allows administrators to:
     * - View and filter staff based on criteria.
     * - Add new staff members.
     * - Update existing staff details.
     * - Remove staff members from the system.
     * The menu loops until the administrator chooses to return to the main menu.
     *
     * @throws IOException If an I/O error occurs during any staff operation.
     */
    public void displayMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean continueMenu = true;

        do {
            System.out.println("\n+======= Staff Management =======+");
            System.out.println("1. View and Filter Staff");
            System.out.println("2. Add Staff");
            System.out.println("3. Update Staff");
            System.out.println("4. Remove Staff");
            System.out.println("5. Go Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> sManager.viewAndFilterStaff(); // View and filter staff
                case 2 -> sManager.addStaff(scanner); // Add a new staff member
                case 3 -> sManager.updateStaff(scanner); // Update staff details
                case 4 -> sManager.removeStaff(scanner); // Remove a staff member
                case 5 -> {
                    System.out.println("Returning to the main menu..."); // Exit the menu
                    return;
                }
                default -> System.out.println("Invalid choice"); // Handle invalid input
            }
        } while (continueMenu);
    }
}
