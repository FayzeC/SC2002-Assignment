package appointmentsystem;

import filemanager.CSVDataLoader;
import filemanager.CSVUpdater;
import filemanager.FilePaths;
import roles.Doctor;
import roles.InformationAccess;
import roles.Patient;
import roles.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * The DoctorInformationAccess class provides doctors with access to view and update
 * the medical records of patients assigned to them. This class implements the {@link InformationAccess} interface
 * to enforce access control and allow only doctors to view and modify records.
 */
public class DoctorInformationAccess implements InformationAccess {

    /**
     * Displays the medical records of patients assigned to the specified doctor.
     * Only accessible by users with the role of Doctor.
     *
     * @param user The user requesting access to patient records.
     */
    @Override
    public void viewMedicalRecords(User user) {
        if (!(user instanceof Doctor)) {
            System.out.println("Access denied. Only doctors can view patient records.");
            return;
        }

        Doctor doctor = (Doctor) user;

        try {
            List<Patient> allPatients = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);
            Map<String, List<Patient>> patientRecordsMap = new LinkedHashMap<>();

            // Group all records for each patient ID, regardless of doctor assignment
            for (Patient patient : allPatients) {
                patientRecordsMap.computeIfAbsent(patient.getHospitalID(), k -> new ArrayList<>()).add(patient);
            }

            boolean anyRecordsDisplayed = false;

            System.out.println("\n+======= Medical Records for Patients Assigned to Doctor ID: " + doctor.getHospitalID() + " =======+");
            for (Map.Entry<String, List<Patient>> entry : patientRecordsMap.entrySet()) {
                String patientId = entry.getKey();
                List<Patient> records = entry.getValue();

                // Check if the current doctor is assigned to any record of this patient
                boolean isAssignedToCurrentDoctor = records.stream()
                        .anyMatch(record -> record.getDoctorAssigned().equals(doctor.getHospitalID()));

                // Only display if the patient has at least one record with the current doctor
                if (isAssignedToCurrentDoctor) {
                    anyRecordsDisplayed = true;
                    Patient mainRecord = records.get(0);
                    System.out.println("\nPatient ID: " + mainRecord.getHospitalID() + ", Name: " + mainRecord.getName() + "\n" +
                            "Date of Birth: " + mainRecord.getDoB() + ", Gender: " + mainRecord.getGender() + "\n" +
                            "Blood Type: " + mainRecord.getBloodType() + ", Contact: " + mainRecord.getEmail());
                    System.out.println("---------- Medical History ----------");
                    for (Patient record : records) {
                        System.out.println("Doctor ID: " + record.getDoctorAssigned() + "\n" +
                                "Past Diagnosis: " + record.getPastDiagnosis() + "\n" +
                                "Past Treatment: " + record.getPastTreatment() + "\n" +
                                "------------------------------------");
                    }
                }
            }

            if (!anyRecordsDisplayed) {
                System.out.println("No medical records found for patients assigned to Doctor ID: " + doctor.getHospitalID());
            }
        } catch (IOException e) {
            System.err.println("Error loading patient medical records: " + e.getMessage());
        }
    }

    /**
     * Allows the doctor to update the medical records of a patient assigned to them.
     * The doctor can input new diagnosis and treatment information, which is then appended
     * to the patient's record in the CSV file.
     *
     * @param user The user attempting to update patient information.
     * @throws IOException if an I/O error occurs while accessing the CSV file.
     */
    @Override
    public void updatePersonalInformation(User user) throws IOException {
        if (!(user instanceof Doctor)) {
            System.out.println("Access denied. Only doctors can update patient records.");
            return;
        }

        Doctor doctor = (Doctor) user;
        List<Patient> patients = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n+======= Patients Assigned to Doctor ID: " + doctor.getHospitalID() + " =======+");
        Set<String> displayedPatientIDs = new HashSet<>();
        boolean hasPatientsAssigned = false;

        for (Patient patient : patients) {
            if (patient.getDoctorAssigned().equals(doctor.getHospitalID()) && !displayedPatientIDs.contains(patient.getHospitalID())) {
                hasPatientsAssigned = true;
                System.out.println("Patient ID: " + patient.getHospitalID() + ", Name: " + patient.getName());
                displayedPatientIDs.add(patient.getHospitalID());
            }
        }

        if (!hasPatientsAssigned) {
            System.out.println("No patients assigned to Doctor ID: " + doctor.getHospitalID());
            return;
        }

        System.out.print("\nEnter the Patient ID to update records: ");
        String patientId = scanner.nextLine();

        if (!patientExists(patientId)) {
            System.out.println("Error: Patient ID " + patientId + " does not exist.");
            return;
        }

        if (!isAssignedToDoctor(patientId, doctor.getHospitalID())) {
            System.out.println("Error: Patient ID " + patientId + " is not assigned to you.");
            return;
        }

        System.out.print("Enter New Diagnosis: ");
        String newDiagnosis = scanner.nextLine();
        System.out.print("Enter New Treatment: ");
        String newTreatment = scanner.nextLine();

        addOrUpdatePatientRecord(patientId, doctor.getHospitalID(), newDiagnosis, newTreatment);
        System.out.println("Medical records for Patient ID " + patientId + " have been updated successfully.");
    }

    /**
     * Checks whether a patient ID exists in the CSV file.
     *
     * @param patientId The patient ID to validate.
     * @return true if the patient ID exists; false otherwise.
     */
    private boolean patientExists(String patientId) {
        try {
            List<Patient> allPatients = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);
            for (Patient patient : allPatients) {
                if (patient.getHospitalID().equals(patientId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error validating patient existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks whether a patient is assigned to the specified doctor.
     *
     * @param patientId The patient ID to validate.
     * @param doctorId  The doctor ID to check against.
     * @return true if the patient is assigned to the doctor; false otherwise.
     */
    private boolean isAssignedToDoctor(String patientId, String doctorId) {
        try {
            List<Patient> allPatients = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);
            for (Patient patient : allPatients) {
                if (patient.getHospitalID().equals(patientId) && patient.getDoctorAssigned().equals(doctorId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking doctor assignment: " + e.getMessage());
        }
        return false;
    }

    /**
     * Adds a new patient record or updates an existing record for a patient with new diagnosis and
     * treatment information. If the existing diagnosis or treatment is marked as "NA," the record is updated
     * directly; otherwise, a new entry is appended to the CSV file.
     *
     * @param patientId     The ID of the patient whose records are being updated.
     * @param doctorId      The ID of the doctor updating the records.
     * @param newDiagnosis  The new diagnosis for the patient.
     * @param newTreatment  The new treatment for the patient.
     * @throws IOException if an I/O error occurs while writing to the CSV file.
     */
    private void addOrUpdatePatientRecord(String patientId, String doctorId, String newDiagnosis, String newTreatment) throws IOException {
        List<Patient> patients = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);

        for (Patient patient : patients) {
            if (patient.getHospitalID().equals(patientId) && patient.getDoctorAssigned().equals(doctorId)) {
                String currentDiagnosis = patient.getPastDiagnosis();
                String currentTreatment = patient.getPastTreatment();
                if ("NA".equalsIgnoreCase(currentDiagnosis) || "NA".equalsIgnoreCase(currentTreatment)) {
                    CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, patientId, doctorId, "Past Diagnosis", newDiagnosis, 0, 7);
                    CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, patientId, doctorId, "Past Treatment", newTreatment, 0, 7);
                } else {
                    try (FileWriter fw = new FileWriter(FilePaths.PATIENT_LIST_PATH, true); PrintWriter pw = new PrintWriter(fw)) {
                        pw.println(String.join(",", patientId, patient.getName(), patient.getPassword(),
                                patient.getDoB(), patient.getGender(), patient.getBloodType(),
                                patient.getEmail(), doctorId, newDiagnosis, newTreatment, (!patient.getFirstLogin()) ? "No" : "Yes"));
                    }
                }
                break;
            }
        }
    }
}
