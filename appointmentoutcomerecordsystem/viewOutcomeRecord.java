package appointmentoutcomerecordsystem;

/**
 * The {@code viewOutcomeRecord} interface defines the contract for viewing
 * appointment outcome records. Implementing classes provide concrete implementations
 * for displaying the details of appointment outcome records based on specific criteria.
 *
 * <p>
 * This interface is designed to facilitate the retrieval and display of appointment
 * outcome records, allowing users to view the relevant information for a specific
 * patient, doctor, or other roles based on the provided identifier.
 * </p>
 */
public interface viewOutcomeRecord {

    /**
     * Displays appointment outcome records based on the provided identifier.
     *
     * <p>
     * This method retrieves and displays appointment outcome records that match the
     * specified {@code checker}. The identifier could represent a patient ID, doctor ID,
     * or other role-based criteria, depending on the implementing class.
     * </p>
     *
     * @param checker the identifier used to filter and view appointment outcome records
     *                (e.g., a patient ID to view records for a specific patient)
     */
    void viewAppointmentOutcomeRecord(String checker);
}