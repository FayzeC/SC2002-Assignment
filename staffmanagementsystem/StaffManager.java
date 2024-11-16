package staffmanagementsystem;

import java.io.IOException;
import java.util.Scanner;

/**
 * The {@code StaffManager} interface defines operations for managing staff members,
 * including viewing, filtering, adding, updating, and removing staff.
 */
public interface StaffManager {

    /**
     * Displays all staff members and provides functionality for filtering based on specific criteria.
     */
    void viewAndFilterStaff();

    /**
     * Handles user input to add a new staff member to the system.
     *
     * @param scanner The {@link Scanner} object for reading user input.
     * @throws IOException If an error occurs while saving staff data.
     */
    void addStaff(Scanner scanner) throws IOException;

    /**
     * Handles user input to update an existing staff member's information in the system.
     *
     * @param scanner The {@link Scanner} object for reading user input.
     * @throws IOException If an error occurs while saving updated staff data.
     */
    void updateStaff(Scanner scanner) throws IOException;

    /**
     * Handles user input to remove a staff member from the system.
     *
     * @param scanner The {@link Scanner} object for reading user input.
     * @throws IOException If an error occurs while saving changes after removal.
     */
    void removeStaff(Scanner scanner) throws IOException;
}
