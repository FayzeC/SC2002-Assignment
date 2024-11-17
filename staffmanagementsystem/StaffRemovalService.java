package staffmanagementsystem;

import roles.User;
import java.util.List;

/**
 * The {@code StaffRemovalService} class provides functionality for removing staff members
 * from the system based on their Hospital ID.
 */
public class StaffRemovalService {

    /**
     * Removes a staff member from the provided staff list using their Hospital ID.
     *
     * @param id       The Hospital ID of the staff member to remove.
     * @param staffList The list of staff members from which the removal will be performed.
     * @return {@code true} if the staff member was successfully removed, {@code false} otherwise.
     */
    public static boolean removeStaffById(String id, List<User> staffList) {
        // Attempt to remove the staff member by matching the Hospital ID
        boolean removed = staffList.removeIf(staff -> staff.getHospitalID().equals(id));

        if (removed) {
            // If removal is successful, print a confirmation message
            System.out.println(" ");
        } else {
            // If no matching staff member is found, print an error message
            System.out.println("No staff member with ID " + id + " found.");
        }

        return removed;
    }
}
