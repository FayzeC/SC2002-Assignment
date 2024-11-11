package MohikaV1;

import java.io.IOException;

public interface patientAppointmentManagement {
    void scheduleAppointment(String patientId, String patientName, String appointmentId);
    void cancelAppointment(String appointmentId);
    void rescheduleAppointment(String patientId, String patientName, String appointmentId, String newAppointmentId) throws IOException;
}