package appointmentoutcomerecordsystem;

import java.io.IOException;

/**
 * The {@code addOutcomeRecord} interface defines a method for adding an appointment
 * outcome record to the system. Implementing classes should provide the logic for
 * adding the record, ensuring the necessary validations and updates are performed.
 */
public interface addOutcomeRecord {

    /**
     * Adds an appointment outcome record for a specified doctor.
     *
     * <p>This method is responsible for performing the following tasks:
     * <ul>
     *   <li>Validating the appointment details and ensuring the appointment is confirmed.</li>
     *   <li>Collecting additional details such as consultation notes, services provided,
     *       medications prescribed, and their quantities.</li>
     *   <li>Storing the new appointment outcome record into the system.</li>
     *   <li>Updating the appointment status to indicate the outcome.</li>
     * </ul>
     *
     * @param doctorID the unique identifier of the doctor adding the outcome record.
     * @throws IOException if an error occurs during file operations or data processing.
     */
    void addAppointmentOutcomeRecord(String doctorID) throws IOException;
}

