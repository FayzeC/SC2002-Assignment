package appointmentsystem;

import java.util.List;
import java.io.IOException;

/**
 * The AppointmentRepository interface defines the contract for managing appointment data.
 * It provides methods to load all appointments and filter appointments by status.
 * Implementations of this interface can interact with various data sources, such as CSV files or databases.
 */
public interface AppointmentRepository {

    /**
     * Loads all appointments from the data source.
     *
     * @return a list of all appointments.
     * @throws IOException if an I/O error occurs while loading the data.
     */
    List<Appointment> loadAppointments() throws IOException;

    /**
     * Filters a list of appointments by the specified status.
     *
     * @param appointments the list of appointments to filter.
     * @param status       the status to filter appointments by (e.g., "confirmed", "completed").
     * @return a list of appointments matching the specified status.
     */
    List<Appointment> filterAppointmentsByStatus(List<Appointment> appointments, String status);
}
