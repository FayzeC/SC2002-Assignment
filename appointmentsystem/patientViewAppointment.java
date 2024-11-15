package appointmentsystem;

/**
 * The patientViewAppointment interface defines methods for viewing appointment-related information for patients.
 * It includes methods to view available appointment slots and to check the status of scheduled appointments.
 */
public interface patientViewAppointment {

    /**
     * Displays a list of available appointment slots that can be booked by patients.
     * This view includes details such as appointment ID, date, time slot, doctor name, and status.
     */
    void viewAppointmentSlots();

    /**
     * Displays the scheduled appointments for a specific patient, showing details of each appointment such as
     * appointment ID, date, time slot, doctor ID, doctor name, and status.
     *
     * @param patientId The ID of the patient whose appointment status is to be viewed.
     */
    void viewAppointmentStatus(String patientId);
}
