import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadPatient extends DataLoader {
    private List<Patient> patientList = new ArrayList<>();

    @Override
    public void importFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header row if there is one
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Split line into fields assuming comma-separated values
                String[] fields = line.split(",");

                if (fields.length < 6) {
                    System.err.println("Skipping invalid line: " + line);
                    continue; // Skip this line if it doesn't have enough fields
                }

                // Assign each field to the appropriate variable
                String patientID = fields[0].trim();
                String name = fields[1].trim();
                String dob = fields[2].trim();
                String gender = fields[3].trim();
                String bloodType = fields[4].trim();
                String email = fields[5].trim();

                // Create a new Patient object and add it to the list
                ConcretePatient patient = new ConcretePatient(patientID, name, "Patient", dob, gender, bloodType, email);
                // Set itself as the updater
                patient.setPersonalInfoUpdater(patient);
                patient.setViewMedicalRecords(patient);
                patientList.add(patient);
            }

            System.out.println("Patient data loaded successfully.");

        } catch (IOException e) {
            System.err.println("Error loading patient data: " + e.getMessage());
        }
    }

    public List<Patient> getPatientList() {
        return patientList;
    }
}
