package AppointmentSystem;

import java.io.IOException;

public interface doctorAppointmentManagement {
    void acceptAppointment(String doctorId, String appointmentId) throws IOException;
    void declineAppointment(String doctorId, String appointmentId) throws IOException;
}
