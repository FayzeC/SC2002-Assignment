package staffmanagementsystem;

import roles.User;
import java.util.List;

/**
 * The {@code FilterCriteria} interface defines a contract for filtering a list of staff members
 * based on specific criteria. Implementing classes should provide the logic for filtering the list.
 */
public interface FilterCriteria {

    /**
     * Filters the provided list of staff members based on a specific criterion.
     *
     * @param staffList the list of {@link User} objects to filter.
     * @return a filtered list of {@link User} objects matching the criteria.
     */
    List<User> filter(List<User> staffList);
}
