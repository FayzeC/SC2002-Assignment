import java.util.Scanner;

public class RoleAccessController {
    public static void accessUserFeatures(User user) {
        boolean isLoggedIn = true;
        Scanner scanner = new Scanner(System.in);

        while (isLoggedIn) {
            user.displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == user.getLogoutOption()) { // Call user’s specific logout option
                isLoggedIn = false;
                System.out.println("Logging out...\n");
            } else {
                user.handleOption(choice);
            }
        }
    }
}