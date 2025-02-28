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
     * @param appointmentID   The unique identifier for the appointment.
     * @param appointmentDate The date of the appointment.
     * @param appointmentTime The time of the appointment.
     * @param patientID       The ID of the patient associated with the appointment.
     * @param patientName     The name of the patient.
     * @param doctorID        The ID of the doctor assigned to the appointment.
     * @param doctorName      The name of the doctor.
     * @param status          The current status of the appointment (e.g., "UNRESERVED", "PENDING", "CONFIRMED").
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
     * Retrieves the unique identifier of the appointment.
     *
     * @return The appointment ID.
     */
    public String getAppointmentId() {
        return appointmentID;
    }

    /**
     * Retrieves the date of the appointment.
     *
     * @return The appointment date in the format "YYYY-MM-DD".
     */
    public String getDate() {
        return appointmentDate;
    }

    /**
     * Retrieves the time slot of the appointment.
     *
     * @return The appointment time in the format "HH:mm".
     */
    public String getTimeSlot() {
        return appointmentTime;
    }

    /**
     * Retrieves the unique ID of the patient associated with the appointment.
     *
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientID;
    }

    /**
     * Retrieves the name of the patient associated with the appointment.
     *
     * @return The patient's name.
     */
    public String getPatientName() {
        return patientName;
    }

    /**
            * Retrieves the unique ID of the patient associated with the appointment.
     *
             * @return The patient ID.
     */
    public String getDoctorId() {
        return doctorID;
    }

    /**
     * Retrieves the name of the doctor assigned to the appointment.
     *
     * @return The doctor's name.
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * Retrieves the current status of the appointment.
     *
     * @return The status of the appointment (e.g., "UNRESERVED", "PENDING", "CONFIRMED").
     */
    public String getStatus() {
        return status;
    }
}
