package AppointmentSystem;

import basic.CSVUpdater;
import basic.FilePaths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DoctorAppointmentManager extends DoctorScheduleView implements doctorAppointmentManagement {

    @Override
    public void acceptAppointment(String doctorId, String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            System.out.println("Invalid appointment ID.");
            return;
        }
        if (isAppointmentAssignedToDoctor(doctorId.trim(), appointmentId.trim())) {
            updateAppointmentStatus(appointmentId.trim(), "CONFIRMED");
            System.out.println("Appointment ID " + appointmentId + " has been accepted.");

            // Update Doctor ID for the patient in patient_list.csv
            updateDoctorForPatient(appointmentId.trim(), doctorId.trim());
        } else {
            System.out.println("Error: Appointment ID " + appointmentId + " is not assigned to Doctor ID " + doctorId);
        }
    }

    @Override
    public void declineAppointment(String doctorId, String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            System.out.println("Invalid appointment ID.");
            return;
        }
        if (isAppointmentAssignedToDoctor(doctorId.trim(), appointmentId.trim())) {
            updateAppointmentStatus(appointmentId.trim(), "CANCELLED");
            System.out.println("Appointment ID " + appointmentId + " has been declined.");
        } else {
            System.out.println("Error: Appointment ID " + appointmentId + " is not assigned to Doctor ID " + doctorId);
        }
    }

    // Method to verify if an appointment is assigned to a specific doctor
    private boolean isAppointmentAssignedToDoctor(String doctorId, String appointmentId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String cellAppointmentId = fields[0].trim();
                String cellPatientId = fields[3].trim();
                String cellDoctorId = fields[5].trim();

                // Check if both appointment ID and doctor ID match and patient ID is not empty
                if (cellAppointmentId.equalsIgnoreCase(appointmentId) && cellDoctorId.equalsIgnoreCase(doctorId)
                        && !cellPatientId.isEmpty()) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error verifying appointment assignment: " + e.getMessage());
        }
        return false;
    }

    // Update the status of the appointment if assigned to the doctor
    private void updateAppointmentStatus(String appointmentId, String newStatus) {
        try {
            CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, "status", newStatus);
        } catch (IOException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }
    }

    // Update Doctor ID for the patient in patient_list.csv based on the appointment information
    private void updateDoctorForPatient(String appointmentId, String doctorId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;
            String patientId = null;
            String status = null;

            // Locate the patient ID from the appointment record
            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(appointmentId)) {
                    patientId = fields[3].trim();
                    status = fields[7].trim();
                    break;
                }
            }

            // If patient ID and confirmed status are found, update Doctor ID in patient_list.csv only if necessary
            if (patientId != null && "CONFIRMED".equalsIgnoreCase(status)) {
                if (!isDoctorAlreadyAssigned(patientId, doctorId)) { // Check for existing assignment
                    CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, patientId, "Doctor Assigned", doctorId);
                    System.out.println("Updated Doctor ID for Patient ID " + patientId + " to " + doctorId + " in patient_list.csv.");
                } else {
                    System.out.println("Doctor ID " + doctorId + " is already assigned to Patient ID " + patientId + ". No update needed.");
                }
            } else {
                System.out.println("Error: Patient ID not found or appointment status is not CONFIRMED for Appointment ID " + appointmentId);
            }
        } catch (IOException e) {
            System.err.println("Error updating Doctor ID for patient: " + e.getMessage());
        }
    }

    // Helper method to check if a doctor is already assigned to a patient
    private boolean isDoctorAlreadyAssigned(String patientId, String doctorId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.PATIENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String cellPatientId = fields[0].trim(); // Assuming Patient ID is in the first column
                String cellDoctorAssigned = fields[8].trim(); // Assuming Doctor Assigned is in the ninth column

                // Check if the patient already has the specified doctor assigned
                if (cellPatientId.equalsIgnoreCase(patientId) && cellDoctorAssigned.equalsIgnoreCase(doctorId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking existing doctor assignment for patient: " + e.getMessage());
        }
        return false;
    }
}
