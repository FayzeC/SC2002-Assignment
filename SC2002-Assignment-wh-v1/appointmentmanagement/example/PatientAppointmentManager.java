package appointmentmanagement.example;

import login.example.CSVUpdater;
import login.example.FilePaths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PatientAppointmentManager implements patientAppointmentManagement {

    @Override
    public void scheduleAppointment(String patientId, String patientName, String appointmentId) {
        boolean appointmentExists = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[0].equals(appointmentId)) {
                    appointmentExists = true;

                    if (fields[7].equalsIgnoreCase("UNRESERVED")) {
                        // Update the CSV file with patient details and mark as pending confirmation
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, "Patient ID", patientId);
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, "Patient Name", patientName);
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, "Status", "PENDING");
                        System.out.println("Appointment " + appointmentId + " is pending confirmation from the doctor.");
                    } else {
                        System.out.println("Error: Appointment ID " + appointmentId + " is already reserved.");
                    }
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
        }

        if (!appointmentExists) {
            System.out.println("Error: Appointment ID " + appointmentId + " does not exist.");
        }
    }

    @Override
    public void cancelAppointment(String appointmentId, String patientId) {
        boolean appointmentExists = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[0].equals(appointmentId)) {
                    appointmentExists = true;

                    // Check if the patient ID matches
                    if (!fields[3].equals(patientId)) {
                        System.out.println("Error: You cannot cancel appointment ID " + appointmentId + " as it is not booked by you.");
                        return;
                    }

                    // Check if the appointment status allows cancellation
                    if (fields[7].equalsIgnoreCase("PENDING") || fields[7].equalsIgnoreCase("CONFIRMED")) {
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, "Patient ID", "");
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, "Patient Name", "");
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, "Status", "UNRESERVED");
                        System.out.println("Appointment " + appointmentId + " has been cancelled.");
                    } else {
                        System.out.println("Error: Appointment ID " + appointmentId + " cannot be canceled because it is unreserved.");
                    }
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error canceling appointment: " + e.getMessage());
        }

        if (!appointmentExists) {
            System.out.println("Error: Appointment ID " + appointmentId + " does not exist.");
        }
    }

    @Override
    public void rescheduleAppointment(String patientId, String patientName, String appointmentId, String newAppointmentId) throws IOException {
        if (!isAppointmentOwnedByPatient(appointmentId, patientId)) {
            System.out.println("Rescheduling failed: You cannot reschedule appointment ID " + appointmentId + " as it is not booked by you.");
            return;
        }

        cancelAppointment(appointmentId, patientId);
        scheduleAppointment(patientId, patientName, newAppointmentId);

        if (isAppointmentScheduled(newAppointmentId)) {
            System.out.println("Appointment rescheduled successfully to " + newAppointmentId);
        } else {
            System.out.println("Failed to reschedule: the requested slot is unavailable.");
        }
    }

    // Method to verify if an appointment is owned by a specific patient
    private boolean isAppointmentOwnedByPatient(String appointmentId, String patientId) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[0].equals(appointmentId) && fields[3].equals(patientId)) {
                    return true; // The appointment is booked by the specified patient
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking appointment ownership: " + e.getMessage());
        }
        return false;
    }

    public boolean isAppointmentScheduled(String appointmentId) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[0].equals(appointmentId) && !fields[7].equalsIgnoreCase("UNRESERVED")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking appointment status: " + e.getMessage());
        }

        return false;
    }
}