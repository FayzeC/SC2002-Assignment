package staffmanagementsystem;

import roles.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code AgeFilter} class implements the {@link FilterCriteria} interface
 * to filter a list of staff members based on their age.
 */
public class AgeFilter implements FilterCriteria {
    private final String age;

    /**
     * Constructs an {@code AgeFilter} with the specified age criteria.
     *
     * @param age the age to filter staff members by.
     */
    public AgeFilter(String age) {
        this.age = age;
    }

    /**
     * Filters the provided staff list by the specified age.
     *
     * @param staffList the list of staff members to filter.
     * @return a list of staff members whose age matches the specified age.
     */
    @Override
    public List<User> filter(List<User> staffList) {
        return staffList.stream()
                .filter(user -> getStaffAge(user).equals(age))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to retrieve the age of a staff member based on their role.
     * This handles the differences in how age is stored across {@link Doctor},
     * {@link Pharmacist}, and {@link Administrator}.
     *
     * @param staff the staff member whose age is being retrieved.
     * @return the age of the staff member as a {@code String}.
     */
    private String getStaffAge(User staff) {
        if (staff instanceof Doctor) {
            return ((Doctor) staff).getAge();
        } else if (staff instanceof Pharmacist) {
            return ((Pharmacist) staff).getAge();
        } else if (staff instanceof Administrator) {
            return ((Administrator) staff).getAge();
        }
        return "Unknown"; // Fallback for unhandled staff roles
    }
}
