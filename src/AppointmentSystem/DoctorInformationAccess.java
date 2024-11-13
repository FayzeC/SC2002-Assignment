package AppointmentSystem;

import basic.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class DoctorInformationAccess implements InformationAccess {

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
                                "Past Diagnosis: " + record.getPastDiagnosis() + "\n"+
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
        Patient targetPatient = null;

        for (Patient patient : patients) {
            if (patient.getHospitalID().equals(patientId) && patient.getDoctorAssigned().equals(doctor.getHospitalID())) {
                targetPatient = patient;
                break;
            }
        }

        if (targetPatient == null) {
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

    private void addOrUpdatePatientRecord(String patientId, String doctorId, String newDiagnosis, String newTreatment) throws IOException {
        List<Patient> patients = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);
        boolean recordUpdated = false;

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
                recordUpdated = true;
                break;
            }
        }
    }
}
