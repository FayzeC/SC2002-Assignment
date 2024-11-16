package staffmanagementsystem;

import roles.*;
import java.util.List;

/**
 * The {@code StaffValidator} class provides validation methods for staff-related fields
 * such as roles, gender, and Hospital ID, as well as utility methods like capitalization.
 */
public class StaffValidator {

    /**
     * Validates if the given role is a valid staff role.
     *
     * @param role The role to validate.
     * @return {@code true} if the role is "Doctor", "Pharmacist", or "Administrator"; {@code false} otherwise.
     */
    public boolean isValidRole(String role) {
        return role.equalsIgnoreCase("Doctor") ||
                role.equalsIgnoreCase("Pharmacist") ||
                role.equalsIgnoreCase("Administrator");
    }

    /**
     * Validates if the given gender is valid.
     *
     * @param gender The gender to validate.
     * @return {@code true} if the gender is "Male" or "Female"; {@code false} otherwise.
     */
    public boolean isValidGender(String gender) {
        return gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female");
    }

    /**
     * Validates if the given Hospital ID matches the role's required prefix.
     *
     * @param id   The Hospital ID to validate.
     * @param role The role of the staff member.
     * @return {@code true} if the ID starts with the appropriate prefix ('D' for Doctor, 'P' for Pharmacist, 'A' for Administrator);
     *         {@code false} otherwise.
     */
    public boolean isValidIDForRole(String id, String role) {
        if (role.equalsIgnoreCase("Doctor")) return id.startsWith("D");
        if (role.equalsIgnoreCase("Pharmacist")) return id.startsWith("P");
        if (role.equalsIgnoreCase("Administrator")) return id.startsWith("A");
        return false;
    }

    /**
     * Checks if the given Hospital ID is unique within the staff list.
     *
     * @param id       The Hospital ID to check.
     * @param staffList The list of staff members to validate against.
     * @return {@code true} if the ID is unique; {@code false} otherwise.
     */
    public boolean isUniqueID(String id, List<User> staffList) {
        return staffList.stream().noneMatch(staff -> staff.getHospitalID().equals(id));
    }

    /**
     * Capitalizes the first letter of each word in the given string.
     *
     * @param value The string to capitalize.
     * @return A string with each word capitalized.
     */
    public String capitalize(String value) {
        // Split the input into words by spaces
        String[] words = value.toLowerCase().split("\\s+");

        // Capitalize the first letter of each word
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        // Remove the trailing space and return the result
        return capitalized.toString().trim();
    }
}
