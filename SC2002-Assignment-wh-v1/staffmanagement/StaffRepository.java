package sc2002.hospital.staffmanagement;

import sc2002.hospital.login.Staff;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaffRepository implements StaffManager {
    private List<Staff> staffList = new ArrayList<>();

    @Override
    public List<StaffDetailsDTO> getStaffListByFilter(FilterCriteria criteria) {
        // Filters staff and maps each result to StaffDetailsDTO
        return staffList.stream()
                .filter(staff -> 
                    (criteria.getRole() == null || staff.getRoleType().toString().equalsIgnoreCase(criteria.getRole())) &&
                    (criteria.getGender() == null || staff.getGender().equalsIgnoreCase(criteria.getGender())) &&
                    (criteria.getAge() == null || staff.getAge() == criteria.getAge().intValue())


                )
                .map(staff -> new StaffDetailsDTO(
                    staff.getId(),
                    staff.getName(),
                    staff.getRoleType(),
                    staff.getGender(),
                    staff.getAge()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void addStaff(Staff staff) {
        staffList.add(staff);
    }

    @Override
    public void updateStaff(Staff staff) {
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getId().equals(staff.getId())) {
                staffList.set(i, staff);
                return;
            }
        }
    }

    @Override
    public void removeStaff(String id) {
        staffList.removeIf(staff -> staff.getId().equals(id));
    }
}