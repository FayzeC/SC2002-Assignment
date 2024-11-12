package appointmentmanagement.example;

import java.util.List;
import java.util.Optional;

public class AdminAppointmentViewer{
    private AppointmentRepository appointmentRepository;

    public AdminAppointmentViewer(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> viewScheduledAppointments() {
        // Fetch and return the list of all scheduled (confirmed) appointments
        return appointmentRepository.getScheduledAppointments();
    }


    public Appointment viewAppointmentDetails(String appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.getAppointmentById(appointmentId);
        if (appointmentOpt.isPresent()) {
            // Display detailed information if appointment is found
            displayAppointmentDetails(appointmentOpt.get());
            return appointmentOpt.get();
        } else {
            System.out.println("Appointment not found.");
            return null;
        }
    }

    private void displayAppointmentDetails(Appointment appointment) {
        System.out.println("Appointment ID: " + appointment.getAppointmentID());
        System.out.println("Patient ID: " + appointment.getPatientAssigned());
        System.out.println("Doctor ID: " + appointment.getDoctorAssigned());
        System.out.println("Status: " + appointment.getStatus());
        System.out.println("Date: " + appointment.getAppointmentDate());
        System.out.println("Time: " + appointment.getAppointmentTime());

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            Optional<AppointmentOutcomeRecord> outcomeOpt = appointmentRepository.getOutcomeRecord(appointment.getAppointmentID());
            if (outcomeOpt.isPresent()) {
                AppointmentOutcomeRecord outcome = outcomeOpt.get();
                System.out.println("Outcome Notes: " + outcome.getConsultationNotes());
            } else {
                System.out.println("No outcome record available.");
            }
        }
    }
}

