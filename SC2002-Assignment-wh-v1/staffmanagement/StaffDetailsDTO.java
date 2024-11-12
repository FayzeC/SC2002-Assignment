package staffmanagement;

public class StaffDetailsDTO {
    private final String id;
    private final String name;
    private final String gender;
    private final Integer age;

    public StaffDetailsDTO(String id, String name, String gender, Integer age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getGender() {return gender;}

    public Integer getAge() {return age;}
}

