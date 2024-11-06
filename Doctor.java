import java.io.IOException;

public class Doctor extends User {
    public static final int LOGOUT_OPTION = 9; // Define a constant for logout option
    private String age;

    public Doctor(String hospitalID, String name, String password, String role, String gender, String age, boolean firstLogin) {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
    }

    @Override
    public void displayMenu() {
        System.out.println("""
                \n+======= Doctor Menu =======+
                1. View Patient Medical Records
                2. Update Patient Medical Records
                3. View Personal Schedule
                4. Set Availability for Appointments
                5. Accept or Decline Appointment Requests
                6. View Upcoming Appointments
                7. Record Appointment Outcome
                8. Change Password
                9. Logout
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
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                changePassword(FilePaths.STAFF_LIST_PATH);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    // Getters
    public int getLogoutOption() { return LOGOUT_OPTION; }
}