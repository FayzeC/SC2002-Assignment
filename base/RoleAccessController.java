package base;

import roles.User;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The RoleAccessController class handles the user interface for accessing features
 * based on the user’s role. It displays a menu of options to the user and processes
 * the user's input accordingly. The user can select options or log out.
 */
public class RoleAccessController {

    /**
     * Provides access to the user’s features by displaying their menu and handling
     * user input based on the selected options. The method will continue until the user
     * logs out.
     *
     * @param user the User object whose role determines the available menu options
     * @throws IOException if an input/output error occurs while handling user input
     */
    public static void accessUserFeatures(User user) throws IOException {
        boolean isLoggedIn = true; // Flag to track whether the user is logged in
        Scanner scanner = new Scanner(System.in);

        // Loop to keep displaying the menu until the user logs out
        while (isLoggedIn) {
            user.displayMenu(); // Display the menu specific to the user's role
            try {
                // Prompt user for choice
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt(); // Read the user's choice

                // Check if the user chose to log out
                if (choice == user.getLogoutOption()) {
                    isLoggedIn = false; // Set flag to false to exit the loop
                    System.out.println("Logging out...\n");
                } else {
                    // Handle the selected option
                    user.handleOption(choice);
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (non-integer)
                System.out.println("Invalid option. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner buffer
            }
        }
    }
}
