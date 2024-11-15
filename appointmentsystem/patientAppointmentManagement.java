package appointmentsystem;

import java.io.IOException;

/**
 * The patientAppointmentManagement interface defines methods for managing patient appointments,
 * including scheduling, canceling, and rescheduling appointments.
 */
public interface patientAppointmentManagement {

    /**
     * Schedules an appointment for a patient with the specified appointment ID.
     *
     * @param patientId The ID of the patient scheduling the appointment.
     * @param patientName The name of the patient.
     * @param appointmentId The ID of the appointment to be scheduled.
     */
    void scheduleAppointment(String patientId, String patientName, String appointmentId);

    /**
     * Cancels an appointment for a patient based on the specified appointment ID.
     *
     * @param appointmentId The ID of the appointment to be canceled.
     * @param patientId The ID of the patient canceling the appointment.
     */
    void cancelAppointment(String appointmentId, String patientId);

    /**
     * Reschedules an existing appointment to a new appointment slot if available.
     *
     * @param patientId The ID of the patient rescheduling the appointment.
     * @param patientName The name of the patient.
     * @param appointmentId The current appointment ID to be rescheduled.
     * @param newAppointmentId The new appointment ID for rescheduling.
     * @throws IOException if an I/O error occurs while processing the rescheduling.
     */
    void rescheduleAppointment(String patientId, String patientName, String appointmentId, String newAppointmentId) throws IOException;
}
