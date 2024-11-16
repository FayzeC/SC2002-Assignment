package roles;

import appointmentoutcomerecordsystem.DoctorAORAdd;
import appointmentsystem.DoctorInformationAccess;
import appointmentsystem.DoctorAppointmentManager;
import appointmentsystem.DoctorAvailability;
import appointmentsystem.DoctorScheduleView;
import filemanager.FilePaths;

import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a Doctor user in the system with various medical and scheduling functionalities.
 * Inherits from the {@link User} class.
 * Provides the doctor with capabilities like viewing and updating medical records,
 * managing appointments, setting availability, and more.
 */
public class Doctor extends User {
    /**
     * Logout option constant for the Doctor menu.
     */
    public static final int LOGOUT_OPTION = 9;

    private String age;
    private DoctorAvailability availabilityManager;
    private DoctorAppointmentManager appointmentManager;
    private DoctorScheduleView schedule;
    private DoctorInformationAccess informationAccess;
    private DoctorAORAdd aorADD;

    /**
     * Constructor to initialize a Doctor with essential details and dependencies.
     *
     * @param hospitalID Hospital ID of the doctor
     * @param name Name of the doctor
     * @param password Password of the doctor
     * @param role Role of the user (should be 'Doctor')
     * @param gender Gender of the doctor
     * @param age Age of the doctor
     * @param firstLogin Indicates if it is the first login of the doctor
     * @throws IOException If there is an error during initialization
     */
    public Doctor(String hospitalID, String name, String password, String role, String gender, String age, boolean firstLogin) throws IOException {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
        this.availabilityManager = new DoctorAvailability();
        this.appointmentManager = new DoctorAppointmentManager();
        this.schedule = new DoctorScheduleView();
        this.informationAccess = new DoctorInformationAccess();
        this.aorADD = new DoctorAORAdd();
    }

    /**
     * Displays the menu of options available for the Doctor to choose from.
     */
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

    /**
     * Handles the user's option selection and executes the corresponding functionality.
     *
     * @param option The option selected by the Doctor
     * @throws IOException If there is an error during any of the operations
     */
    @Override
    public void handleOption(int option) throws IOException {
        Scanner scanner = new Scanner(System.in);
        switch (option) {
            case 1:
                informationAccess.viewMedicalRecords(this);
                break;
            case 2:
                informationAccess.updatePersonalInformation(this);
                break;
            case 3:
                schedule.viewPersonalSchedule(getHospitalID());
                break;
            case 4:
                availabilityManager.setAvailability(this);
                break;
            case 5:
                System.out.print("Enter Appointment ID to accept or decline: ");
                String appointmentId = scanner.nextLine();
                System.out.print("Enter 'A' to accept or 'D' to decline: ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (choice.equals("A")) {
                    appointmentManager.acceptAppointment(getHospitalID(), appointmentId);
                } else if (choice.equals("D")) {
                    appointmentManager.declineAppointment(getHospitalID(), appointmentId);
                } else {
                    System.out.println("Invalid choice.");
                }
                break;
            case 6:
                schedule.viewUpcomingAppointments(getHospitalID());
                break;
            case 7:
                aorADD.addAppointmentOutcomeRecord(getHospitalID());
                break;
            case 8:
                changePassword(FilePaths.STAFF_LIST_PATH);
                break;
            case 9:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Gets the constant logout option for the Doctor.
     *
     * @return The logout option constant (9)
     */
    public int getLogoutOption() {
        return LOGOUT_OPTION;
    }

    /**
     * Returns a string representation of the Doctor object.
     *
     * @return A string containing the Doctor's name
     */
    public String toString() {
        return "Doctor " + getName();
    }

    /**
     * Retrieves the age of the Doctor.
     *
     * @return The age of the Doctor.
     */
    public String getAge() {
        return age;
    }
}
