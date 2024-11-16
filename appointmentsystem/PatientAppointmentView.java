package appointmentsystem;

import filemanager.FilePaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The PatientAppointmentView class provides functionalities for patients to view available appointment slots
 * and check the status of their scheduled appointments. It interacts with the CSV file to retrieve appointment data.
 * This class implements the {@link patientViewAppointment} interface.
 */
public class PatientAppointmentView implements patientViewAppointment {

    /**
     * Displays a list of all available appointment slots that are unreserved.
     * Each slot shows details such as the appointment ID, date, time slot, doctor name, and current status.
     */
    public void viewAppointmentSlots() {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        System.out.println("\n+======= Available Appointments to Book =======+");
        System.out.printf("%-15s %-15s %-20s %-20s %-15s \n", "Appointment ID", "Date", "Time Slot", "Doctor Name", "Status");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[7].equals("UNRESERVED")) { // Check status in the 8th column
                    String appointmentId = fields[0].trim();
                    String date = fields[1].trim();
                    String timeSlot = fields[2].trim();
                    String doctorName = fields[6].trim();

                    System.out.printf("%-15s %-15s %-20s %-20s %-15s\n", appointmentId, date, timeSlot, doctorName, fields[7].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }
    }

    /**
     * Displays the scheduled appointments for a specific patient.
     * Each appointment displays details such as appointment ID, date, time slot, doctor ID, doctor name, and status.
     *
     * @param patientId The ID of the patient whose appointments are to be viewed.
     */
    public void viewAppointmentStatus(String patientId) {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        System.out.println("\n+======= Scheduled Appointments =======+");
        System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", "Appointment ID", "Date", "Time Slot", "Doctor ID", "Doctor Name", "Status");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[3].equals(patientId)) { // Patient ID in the 4th column
                    String appointmentId = fields[0].trim();
                    String date = fields[1].trim();
                    String timeSlot = fields[2].trim();
                    String doctorId = fields[5].trim();
                    String doctorName = fields[6].trim();
                    String status = fields[7].trim();

                    System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", appointmentId, date, timeSlot, doctorId, doctorName, status);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }
    }
}
