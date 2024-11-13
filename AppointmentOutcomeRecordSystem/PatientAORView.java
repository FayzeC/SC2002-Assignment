package AppointmentOutcomeRecordSystem;

import basic.AppointmentOutcomeRecord;
import basic.CSVDataLoader;
import basic.FilePaths;
import java.io.IOException;
import java.util.List;

public class PatientAORView implements viewOutcomeRecord{
    // View appointment outcome records for this specific patient
    @Override
    public void viewAppointmentOutcomeRecord(String patientID) {
        try {
            // Load all appointment outcome records from CSV
            List<AppointmentOutcomeRecord> outcomeRecords = CSVDataLoader.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);

            // Display only records that match the patient ID
            System.out.println("\n+======= Appointment Outcome Records for Patient ID: " + patientID + " =======+\n");
            boolean recordFound = false;
            int counter = 0;
            for (AppointmentOutcomeRecord record : outcomeRecords) {
                if (record.getPatientID().equalsIgnoreCase(patientID)) {
                    counter++;
                    System.out.println("Appointment Outcome Record: " + counter);
                    System.out.println("--------------------------------------------------");
                    System.out.println("Appointment ID: " + record.getAppointmentID());
                    System.out.println("Doctor Assigned: " + record.getDoctorAssigned());
                    System.out.println("Appointment Date: " + record.getAppointmentDate());
                    System.out.println("Service: " + record.getService());
                    System.out.println("Medication: " + record.getMedication());
                    System.out.println("Status: " + record.getStatus());
                    System.out.println("Quantity: " + record.getQuantity());
                    System.out.println("Consultation Notes: " + record.getConsultationNotes());
                    System.out.println("--------------------------------------------------");
                    recordFound = true;
                }
            }

            if (!recordFound) {
                System.out.println("No records found for Patient ID: " + patientID);
            }

            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }
    }
}
