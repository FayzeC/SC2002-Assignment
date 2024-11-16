package staffmanagementsystem;

import roles.*;
import java.util.List;

/**
 * The {@code StaffDisplayViewer} class implements the {@link StaffDisplay} interface,
 * providing functionality to display details of all staff members or filtered subsets of staff.
 */
public class StaffDisplayViewer implements StaffDisplay {

    /**
     * Displays all staff members in the provided list.
     * If the list is empty, a message indicating an empty staff list is displayed.
     *
     * @param staffList The list of all staff members to be displayed.
     */
    @Override
    public void displayAllStaff(List<User> staffList) {
        if (staffList.isEmpty()) {
            System.out.println("Staff list is empty.");
        } else {
            System.out.println("All staff members:");
            for (User staff : staffList) {
                System.out.println(getStaffDetails(staff));
            }
        }
    }

    /**
     * Displays filtered staff members from the provided list.
     * If the list is empty, a message indicating no matching staff is displayed.
     *
     * @param filteredStaff The list of filtered staff members to be displayed.
     */
    @Override
    public void displayFilteredStaff(List<User> filteredStaff) {
        if (filteredStaff.isEmpty()) {
            System.out.println("No staff members match the specified criteria.");
        } else {
            System.out.println("Filtered staff members:");
            for (User staff : filteredStaff) {
                System.out.println(getStaffDetails(staff));
            }
        }
    }

    /**
     * Retrieves and formats the details of a staff member, including their age if available.
     *
     * @param staff The {@link User} object representing a staff member.
     * @return A formatted string containing the staff member's details.
     */
    private String getStaffDetails(User staff) {
        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(staff.getHospitalID())
                .append(", Name: ").append(staff.getName())
                .append(", Role: ").append(staff.getRole())
                .append(", Gender: ").append(staff.getGender())
                .append(", Age: ").append(getStaffAge(staff));

        return details.toString();
    }

    /**
     * Retrieves the age of a staff member using reflection if the age field is available.
     *
     * @param staff The {@link User} object representing a staff member.
     * @return The age of the staff member as a {@link String}, or "N/A" if the age is not available.
     */
    private String getStaffAge(User staff) {
        try {
            // Use reflection to check if the class has a getAge method
            return (String) staff.getClass().getMethod("getAge").invoke(staff);
        } catch (Exception e) {
            // If no getAge method exists, return "N/A"
            return "N/A";
        }
    }
}
