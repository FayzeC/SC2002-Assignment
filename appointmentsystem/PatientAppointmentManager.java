package appointmentsystem;

import filemanager.CSVUpdater;
import filemanager.FilePaths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PatientAppointmentManager implements patientAppointmentManagement {

    @Override
    public void scheduleAppointment(String patientId, String patientName, String appointmentId) {
        boolean appointmentExists = false;
        String appointmentDate = getAppointmentDate(appointmentId);

        if (appointmentDate == null) {
            System.out.println("Error: Appointment ID " + appointmentId + " does not exist.");
            return;
        }

        // Check if the patient already has an appointment on the same day
        if (isAppointmentOnSameDay(patientId, appointmentDate)) {
            System.out.println("Error: You have already booked an appointment on " + appointmentDate + ". Only one slot per day is allowed.");
            return;
        }

        // Proceed to book the appointment if it's available
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
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, null, "Patient ID", patientId, 0, 0);
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, null, "Patient Name", patientName, 0, 0);
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, null, "Status", "PENDING", 0, 0);
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
        String doctorAssigned = null;

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

                    // Retrieve the doctor assigned to this appointment
                    doctorAssigned = fields[5].trim();

                    // Check if the appointment status allows cancellation
                    if (fields[7].equalsIgnoreCase("PENDING") || fields[7].equalsIgnoreCase("CONFIRMED")) {
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId,null, "Patient ID", "", 0, 0);
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, null, "Patient Name", "", 0, 0);
                        CSVUpdater.updater(FilePaths.APPOINTMENT_LIST_PATH, appointmentId, null, "Status", "UNRESERVED", 0, 0);

                        if (doctorAssigned != null && !hasOtherAppointmentsWithDoctor(patientId, doctorAssigned)) {
                            CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, patientId, null,"Doctor Assigned", "", 0, 0);
                        }

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

        // Check if the new appointment slot is available
        if (isAppointmentScheduled(newAppointmentId)) {
            System.out.println("Rescheduling failed: The requested slot (Appointment ID " + newAppointmentId + ") is already booked.");
            return;
        }

        String currentAppointmentDate = getAppointmentDate(appointmentId);
        String newAppointmentDate = getAppointmentDate(newAppointmentId);

        if (newAppointmentDate == null) {
            System.out.println("Error: The new appointment ID " + newAppointmentId + " does not exist.");
            return;
        }

        // Check for one-slot-per-day rule
        if (!newAppointmentDate.equals(currentAppointmentDate) && isAppointmentOnSameDay(patientId, newAppointmentDate)) {
            System.out.println("Error: You have already booked an appointment on " + newAppointmentDate + ". Only one slot per day is allowed.");
            return;
        }

        // Cancel the original appointment and book the new appointment
        cancelAppointment(appointmentId, patientId);
        scheduleAppointment(patientId, patientName, newAppointmentId);

        if (isAppointmentScheduled(newAppointmentId)) {
            System.out.println("Appointment rescheduled successfully to " + newAppointmentId);
        } else {
            System.out.println("Failed to reschedule: the requested slot is unavailable.");
        }
    }

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

    private boolean hasOtherAppointmentsWithDoctor(String patientId, String doctorId) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[3].equals(patientId) && fields[5].equals(doctorId)
                        && (fields[7].equalsIgnoreCase("PENDING") || fields[7].equalsIgnoreCase("CONFIRMED"))) {
                    return true; // Patient has other appointments with this doctor
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking other appointments with doctor: " + e.getMessage());
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

    private String getAppointmentDate(String appointmentId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 2 && fields[0].equals(appointmentId)) {
                    return fields[1].trim(); // Assuming date is in the second column
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving appointment date: " + e.getMessage());
        }
        return null;
    }

    private boolean isAppointmentOnSameDay(String patientId, String date) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.APPOINTMENT_LIST_PATH))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[3].equals(patientId) && fields[1].equals(date) && !fields[7].equalsIgnoreCase("UNRESERVED")) {
                    return true; // Patient has an appointment on the same date
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking for same-day appointments: " + e.getMessage());
        }
        return false;
    }
}
