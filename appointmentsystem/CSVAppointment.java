package appointmentsystem;

import filemanager.FilePaths;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVAppointment is a concrete implementation of the {@link AppointmentRepository} interface.
 * It handles loading appointment data from a CSV file and filtering appointments based on their status.
 */
public class CSVAppointment implements AppointmentRepository {

    /**
     * Loads all appointments from the CSV file specified in {@link FilePaths#APPOINTMENT_LIST_PATH}.
     *
     * @return a list of {@link Appointment} objects loaded from the CSV file.
     *         Returns an empty list if the file does not exist or no appointments are found.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    @Override
    public List<Appointment> loadAppointments() throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);

        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return appointments;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8) {
                    appointments.add(new Appointment(
                            fields[0].trim(), // Appointment ID
                            fields[1].trim(), // Date
                            fields[2].trim(), // Time Slot
                            fields[3].trim(), // Patient ID
                            fields[4].trim(), // Patient Name
                            fields[5].trim(), // Doctor ID
                            fields[6].trim(), // Doctor Name
                            fields[7].trim()  // Status
                    ));
                }
            }
        }
        return appointments;
    }

    /**
     * Filters a list of appointments based on their status.
     *
     * @param appointments the list of appointments to filter.
     * @param status       the status to filter appointments by (e.g., "confirmed", "completed").
     * @return a list of appointments that match the specified status. Returns an empty list if no matches are found.
     */
    @Override
    public List<Appointment> filterAppointmentsByStatus(List<Appointment> appointments, String status) {
        List<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equalsIgnoreCase(status)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }
}
