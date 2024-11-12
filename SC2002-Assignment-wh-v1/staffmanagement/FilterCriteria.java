package staffmanagement;

public class FilterCriteria {
    private String role;
    private String gender;
    private Integer age;

    public FilterCriteria(String role, String gender, Integer age) {
        this.role = role;
        this.gender = gender;
        this.age = age;
    }

    public String getRole() { return role; }
    public String getGender() { return gender; }
    public Integer getAge() { return age; }
}

