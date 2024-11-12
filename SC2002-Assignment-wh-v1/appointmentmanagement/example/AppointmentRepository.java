package appointmentmanagement.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppointmentRepository {
    private List<Appointment> appointments = new ArrayList<>();
    private List<AppointmentOutcomeRecord> outcomeRecords = new ArrayList<>();

    public List<Appointment> getScheduledAppointments() {
        // Returns only appointments with CONFIRMED status
        return appointments.stream()
                .filter(app -> app.getStatus() == AppointmentStatus.CONFIRMED)
                .collect(Collectors.toList());
    }

    public Optional<Appointment> getAppointmentById(String appointmentId) {
        return appointments.stream()
                .filter(app -> app.getAppointmentID().equals(appointmentId))
                .findFirst();
    }

    public Optional<AppointmentOutcomeRecord> getOutcomeRecord(String appointmentId) {
        return outcomeRecords.stream()
                .filter(record -> record.getAppointmentID().equals(appointmentId))
                .findFirst();
    }

    // Method to add sample data (appointments and outcomes)
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void addOutcomeRecord(AppointmentOutcomeRecord outcomeRecord) {
        outcomeRecords.add(outcomeRecord);
    }
}
