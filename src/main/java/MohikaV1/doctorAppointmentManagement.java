package MohikaV1;

public interface doctorAppointmentManagement {
    void acceptAppointment(String doctorId, String appointmentId);
    void declineAppointment(String doctorId, String appointmentId);
}
