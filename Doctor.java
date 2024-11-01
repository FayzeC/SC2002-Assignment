public class Doctor extends User {
    public static final int LOGOUT_OPTION = 9; // Define a constant for logout option
    private String gender;
    private int age;

    public Doctor(String hospitalID, String name, String role, String gender, int age) {
        super(hospitalID, name, role);
        this.gender = gender;
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
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
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
        return "Doctor{" +
//                "Staff ID='" + getHospitalID() + '\'' +
//                ", Role=" + role + '\'' +
                ", Gender='" + gender + '\'' +
                ", Age='" + age + '\'' +
                '}';
    }
}
