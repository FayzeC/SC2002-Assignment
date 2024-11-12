package appointmentmanagement.example;

import login.example.*;
import role.example.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DoctorInformationAccess implements InformationAccess {
    private static final String PATIENT_LIST_PATH = FilePaths.PATIENT_LIST_PATH;

    // View medical records for patients assigned to a specific doctor
    @Override
    public void viewMedicalRecords(User user) {
        if (!(user instanceof Doctor)) {
            System.out.println("Access denied. Only doctors can view patient records.");
            return;
        }

        Doctor doctor = (Doctor) user;
        try {
            List<Patient> patients = CSVDataLoader.loadPatients(PATIENT_LIST_PATH);
            System.out.println("\n+======= Medical Records for Patients Assigned to Doctor ID: " + doctor.getHospitalID() + " =======+");

            for (Patient patient : patients) {
                if (patient.getDoctorAssigned().equals(doctor.getHospitalID())) {
                    if (patient instanceof ConcretePatient) {
                        ((ConcretePatient) patient).viewMedicalRecords(doctor);
                    } else {
                        System.out.println("Error: Patient data type does not support viewing medical records.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading patient medical records: " + e.getMessage());
        }
    }

    // Update medical records for a specific patient assigned to the doctor
    @Override
    public void updatePersonalInformation(User user) throws IOException {
    }
}