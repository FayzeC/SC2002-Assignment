package appointmentsystem;

/**
 * The AdminViewAppointment interface defines methods for administrators
 * to view and manage appointment-related information.This interface serves
 * as a contract for classes that implement administrative appointment views,
 * ensuring consistency in how appointment data is accessed and presented.
 */
public interface adminViewAppointment {

    /**
     * Displays all appointments, including their details.
     * Implementing classes should retrieve and present all available appointment data,
     * ensuring a comprehensive view of scheduled appointments.
     */
    void viewAllAppointments();

    /**
     * Displays appointments filtered by their status.
     * Implementing classes should filter the appointments by the specified status
     * (e.g., confirmed, pending, completed) and display the matching results.
     *
     * @param status The status to filter appointments by (case-insensitive).
     */
    void viewAppointmentsByStatus(String status);
}
