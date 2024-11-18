package appointmentoutcomerecordsystem;

import filemanager.FilePaths;
import java.io.IOException;
import java.util.List;

/**
 * The {@code PatientAORView} class allows patients to view their appointment outcome records.
 * It provides functionality to load and display records specific to a patient from the CSV file.
 *
 * This class implements the {@code viewOutcomeRecord} interface to enable patients to:
 * <ul>
 *     <li>Load all appointment outcome records from a file.</li>
 *     <li>Filter and display records based on the provided patient ID.</li>
 * </ul>
 *
 * @see filemanager.FilePaths
 * @see AppointmentOutcomeRecord
 * @see CSVAppointmentOutcomeRecord
 */
public class PatientAORView implements viewOutcomeRecord {

    /**
     * Displays all appointment outcome records associated with the given patient ID.
     *
     * <p>
     * The method retrieves appointment outcome records from the CSV file specified in
     * {@link FilePaths#APPOINTMENT_OUTCOME_RECORD_PATH}, filters them by the given patient ID,
     * and displays the matching records in a formatted manner.
     * </p>
     *
     * <p>
     * If no matching records are found, the method notifies the user.
     * </p>
     *
     * @param patientID the unique identifier of the patient whose records need to be displayed
     */
    @Override
    public void viewAppointmentOutcomeRecord(String patientID) {
        try {
            // Load all appointment outcome records from CSV
            List<AppointmentOutcomeRecord> outcomeRecords = CSVAppointmentOutcomeRecord.loadApptOutcomeRecord(FilePaths.APPOINTMENT_OUTCOME_RECORD_PATH);

            // Display only records that match the patient ID
            System.out.println("\n+======= Appointment Outcome Records for Patient ID: " + patientID + " =======+\n");
            boolean recordFound = false;
            int counter = 0;
            for (AppointmentOutcomeRecord record : outcomeRecords) {
                if (record.getPatientID().equalsIgnoreCase(patientID)) {
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
                System.out.println("No records found for Patient ID: " + patientID);
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
