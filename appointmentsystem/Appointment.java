package appointmentsystem;

/**
 * The Appointment class represents an appointment in the appointment scheduling system.
 * Each appointment contains details such as appointment ID, date, time, patient ID and name,
 * doctor ID and name, and appointment status.
 */
public class Appointment {
    private final String appointmentID;
    private String appointmentDate;
    private String appointmentTime;
    private String patientID;
    private String patientName;
    private String doctorID;
    private String doctorName;
    private String status;

    /**
     * Constructs an Appointment with the specified details.
     *
     * @param appointmentID The unique identifier for the appointment.
     * @param appointmentDate The date of the appointment.
     * @param appointmentTime The time of the appointment.
     * @param patientID The ID of the patient associated with the appointment.
     * @param patientName The name of the patient.
     * @param doctorID The ID of the doctor assigned to the appointment.
     * @param doctorName The name of the doctor.
     * @param status The current status of the appointment (e.g., "UNRESERVED", "PENDING", "CONFIRMED").
     */
    public Appointment(String appointmentID, String appointmentDate, String appointmentTime, String patientID, String patientName, String doctorID, String doctorName, String status) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.status = status;
    }

    /**
     * Prints the details of the appointment to the console.
     * This method is intended for debugging purposes and should be removed in the final submission.
     */
    public void print() {
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Appointment Time: " + appointmentTime);
        System.out.println("Patient ID: " + patientID);
        System.out.println("Patient Name: " + patientName);
        System.out.println("Doctor ID: " + doctorID);
        System.out.println("Doctor Name: " + doctorName);
        System.out.println("Status: " + status);
    }
}
