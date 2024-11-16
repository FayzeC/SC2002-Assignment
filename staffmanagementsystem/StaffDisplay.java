package staffmanagementsystem;

import roles.User;
import java.util.List;

/**
 * The {@code StaffDisplay} interface defines methods for displaying staff information.
 * It provides functionality to display all staff members or filtered subsets of staff.
 */
public interface StaffDisplay {

    /**
     * Displays all staff members from the provided list.
     *
     * @param staffList The list of all staff members to be displayed.
     */
    void displayAllStaff(List<User> staffList);

    /**
     * Displays a filtered subset of staff members from the provided list.
     *
     * @param filteredStaff The list of filtered staff members to be displayed.
     */
    void displayFilteredStaff(List<User> filteredStaff);
}
