import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadStaff extends DataLoader {
    private List<Doctor> doctorList = new ArrayList<>();
    private List<Pharmacist> pharmacistList = new ArrayList<>();
    private List<Administrator> administratorList = new ArrayList<>();

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

                if (fields.length < 5) {
                    System.err.println("Skipping invalid line: " + line);
                    continue; // Skip this line if it doesn't have enough fields
                }

                // Assign each field to the appropriate variable
                String staffID = fields[0].trim();
                String name = fields[1].trim();
                String role = fields[2].trim();
                String gender = fields[3].trim();
                int age = Integer.parseInt(fields[4].trim());

                // Check the prefix of staffID and create appropriate object
                if (staffID.startsWith("D")) {
                    Doctor doctor = new Doctor(staffID, name, role, gender, age);
                    doctorList.add(doctor);
                } else if (staffID.startsWith("P")) {
                    Pharmacist pharmacist = new Pharmacist(staffID, name, role, gender, age);
                    pharmacistList.add(pharmacist);
                } else if (staffID.startsWith("A")) {
                    Administrator administrator = new Administrator(staffID, name, role, gender, age);
                    administratorList.add(administrator);
                } else {
                    System.err.println("Unknown staff ID prefix, skipping: " + staffID);
                }
            }

            System.out.println("Staff data loaded successfully.");

        } catch (IOException e) {
            System.err.println("Error loading staff data: " + e.getMessage());
        }
    }

    // Getter methods for each list
    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public List<Pharmacist> getPharmacistList() {
        return pharmacistList;
    }

    public List<Administrator> getAdministratorList() {
        return administratorList;
    }
}
