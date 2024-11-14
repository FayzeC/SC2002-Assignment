package base;

import roles.User;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RoleAccessController {
    public static void accessUserFeatures(User user) throws IOException {
        boolean isLoggedIn = true;
        Scanner scanner = new Scanner(System.in);

        while (isLoggedIn) {
            user.displayMenu();
            try {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == user.getLogoutOption()) { // Call user’s specific logout option
                    isLoggedIn = false;
                    System.out.println("Logging out...\n");
                } else {
                    user.handleOption(choice);
                }
            }catch (InputMismatchException e) {
                System.out.println("Invalid option. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }
}