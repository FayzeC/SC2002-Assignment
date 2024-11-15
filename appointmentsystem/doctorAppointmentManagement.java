package appointmentsystem;

import java.io.IOException;

/**
 * The doctorAppointmentManagement interface defines methods for managing doctor actions
 * on patient appointments, specifically for accepting or declining appointments.
 */
public interface doctorAppointmentManagement {

    /**
     * Accepts an appointment, confirming the specified appointment for the doctor.
     *
     * @param doctorId The ID of the doctor accepting the appointment.
     * @param appointmentId The ID of the appointment to be accepted.
     * @throws IOException if an I/O error occurs while processing the acceptance.
     */
    void acceptAppointment(String doctorId, String appointmentId) throws IOException;

    /**
     * Declines an appointment, marking the specified appointment as declined by the doctor.
     *
     * @param doctorId The ID of the doctor declining the appointment.
     * @param appointmentId The ID of the appointment to be declined.
     * @throws IOException if an I/O error occurs while processing the decline.
     */
    void declineAppointment(String doctorId, String appointmentId) throws IOException;
}
