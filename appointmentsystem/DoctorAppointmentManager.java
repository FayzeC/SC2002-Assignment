package appointmentsystem;

import filemanager.CSVUpdater;
import filemanager.FilePaths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The DoctorAppointmentManager class manages appointment actions for doctors,
 * such as accepting or declining appointments. It updates CSV files accordingly and
 * maintains doctor-patient assignment records.
 * This class extends {@link DoctorScheduleView} and implements {@link doctorAppointmentManagement}.
 *
 * Dependencies:
 * - {@link CSVUpdater} for updating CSV files
 * - {@link FilePaths} for file path configurations
 */
public class DoctorAppointmentManager extends DoctorScheduleView implements doctorAppointmentManagement {

    /**
     * Accepts an appointment for the specified doctor, updates the appointment status
     * to "CONFIRMED," and associates the doctor with the patient in the records.
     *
     * @param doctorId The ID of the doctor accepting the appointment.
     * @param appointmentId The ID of the appointment to accept.
     */
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

    /**
     * Declines an appointment for the specified doctor, updates the appointment status
     * to "CANCELLED," and maintains the patient-doctor association records.
     *
     * @param doctorId The ID of the doctor declining the appointment.
     * @param appointmentId The ID of the appointment to decline.
     */
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

    /**
     * Checks if an appointment is assigned to a specific doctor.
     *
     * @param doctorId The ID of the doctor.
     * @param appointmentId The ID of the appointment.
     * @return true if the appointment is assigned to the specified doctor, false otherwise.
     */
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

    /**
     * Checks if an appointment is selected by any patient.
     *
     * @param appointmentId The ID of the appointment.
     * @return true if a patient has selected the appointment, false otherwise.
     */
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

    /**
     * Updates the status of an appointment to the specified new status.
     *
     * @param appointmentId The ID of the appointment.
     * @param newStatus The new status to set (e.g., "CONFIRMED" or "CANCELLED").
     */
    private void updateAppointmentStatus(String appointmentId, String newStatus) {
        try {
            CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, null, "status", newStatus, 0, 0);
        } catch (IOException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }
    }

    /**
     * Updates or adds the doctor ID for the patient associated with the specified appointment.
     *
     * @param appointmentId The ID of the appointment.
     * @param doctorId The ID of the doctor to assign.
     */
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

    /**
     * Checks if the current doctor assigned to the patient is marked as "NA."
     *
     * @param patientId The ID of the patient.
     * @return true if the doctor assignment is "NA," false otherwise.
     */
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

    /**
     * Checks if the specified doctor is already assigned to the specified patient.
     *
     * @param patientId The ID of the patient.
     * @param doctorId The ID of the doctor.
     * @return true if the doctor is already assigned to the patient, false otherwise.
     */
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

    /**
     * Adds a new record for a patient with the specified doctor ID, if the patient has
     * multiple doctors assigned.
     *
     * @param patientId The ID of the patient.
     * @param doctorId The ID of the doctor to add.
     */
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
