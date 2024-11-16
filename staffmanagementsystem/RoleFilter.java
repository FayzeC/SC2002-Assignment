package staffmanagementsystem;

import roles.User;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code RoleFilter} class implements the {@link FilterCriteria} interface
 * to filter a list of staff members based on their role.
 */
public class RoleFilter implements FilterCriteria {

    private final String role;

    /**
     * Constructs a new {@code RoleFilter} with the specified role.
     *
     * @param role the role to filter by (e.g., "Doctor", "Pharmacist", "Administrator").
     */
    public RoleFilter(String role) {
        this.role = role;
    }

    /**
     * Filters the provided list of staff members by the specified role.
     *
     * @param staffList the list of {@link User} objects to filter.
     * @return a list of {@link User} objects whose role matches the specified criteria.
     */
    @Override
    public List<User> filter(List<User> staffList) {
        return staffList.stream()
                .filter(user -> user.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }
}
