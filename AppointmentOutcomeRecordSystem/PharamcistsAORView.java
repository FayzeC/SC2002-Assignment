package AppointmentOutcomeRecordSystem;

import basic.AppointmentOutcomeRecord;
import basic.CSVDataLoader;
import basic.FilePaths;
import java.io.IOException;
import java.util.List;

public class PharamcistsAORView implements viewOutcomeRecord {
    @Override
    public void viewAppointmentOutcomeRecord(String status) {
        try {
            // Load all appointment outcome records from the CSV file
            List<AppointmentOutcomeRecord> records = CSVDataLoader.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);

            System.out.println("Pending Appointment Outcome Records:");
            System.out.println("Appointment ID | Patient ID | Doctor Assigned | Appointment Date | Service | Medication | Status | Quantity | Consultation Notes");

            // Filter and display only records with "pending" status
            for (AppointmentOutcomeRecord record : records) {
                if ("pending".equalsIgnoreCase(record.getStatus())) {
                    System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s | %s%n",
                            record.getAppointmentID(),
                            record.getPatientID(),
                            record.getDoctorAssigned(),
                            record.getAppointmentDate(),
                            record.getService(),
                            record.getMedication(),
                            record.getStatus(),
                            record.getQuantity(),
                            record.getConsultationNotes());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointment outcome records: " + e.getMessage());
        }
    }
}
