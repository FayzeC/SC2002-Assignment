package filemanager;

import roles.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CSVDataLoader class provides static methods to load various types of data
 * (e.g., staff, patients, appointments) from CSV files into appropriate data structures.
 * Each method reads data from a specified file path and returns a list of objects
 * based on the CSV content.
 */
public class CSVDataLoader {

    /**
     * Loads staff data (pharmacists, doctors, and administrators) from a CSV file.
     * The method processes each row, identifies the staff type, and creates
     * corresponding objects which are added to the appropriate list.
     *
     * @param filePath the file path to the CSV file containing staff data
     * @param pharmacists a list to store pharmacists
     * @param doctors a list to store doctors
     * @param administrators a list to store administrators
     * @return the updated list of pharmacists
     * @throws IOException if an error occurs while reading the file
     */
    public static List<Pharmacist> loadStaff(String filePath, List<Pharmacist> pharmacists, List<Doctor> doctors, List<Administrator> administrators) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String staffID = fields[0].trim();
                String name = fields[1].trim();
                String password = fields[2].trim();
                String role = fields[3].trim();
                String gender = fields[4].trim();
                String age = fields[5].trim();
                boolean firstLogin = "Yes".equalsIgnoreCase(fields[6].trim());

                // Assign staff to the appropriate list based on their ID
                if (staffID.startsWith("P")) {
                    pharmacists.add(new Pharmacist(staffID, name, password, role, gender, age, firstLogin));
                } else if (staffID.startsWith("D")) {
                    doctors.add(new Doctor(staffID, name, password, role, gender, age, firstLogin));
                } else if (staffID.startsWith("A")) {
                    administrators.add(new Administrator(staffID, name, password, role, gender, age, firstLogin));
                }
            }
        }
        return pharmacists; // Return the list of pharmacists, other lists are updated in place
    }

    /**
     * Loads patient data from a CSV file. Each row in the file represents a patient
     * and is used to create a corresponding `ConcretePatient` object, which is added
     * to the list of patients.
     *
     * @param filePath the file path to the CSV file containing patient data
     * @return a list of Patient objects
     * @throws IOException if an error occurs while reading the file
     */
    public static List<Patient> loadPatients(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String patientID = fields[0].trim();
                String name = fields[1].trim();
                String password = fields[2].trim();
                String dob = fields[3].trim();
                String gender = fields[4].trim();
                String bloodType = fields[5].trim();
                String email = fields[6].trim();
                String doctorAssigned = fields[7].trim();
                String pastDiagnosis = fields[8].trim();
                String pastTreatment = fields[9].trim();
                boolean firstLogin = "Yes".equalsIgnoreCase(fields[10].trim());

                // Create and add the ConcretePatient object
                ConcretePatient patient = new ConcretePatient(patientID, name, password, dob, gender, bloodType, email, doctorAssigned, pastDiagnosis, pastTreatment, firstLogin, "roles.Patient");
                patient.setInformationAccess(patient);
                patients.add(patient);
            }
        }
        return patients;
    }
}
