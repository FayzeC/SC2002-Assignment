public class Patient extends User {
    public static final int LOGOUT_OPTION = 10; // Define a constant for logout option
    private String dob;
    private String gender;
    private String bloodType;
    private String email;
    private String pastDiagnosis;
    private String pastTreatment;

    // Inject implementations
    private viewMedicalRecords viewMedicalRecords;
    private PersonalInfoUpdater personalInfoUpdater;

    public Patient(String hospitalID, String name, String role, String dob, String gender, String bloodType, String email) {
        super(hospitalID, name, role);
        this.dob = dob;
        this.gender = gender;
        this.bloodType = bloodType;
        this.email = email;
        this.pastDiagnosis = "NA";
        this.pastTreatment = "NA";
    }

    @Override
    public void displayMenu() {
        System.out.println("""
                \n+======= Patient Menu =======+
                1. View Medical Record
                2. Update Personal Information
                3. View Available Appointment Slots
                4. Schedule an Appointment
                5. Reschedule an Appointment
                6. Cancel an Appointment
                7. View Scheduled Appointments
                8. View Past Appointments Outcome Records
                9. Change Password
                10. Logout
                """);
    }

    @Override
    public void handleOption(int option) {
        switch (option) {
            case 1:
                viewMedicalRecords.viewMedicalRecords(this);
                break;
            case 2:
                personalInfoUpdater.updatePersonalInformation(this);
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
                break;
            case 9:
                changePassword();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    // Getters
    public int getLogoutOption() { return LOGOUT_OPTION; }

    public String getDoB() { return dob; }

    public String getGender() { return gender; }

    public String getEmail() { return email; }

    public String getBloodType() { return bloodType; }

    public String getPastDiagnosis() { return pastDiagnosis; }

    public String getPastTreatment() { return pastTreatment; }

    // Setters
    public void setPersonalInfoUpdater(PersonalInfoUpdater updater) { this.personalInfoUpdater = updater; }

    public void setViewMedicalRecords(viewMedicalRecords records) { this.viewMedicalRecords = records; }

    public void setDob(String dob) { this.dob = dob; }

    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public void setEmail(String email) { this.email = email; }

    public String toString() { // Remove this when submitting final version
        return "Patient{" +
//                "patientId='" + patientID + '\'' +
                ", dateOfBirth=" + dob +
                ", gender='" + gender + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
