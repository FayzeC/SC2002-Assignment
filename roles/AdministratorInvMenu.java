package roles;

import inventorysystem.AdminInventory;

import java.util.Scanner;

/**
 * Represents the menu for managing the hospital inventory by the Administrator.
 * Provides options to add, remove, update, and view medication inventory.
 */
public class AdministratorInvMenu {

    AdminInventory aInventory = new AdminInventory();

    /**
     * Displays the inventory management menu and handles user choices.
     */
    public void displayMenu(){
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while(choice != 6){
            System.out.println("""
                        \n+======= Inventory Menu =======+
                        1. Add new medicine
                        2. Remove medicine
                        3. Update medicine stock
                        4. Update medicine low stock threshold
                        5. View Inventory
                        6. Quit
                        """);
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    aInventory.addInventoryItem();
                    break;
                case 2:
                    aInventory.removeInventoryItem();
                    break;
                case 3:
                    aInventory.updateInventoryItem();
                    break;
                case 4:
                    aInventory.updateLowStockAlert();
                    break;
                case 5:
                    aInventory.viewInventory();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }}
    }
}
