package appointmentsystem;

import java.io.IOException;
import java.util.List;

/**
 * The AdminAppointmentView class implements the {@link adminViewAppointment} interface,
 * enabling administrators to view all appointments and filter appointments by status.
 * This class interacts with the {@link AppointmentRepository} to retrieve appointment data.
 */
public class AdminAppointmentView implements adminViewAppointment {

    private final AppointmentRepository appointmentRepo;

    /**
     * Constructs an AdminAppointmentView object with the specified AppointmentRepository instance.
     * The repository is used to load and filter appointment data.
     *
     * @param appointmentRepo the AppointmentRepository instance for managing appointment data
     */
    public AdminAppointmentView(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    /**
     * Displays all appointments with their details.
     * This method retrieves the list of all appointments from the repository
     * and prints them in a tabular format. If no appointments are found, it displays a message.
     */
    @Override
    public void viewAllAppointments() {
        try {
            List<Appointment> appointments = appointmentRepo.loadAppointments();
            if (appointments.isEmpty()) {
                System.out.println("No appointments found.");
                return;
            }

            System.out.println("\n+======= All Appointments =======+");
            System.out.printf("%-15s %-15s %-15s %-15s %-20s %-15s %-15s\n",
                    "Appointment ID", "Date", "Time Slot", "Patient ID", "Patient Name", "Doctor ID", "Doctor Name", "Status");

            for (Appointment appointment : appointments) {
                System.out.printf("%-15s %-15s %-15s %-15s %-20s %-15s %-15s %-15s\n",
                        appointment.getAppointmentId(), appointment.getDate(), appointment.getTimeSlot(),
                        appointment.getPatientId(), appointment.getPatientName(), appointment.getDoctorId(),
                        appointment.getDoctorName(), appointment.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }

    /**
     * Displays appointments filtered by their status.
     * This method retrieves all appointments from the repository,
     * filters them by the specified status, and prints the filtered appointments in a tabular format.
     * If no appointments match the status, it displays a message.
     *
     * @param status the status to filter appointments by (e.g., "confirmed", "completed").
     */
    @Override
    public void viewAppointmentsByStatus(String status) {
        try {
            List<Appointment> appointments = appointmentRepo.loadAppointments();
            List<Appointment> filteredAppointments = appointmentRepo.filterAppointmentsByStatus(appointments, status);

            if (filteredAppointments.isEmpty()) {
                System.out.println("No appointments with status: " + status);
                return;
            }

            System.out.println("\n+======= Appointments with Status: " + status.toUpperCase() + " =======+");
            System.out.printf("%-15s %-15s %-15s %-15s %-20s %-15s %-15s\n",
                    "Appointment ID", "Date", "Time Slot", "Patient ID", "Patient Name", "Doctor ID", "Doctor Name", "Status");

            for (Appointment appointment : filteredAppointments) {
                System.out.printf("%-15s %-15s %-15s %-15s %-20s %-15s %-15s %-15s\n",
                        appointment.getAppointmentId(), appointment.getDate(), appointment.getTimeSlot(),
                        appointment.getPatientId(), appointment.getPatientName(), appointment.getDoctorId(),
                        appointment.getDoctorName(), appointment.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }
}
