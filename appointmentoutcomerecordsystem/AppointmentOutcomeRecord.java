package appointmentoutcomerecordsystem;

/**
 * Represents an appointment outcome record in the hospital management system.
 *
 * <p>This class encapsulates details about an appointment, including the assigned doctor,
 * patient, date, service provided, medication, and consultation notes. The class also
 * includes the appointment status and quantity of medication dispensed or required.</p>
 */
public class AppointmentOutcomeRecord {

    private String appointmentID;
    private String patientID;
    private String doctorAssigned;
    private String appointmentDate;
    private String service;
    private String medication;
    private String status;
    private String quantity;
    private String consultationNotes;

    /**
     * Constructs an {@code AppointmentOutcomeRecord} with the provided details.
     *
     * @param appointmentID    the unique identifier for the appointment
     * @param patientID        the identifier of the patient associated with the appointment
     * @param doctorAssigned   the identifier or name of the doctor assigned to the appointment
     * @param appointmentDate  the date of the appointment
     * @param service          the type of service provided during the appointment
     * @param medication       the medication prescribed or discussed in the appointment
     * @param quantity         the quantity of medication dispensed or required
     * @param status           the current status of the appointment (e.g., "Pending", "Approved", "Dispensed")
     * @param consultationNotes additional notes recorded during the consultation
     */
    public AppointmentOutcomeRecord(String appointmentID, String patientID, String doctorAssigned,
                                    String appointmentDate, String service, String medication,
                                    String quantity, String status, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.patientID = patientID; // Ensures the patient ID is valid
        this.doctorAssigned = doctorAssigned;
        this.appointmentDate = appointmentDate;
        this.service = service;
        this.medication = medication;
        this.quantity = quantity;
        this.status = status;
        this.consultationNotes = consultationNotes;
    }

    /**
     * Gets the unique appointment ID.
     *
     * @return the appointment ID
     */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the patient ID associated with the appointment.
     *
     * @return the patient ID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Gets the identifier or name of the doctor assigned to the appointment.
     *
     * @return the doctor assigned to the appointment
     */
    public String getDoctorAssigned() {
        return doctorAssigned;
    }

    /**
     * Gets the date of the appointment.
     *
     * @return the appointment date
     */
    public String getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Gets the type of service provided during the appointment.
     *
     * @return the service provided
     */
    public String getService() {
        return service;
    }

    /**
     * Gets the medication prescribed or discussed in the appointment.
     *
     * @return the medication
     */
    public String getMedication() {
        return medication;
    }

    /**
     * Gets the quantity of medication dispensed or required.
     *
     * @return the medication quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * Gets the current status of the appointment.
     *
     * @return the appointment status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets additional consultation notes recorded during the appointment.
     *
     * @return the consultation notes
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Prints all fields of the {@code AppointmentOutcomeRecord}.
     *
     * <p>This method is used for debugging purposes and can be removed in the final submission.</p>
     */
    public void print() {
        System.out.println("AppointmentID: " + appointmentID);
        System.out.println("PatientID: " + patientID);
        System.out.println("DoctorAssigned: " + doctorAssigned);
        System.out.println("AppointmentDate: " + appointmentDate);
        System.out.println("Service: " + service);
        System.out.println("Medication: " + medication);
        System.out.println("Quantity: " + quantity);
        System.out.println("Status: " + status);
        System.out.println("ConsultationNotes: " + consultationNotes);
    }
}
