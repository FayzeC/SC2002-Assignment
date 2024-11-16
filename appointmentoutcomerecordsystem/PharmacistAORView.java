package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import java.io.IOException;
import java.util.List;

public class PharmacistAORView implements viewOutcomeRecord {

    @Override
    public void viewAppointmentOutcomeRecord(String status) {
        try {
            // Load all appointment outcome records from CSV
            List<AppointmentOutcomeRecord> outcomeRecords = CSVAppointmentOutcomeRecord.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);

            // Display only records that match the status
            System.out.println("\n+======= Appointment Outcome Records to be Approved =======+\n");
            boolean recordFound = false;
            int counter = 0;
            for (AppointmentOutcomeRecord record : outcomeRecords) {
                if (record.getStatus().equalsIgnoreCase(status)) {
                    counter++;
                    System.out.println("--------------- Appointment Outcome Record: " + counter + " ----------------");
                    System.out.println("Appointment ID: " + record.getAppointmentID());
                    System.out.println("Doctor Assigned: " + record.getDoctorAssigned());
                    System.out.println("Appointment Date: " + record.getAppointmentDate());
                    System.out.println("Service: " + record.getService());
                    System.out.println("Medication: " + record.getMedication());
                    System.out.println("Quantity: " + record.getQuantity());
                    System.out.println("Status: " + record.getStatus());
                    System.out.println("Consultation Notes: " + record.getConsultationNotes());
                    System.out.println("--------------------------------------------------------------\n");
                    recordFound = true;
                }
            }

            if (!recordFound) {
                System.out.println("No records found to be pending.");
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
