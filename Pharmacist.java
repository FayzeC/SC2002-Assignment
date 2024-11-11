import java.io.IOException;

public class Pharmacist extends User {
    public static final int LOGOUT_OPTION = 6; // Define a constant for logout option
    private String age;

    public Pharmacist(String hospitalID, String name, String password, String role, String gender, String age, boolean firstLogin) {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
    }

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

    @Override
    public void handleOption(int option) throws IOException {
        switch (option) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                changePassword(FilePaths.STAFF_LIST_PATH);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    // Getters
    public int getLogoutOption() { return LOGOUT_OPTION; }
}