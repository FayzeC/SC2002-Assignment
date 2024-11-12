package staffmanagement;

import java.util.List;
import role.example.*;
public interface StaffManager {
    List<StaffDetailsDTO> getStaffListByFilter(FilterCriteria criteria);
    void addStaff(User user);
    void updateStaff(User user);
    void removeStaff(User staffID);
}


