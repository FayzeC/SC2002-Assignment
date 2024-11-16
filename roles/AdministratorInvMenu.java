package roles;

import inventorysystem.AdminInventory;

import java.util.Scanner;

/**
 * Represents the menu for managing the hospital inventory by the Administrator.
 * This class provides a user interface to perform inventory-related operations such as
 * adding, removing, updating, and viewing medication inventory.
 */
public class AdministratorInvMenu {

    /**
     * An instance of {@link AdminInventory} to handle inventory-related operations.
     */
    AdminInventory aInventory = new AdminInventory();

    /**
     * Displays the inventory management menu and processes user inputs.
     * The menu includes options to add, remove, update, and view inventory items.
     * The loop continues until the user selects the option to go back to the main menu.
     */
    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while (choice != 6) {
            System.out.println("""
                        \n+======= Inventory Menu =======+
                        1. Add new medicine
                        2. Remove medicine
                        3. Update medicine stock
                        4. Update medicine low stock threshold
                        5. View Inventory
                        6. Go Back to Main Menu
                        """);
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1 -> aInventory.addInventoryItem(); // Add a new medicine to the inventory
                case 2 -> aInventory.removeInventoryItem(); // Remove an existing medicine from the inventory
                case 3 -> aInventory.updateInventoryItem(); // Update the stock of an existing medicine
                case 4 -> aInventory.updateLowStockAlert(); // Update the low stock threshold of a medicine
                case 5 -> aInventory.viewInventory(); // View the current inventory
                case 6 -> System.out.println("Returning to the main menu..."); // Exit the inventory menu
                default -> System.out.println("Invalid choice. Please select a valid option."); // Handle invalid inputs
            }
        }
    }
}
