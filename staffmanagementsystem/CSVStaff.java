package staffmanagementsystem;

import roles.Administrator;
import roles.Doctor;
import roles.Pharmacist;
import roles.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CSVStaff} class provides functionality to load and save staff data
 * from and to a CSV file. It implements the {@link StaffRepository} interface.
 */
public class CSVStaff implements StaffRepository {

    private final String filePath;  // Store the file path for the CSV

    /**
     * Constructs a {@code CSVStaff} instance with the specified file path.
     *
     * @param filePath the path to the CSV file for staff data storage.
     */
    public CSVStaff(String filePath) {
        this.filePath = filePath;  // Set the file path
    }

    /**
     * Loads all staff data from the CSV file.
     *
     * @return a list of {@link User} objects representing the staff members.
     * @throws IOException if an error occurs while reading the file.
     */
    @Override
    public List<User> loadAllStaff() throws IOException {
        List<User> staffList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            int validRows = 0;  // Counter for valid rows
            int skippedRows = 0; // Counter for malformed rows

            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;  // Skip the header row
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length < 7) {
                    skippedRows++;
                    continue; // Skip malformed lines
                }

                String hospitalID = fields[0].trim();
                String name = fields[1].trim();
                String password = fields[2].trim();
                String role = fields[3].trim();
                String gender = fields[4].trim();
                String age = fields[5].trim();
                boolean firstLogin = "Yes".equalsIgnoreCase(fields[6].trim());

                // Remove "basic." from the role if present
                if (role != null && role.startsWith("basic.")) {
                    role = role.substring(6);  // Remove the "basic." prefix
                }

                // Create a new User based on role
                User staff = null;
                if (role.equalsIgnoreCase("Doctor")) {
                    staff = new Doctor(hospitalID, name, password, role, gender, age, firstLogin);
                } else if (role.equalsIgnoreCase("Pharmacist")) {
                    staff = new Pharmacist(hospitalID, name, password, role, gender, age, firstLogin);
                } else if (role.equalsIgnoreCase("Administrator")) {
                    staff = new Administrator(hospitalID, name, password, role, gender, age, firstLogin);
                } else {
                    skippedRows++;
                    System.out.println("Unknown role, skipping: " + role);
                    continue; // Skip unknown roles
                }

                staffList.add(staff);
                validRows++;  // Increment valid rows counter
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return staffList;
    }

    /**
     * Saves all staff data to the CSV file.
     *
     * @param staffList the list of {@link User} objects representing the staff members to save.
     * @throws IOException if an error occurs while writing to the file.
     */
    @Override
    public void saveAllStaff(List<User> staffList) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header row
            writer.write("HospitalID,Name,Password,Role,Gender,Age,FirstLogin");
            writer.newLine();

            // Write staff data
            for (User staff : staffList) {
                String age = "";  // Default value for age
                if (staff instanceof Doctor) {
                    age = ((Doctor) staff).getAge();  // Access age for Doctor
                } else if (staff instanceof Pharmacist) {
                    age = ((Pharmacist) staff).getAge();  // Access age for Pharmacist
                } else if (staff instanceof Administrator) {
                    age = ((Administrator) staff).getAge();  // Access age for Administrator
                }

                writer.write(staff.getHospitalID() + "," + staff.getName() + "," + staff.getPassword() + ","
                        + staff.getRole() + "," + staff.getGender() + "," + age + ","
                        + (staff.getFirstLogin() ? "Yes" : "No"));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV: " + e.getMessage());
        }
    }
}
