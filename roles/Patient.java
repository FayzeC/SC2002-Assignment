package roles;

import appointmentsystem.PatientAppointmentManager;
import appointmentsystem.PatientAppointmentView;
import filemanager.FilePaths;

import java.io.IOException;
import java.util.Scanner;

public class Patient extends User {
    public static final int LOGOUT_OPTION = 10; // Define a constant for logout option
    public String dob;
    public String gender;
    public String bloodType;
    public String email;
    public String doctorAssigned;
    public String pastDiagnosis;
    public String pastTreatment;
    public PatientAppointmentView appointmentView;
    public PatientAppointmentManager appointmentManager;
    public Scanner scanner = new Scanner(System.in);
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
        this.appointmentView = new PatientAppointmentView();
        this.appointmentManager = new PatientAppointmentManager();
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
                appointmentView.viewAppointmentSlots();
                break;
            case 4:
                System.out.print("Enter Appointment ID to book for consultation: ");
                String appointmentId = scanner.nextLine();
                appointmentManager.scheduleAppointment(getHospitalID(), getName(), appointmentId);
                break;
            case 5:
                System.out.print("Enter Appointment ID to reschedule for consultation: ");
                appointmentId = scanner.nextLine();
                System.out.print("Enter new Appointment ID to reschedule to for consultation: ");
                String newAppointmentId = scanner.nextLine();
                appointmentManager.rescheduleAppointment(getHospitalID(), getName(), appointmentId, newAppointmentId);
                break;
            case 6:
                System.out.print("Enter Appointment ID to cancel for consultation: ");
                appointmentId = scanner.nextLine();
                appointmentManager.cancelAppointment(appointmentId, getHospitalID());
                break;
            case 7: appointmentView.viewAppointmentStatus(getHospitalID());
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

    // Getters
    public String toString() {
        return "Patient " + getName();
    }
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