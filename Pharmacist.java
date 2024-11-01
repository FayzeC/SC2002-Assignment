public class Pharmacist extends User{
    public static final int LOGOUT_OPTION = 6; // Define a constant for logout option
    private String gender;
    private int age;

    public Pharmacist(String hospitalID, String name, String role, String gender, int age) {
        super(hospitalID, name, role);
        this.gender = gender;
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
    public void handleOption(int option) {
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
                changePassword();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    // Getters
    public int getLogoutOption() { return LOGOUT_OPTION; }

    // Setters


    public String toString() { // Remove this when submitting final version
        return "Pharmacist{" +
//                "Staff ID='" + staffID + '\'' +
//                ", Role=" + role + '\'' +
                ", Gender='" + gender + '\'' +
                ", Age='" + age + '\'' +
                '}';
    }
}
