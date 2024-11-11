import java.io.IOException;

public interface InformationAccess {
    void viewMedicalRecords (User user);
    void updatePersonalInformation(User user) throws IOException;
}
