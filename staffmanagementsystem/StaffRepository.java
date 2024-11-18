package staffmanagementsystem;

import roles.User;
import java.io.IOException;
import java.util.List;

/**
 * The {@code StaffRepository} interface defines methods for managing the persistence
 * of staff members in the system. It acts as a contract for loading and saving staff data.
 */
public interface StaffRepository {

    /**
     * Loads all staff members from the data source (e.g., a CSV file or database).
     *
     * @return A {@code List} of {@link User} objects representing all staff members.
     * @throws IOException If there is an issue reading the data source.
     */
    List<User> loadAllStaff() throws IOException;

    /**
     * Saves the provided list of staff members to the data source.
     *
     * @param staffList A {@code List} of {@link User} objects to be saved.
     * @throws IOException If there is an issue writing to the data source.
     */
    void saveAllStaff(List<User> staffList) throws IOException;
}
