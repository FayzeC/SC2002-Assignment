package appointmentsystem;

import filemanager.FilePaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The DoctorScheduleView class provides functionalities for doctors to view their schedules,
 * including both all appointments and upcoming confirmed appointments.
 * It implements the {@link doctorViewAppointment} interface.
 */
public class DoctorScheduleView implements doctorViewAppointment {

    /**
     * Displays the full schedule of all appointments assigned to the specified doctor.
     * Each appointment displays details such as date, time slot, patient ID, patient name, and status.
     *
     * @param doctorId The ID of the doctor whose schedule is to be viewed.
     */
    public void viewPersonalSchedule(String doctorId) {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        System.out.println("\n+======= Doctor Schedule =======+");
        System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", "Appointment ID", "Date", "Time Slot", "Patient ID", "Patient Name", "Status");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[5].equals(doctorId)) { // Doctor ID is in the 6th column
                    String appointmentId = fields[0].trim();
                    String date = fields[1].trim();
                    String timeSlot = fields[2].trim();
                    String patientId = fields[3].trim();
                    String patientName = fields[4].trim();
                    String status = fields[7].trim();

                    System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", appointmentId, date, timeSlot, patientId, patientName, status);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading schedule: " + e.getMessage());
        }
    }

    /**
     * Displays only the upcoming confirmed appointments for the specified doctor.
     * This view excludes unconfirmed or pending appointments.
     *
     * @param doctorId The ID of the doctor whose upcoming appointments are to be viewed.
     */
    public void viewUpcomingAppointments(String doctorId) {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        System.out.println("\n+======= Upcoming Accepted Appointments =======+");
        System.out.printf("%-15s %-15s %-20s %-15s %-20s\n", "Appointment ID", "Date", "Time Slot", "Patient ID", "Patient Name");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 8 && fields[5].equals(doctorId) && fields[7].equals("CONFIRMED")) { // Doctor ID in 6th, Status in 8th column
                    String appointmentId = fields[0].trim();
                    String date = fields[1].trim();
                    String timeSlot = fields[2].trim();
                    String patientId = fields[3].trim();
                    String patientName = fields[4].trim();

                    System.out.printf("%-15s %-15s %-20s %-15s %-20s\n", appointmentId, date, timeSlot, patientId, patientName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }
    }
}