package base;

import inventorysystem.AdminInventory;

import java.util.Scanner;

public class AdministratorInvMenu {
    private int choice = 0;
    AdminInventory aInventory = new AdminInventory();


    public void displayMenu(){
        Scanner sc = new Scanner(System.in);
        while(choice != 6){
            System.out.println("""
                        \n+======= Inventory Menu =======+
                        1. Add new medicine
                        2. Remove medicine
                        3. Update medicine stock
                        4. Update medicine low stock threshold
                        5. View Inventory
                        6. quit
                        """);
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
                    break;
            }}

    }
}
