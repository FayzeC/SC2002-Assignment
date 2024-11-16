package roles;

import appointmentsystem.PatientAppointmentManager;
import appointmentsystem.PatientAppointmentView;
import appointmentoutcomerecordsystem.PatientAORView;
import filemanager.FilePaths;

import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a Patient user in the system with various medical and appointment functionalities.
 * Inherits from the {@link User} class.
 * Provides the patient with capabilities like viewing and updating personal information,
 * managing appointments, and more.
 */
public class Patient extends User {
    /**
     * Logout option constant for the Patient menu.
     */
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
    public PatientAORView aorView;
    public Scanner scanner = new Scanner(System.in);
    // Inject implementations
    private InformationAccess informationAccess;

    /**
     * Constructor to initialize a Patient with essential details and dependencies.
     *
     * @param hospitalID Hospital ID of the patient
     * @param name Name of the patient
     * @param password Password of the patient
     * @param dob Date of birth of the patient
     * @param gender Gender of the patient
     * @param bloodType Blood type of the patient
     * @param email Email of the patient
     * @param doctorAssigned Doctor assigned to the patient
     * @param pastDiagnosis Past diagnosis history of the patient
     * @param pastTreatment Past treatment history of the patient
     * @param firstLogin Indicates if it is the first login of the patient
     * @param role Role of the user (should be 'Patient')
     */
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
        this.aorView = new PatientAORView();
    }

    /**
     * Displays the menu of options available for the Patient to choose from.
     */
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

    /**
     * Handles the user's option selection and executes the corresponding functionality.
     *
     * @param option The option selected by the Patient
     * @throws IOException If there is an error during any of the operations
     */
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
                aorView.viewAppointmentOutcomeRecord(getHospitalID());
                break;
            case 9:
                changePassword(FilePaths.PATIENT_LIST_PATH);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Returns a string representation of the Patient object.
     *
     * @return A string containing the Patient's name
     */
    public String toString() { return "Patient " + getName(); }

    // Getters
    /**
     * Gets the constant logout option for the Patient.
     *
     * @return The logout option constant (10)
     */
    public int getLogoutOption() { return LOGOUT_OPTION; }

    /**
     * Gets the date of birth of the patient.
     *
     * @return The patient's date of birth
     */
    public String getDoB() {
        return dob;
    }

    /**
     * Gets the email address of the patient.
     *
     * @return The patient's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the blood type of the patient.
     *
     * @return The patient's blood type
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Gets the doctor assigned to the patient.
     *
     * @return The assigned doctor's name
     */
    public String getDoctorAssigned() {
        return doctorAssigned;
    }

    /**
     * Gets the past diagnosis history of the patient.
     *
     * @return The patient's past diagnosis information
     */
    public String getPastDiagnosis() {
        return pastDiagnosis;
    }

    /**
     * Gets the past treatment history of the patient.
     *
     * @return The patient's past treatment information
     */
    public String getPastTreatment() {
        return pastTreatment;
    }

    // Setters

    /**
     * Sets the {@link InformationAccess} implementation for the patient.
     * This allows for the management of the patient's medical records and personal information.
     *
     * @param informationAccess The {@link InformationAccess} instance to be set
     */
    public void setInformationAccess(InformationAccess informationAccess) {
        this.informationAccess = informationAccess;
    }

    /**
     * Sets the date of birth of the patient.
     *
     * @param dob The patient's date of birth to be set
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * Sets the blood type of the patient.
     *
     * @param bloodType The patient's blood type to be set
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * Sets the email address of the patient.
     *
     * @param email The patient's email address to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

}