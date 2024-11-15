package roles;

import java.io.IOException;

/**
 * Interface for managing access to medical records and personal information.
 * Implemented by classes that handle the viewing and updating of medical records.
 */
public interface InformationAccess {

    /**
     * Views the medical records of the given user (patient or doctor).
     *
     * @param user The user whose medical records are to be viewed
     */
    void viewMedicalRecords(User user);

    /**
     * Updates the personal information of the given user (patient or doctor).
     *
     * @param user The user whose information is to be updated
     * @throws IOException If there is an error while updating personal information
     */
    void updatePersonalInformation(User user) throws IOException;
}
