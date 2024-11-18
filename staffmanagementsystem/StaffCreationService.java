package staffmanagementsystem;

import roles.Administrator;
import roles.Doctor;
import roles.Pharmacist;
import roles.User;

import java.io.IOException;

/**
 * The {@code StaffCreationService} class provides functionality for creating new staff members
 * based on the provided details and hashing their passwords securely.
 */
public class StaffCreationService {

    /**
     * Creates a new staff member based on the provided details.
     * Validates the role and assigns the appropriate subclass for the staff member
     * (e.g., {@link Doctor}, {@link Pharmacist}, {@link Administrator}).
     *
     * @param id         The hospital ID of the staff.
     * @param role       The role of the staff (Doctor, Pharmacist, Administrator).
     * @param name       The name of the staff.
     * @param gender     The gender of the staff (Male/Female).
     * @param age        The age of the staff.
     * @param password   The password for the staff account, which will be hashed.
     * @param firstLogin
     * @return The newly created staff member as a {@link User} object.
     * @throws IOException           If there is an error during the staff creation process.
     * @throws IllegalStateException If the provided role is unexpected or invalid.
     */
    public static User createStaffMember(String id, String role, String name, String gender, String age, String password, boolean firstLogin) throws IOException {
        // Create the staff member based on the role
        return switch (role) {
            case "Doctor" -> new Doctor(id, name, password, role, gender, age, firstLogin);
            case "Pharmacist" -> new Pharmacist(id, name, password, role, gender, age, firstLogin);
            case "Administrator" -> new Administrator(id, name, password, role, gender, age, firstLogin);
            default -> throw new IllegalStateException("Unexpected role: " + role);
        };
    }
}
