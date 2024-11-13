package AppointmentSystem;

import basic.CSVUpdater;
import basic.FilePaths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

            // Update or add Doctor ID for the patient in patient_list.csv
            updateOrAddDoctorForPatient(appointmentId.trim(), doctorId.trim());
        } else if (!isAppointmentSelectedByPatient(appointmentId.trim())) {
            System.out.println("Error: No patient has selected Appointment ID " + appointmentId + ".");
        } else {
            System.out.println("Error: You (" + doctorId + ") are not assigned to Appointment ID " + appointmentId + ".");
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
        } else if (!isAppointmentSelectedByPatient(appointmentId.trim())) {
            System.out.println("Error: No patient has selected Appointment ID " + appointmentId + ".");
        } else {
            System.out.println("Error: You (" + doctorId + ") are not assigned to Appointment ID " + appointmentId + ".");
        }
    }

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

    // New method to check if an appointment is selected by any patient
    private boolean isAppointmentSelectedByPatient(String appointmentId) {
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

                if (cellAppointmentId.equalsIgnoreCase(appointmentId) && !cellPatientId.isEmpty()) {
                    return true; // Appointment is selected by a patient
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking if appointment is selected by patient: " + e.getMessage());
        }
        return false;
    }

    private void updateAppointmentStatus(String appointmentId, String newStatus) {
        try {
            CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, null, "status", newStatus, 0, 0);
        } catch (IOException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }
    }

    private void updateOrAddDoctorForPatient(String appointmentId, String doctorId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;
            String patientId = null;
            String status = null;

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

            if (patientId != null && "CONFIRMED".equalsIgnoreCase(status)) {
                if (isDoctorAlreadyAssigned(patientId, doctorId)) {
                    System.out.println("Doctor ID " + doctorId + " is already assigned to Patient ID " + patientId + ". No update needed.");
                } else {
                    // Check if the current doctor assignment is "NA"
                    if (isCurrentDoctorNA(patientId)) {
                        // If current doctor is "NA", directly update the record with the new doctor ID
                        CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, patientId, null, "Doctor Assigned", doctorId, 0, 0);
                        System.out.println("Updated Doctor ID for Patient ID " + patientId + " to " + doctorId + " in patient_list.csv.");
                    } else {
                        // Otherwise, add a new patient record for the additional doctor assignment
                        addNewPatientRecord(patientId, doctorId);
                        System.out.println("Added new record for Patient ID " + patientId + " with Doctor ID " + doctorId + " in patient_list.csv.");
                    }
                }
            } else {
                System.out.println("Error: Patient ID not found or appointment status is not CONFIRMED for Appointment ID " + appointmentId);
            }
        } catch (IOException e) {
            System.err.println("Error updating Doctor ID for patient: " + e.getMessage());
        }
    }

    private boolean isCurrentDoctorNA(String patientId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.PATIENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String cellPatientId = fields[0].trim();
                String cellDoctorAssigned = fields[7].trim();

                if (cellPatientId.equalsIgnoreCase(patientId) && "NA".equalsIgnoreCase(cellDoctorAssigned)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking doctor assignment for patient: " + e.getMessage());
        }
        return false;
    }

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
                String cellPatientId = fields[0].trim();
                String cellDoctorAssigned = fields[7].trim();

                if (cellPatientId.equalsIgnoreCase(patientId) && cellDoctorAssigned.equalsIgnoreCase(doctorId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking existing doctor assignment for patient: " + e.getMessage());
        }
        return false;
    }

    private void addNewPatientRecord(String patientId, String doctorId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.PATIENT_LIST_PATH));
             FileWriter fw = new FileWriter(FilePaths.PATIENT_LIST_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String line;
            boolean isFirstRow = true;
            String[] patientData = null;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields[0].trim().equalsIgnoreCase(patientId)) {
                    patientData = fields;
                    break;
                }
            }

            if (patientData != null) {
                pw.println(String.join(",", patientId, patientData[1], patientData[2], patientData[3], patientData[4],
                        patientData[5], patientData[6], doctorId, "NA", "NA", patientData[10]));
            }
        } catch (IOException e) {
            System.err.println("Error adding new patient record for Doctor ID " + doctorId + ": " + e.getMessage());
        }
    }
}
