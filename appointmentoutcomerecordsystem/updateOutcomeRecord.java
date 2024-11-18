package appointmentoutcomerecordsystem;

import java.io.IOException;
/**
 * The {@code updateOutcomeRecord} interface defines the contract for updating fields
 * in appointment outcome records. Implementing classes provide concrete implementations
 * for modifying specific attributes of these records.
 *
 * <p>
 * This interface is used to ensure consistency and reusability in the update operations
 * for appointment outcome records across different roles or modules in the system.
 * </p>
 */
public interface updateOutcomeRecord {

    /**
     * Updates a specific field of an appointment outcome record identified by its appointment ID.
     *
     * <p>
     * This method allows modifying a specific attribute of the appointment outcome record,
     * identified by the {@code header}, with a new value. It can be used for operations such
     * as updating the status, consultation notes, or other relevant fields.
     * </p>
     *
     * @param appointmentID the unique identifier of the appointment record to update
     * @param header the name of the field to be updated (e.g., "Status", "Medication")
     * @param newValue the new value to set for the specified field
     * @throws IOException if an error occurs while accessing or modifying the underlying data storage
     */
    void updateAppointmentOutcomeRecord(String appointmentID, String header, String newValue) throws IOException;
}
