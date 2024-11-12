package appointmentmanagement.example;

import java.io.IOException;

public interface doctorAppointmentManagement {
    void acceptAppointment(String doctorId, String appointmentId);
    void declineAppointment(String doctorId, String appointmentId);
}