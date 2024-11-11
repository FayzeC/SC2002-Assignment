package org.example;
import java.io.IOException;

public interface InformationAccess {
    void viewMedicalRecords (User user) throws IOException;
    void updatePersonalInformation(User user) throws IOException;
}
