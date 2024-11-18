package staffmanagementsystem;

import roles.User;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code GenderFilter} class implements the {@link FilterCriteria} interface
 * to filter a list of staff members based on their gender.
 */
public class GenderFilter implements FilterCriteria {

    private final String gender;

    /**
     * Constructs a new {@code GenderFilter} with the specified gender.
     *
     * @param gender the gender to filter by (e.g., "Male", "Female").
     */
    public GenderFilter(String gender) {
        this.gender = gender;
    }

    /**
     * Filters the provided list of staff members by the specified gender.
     *
     * @param staffList the list of {@link User} objects to filter.
     * @return a list of {@link User} objects whose gender matches the specified criteria.
     */
    @Override
    public List<User> filter(List<User> staffList) {
        return staffList.stream()
                .filter(user -> user.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }
}
