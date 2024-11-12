package base;

import filemanager.FilePaths;

import java.io.IOException;

public class Patient extends User {
    public static final int LOGOUT_OPTION = 10; // Define a constant for logout option
    private String dob;
    private String gender;
    private String bloodType;
    private String email;
    private String doctorAssigned;
    private String pastDiagnosis;
    private String pastTreatment;

    // Inject implementations
    private InformationAccess informationAccess;

    public Patient(String hospitalID, String name, String password, String dob, String gender, String bloodType, String email, String doctorAssigned, String pastDiagnosis, String pastTreatment, boolean firstLogin, String role) {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.dob = dob;
        this.bloodType = bloodType;
        this.email = email;
        this.doctorAssigned = doctorAssigned;
        this.pastDiagnosis = pastDiagnosis;
        this.pastTreatment = pastTreatment;
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
    public void handleOption(int option) throws IOException {
        switch (option) {
            case 1:
                informationAccess.viewMedicalRecords(this);
                break;
            case 2:
                informationAccess.updatePersonalInformation(this);
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
                changePassword(FilePaths.PATIENT_LIST_PATH);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    public void print(){ // Remove after final submission this is just to check if data is loaded correctly
        System.out.println("Name: " + getName());
        System.out.println("Password: " + getPassword());
        System.out.println("Role: " + getRole());
    }

    public String toString() { return "Patient " + getName(); }

    // Getters
    public int getLogoutOption() { return LOGOUT_OPTION; }

    public String getDoB() { return dob; }

    public String getEmail() { return email; }

    public String getBloodType() { return bloodType; }

    public String getDoctorAssigned() { return doctorAssigned; }

    public String getPastDiagnosis() { return pastDiagnosis; }

    public String getPastTreatment() { return pastTreatment; }

    // Setters
    public void setInformationAccess(InformationAccess informationAccess) { this.informationAccess = informationAccess; }

    public void setDob(String dob) { this.dob = dob; }

    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public void setEmail(String email) { this.email = email; }
}