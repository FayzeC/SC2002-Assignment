package appointmentsystem;
import filemanager.FilePaths;
import roles.Doctor;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The DoctorAvailability class allows doctors to set their availability on a given date.
 * It enables specifying start and end times within a valid range and generates 30-minute time slots,
 * which are then saved to a CSV file for tracking availability.
 */
public class DoctorAvailability {
    private Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Prompts the doctor to set availability by specifying a date, start time, and end time.
     * Validates the date and time inputs, generates 30-minute time slots within the specified
     * range, and writes them to the CSV file.
     *
     * @param doctor The doctor object for whom availability is being set.
     */
    // Method to set availability for a doctor
    public void setAvailability(Doctor doctor) {
        String date;
        LocalTime startTime;
        LocalTime endTime;

        // Prompt for date and validate the format and future date requirement
        while (true) {
            System.out.print("Enter the date for the appointment (YYYY-MM-DD): ");
            date = scanner.nextLine();
            if (isValidDate(date)) {
                if (isFutureDate(date)) {
                    break;
                } else {
                    System.out.println("You can only set availability for future dates.");
                }
            } else {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        // Prompt for start and end times in HH:mm format, ensuring they fall within a valid time range
        while (true) {
            System.out.print("Enter available start time (HH:mm, between 10:00 and 21:00): ");
            String startInput = scanner.nextLine();
            startTime = parseTime(startInput);
            if (startTime != null && isValidTimeRange(startTime)) {
                break;
            } else {
                System.out.println("Invalid start time. Please enter a time between 10:00 and 21:00.");
            }
        }

        while (true) {
            System.out.print("Enter available end time (HH:mm, after start time and no later than 21:00): ");
            String endInput = scanner.nextLine();
            endTime = parseTime(endInput);
            if (endTime != null && endTime.isAfter(startTime) && isValidTimeRange(endTime)) {
                break;
            } else {
                System.out.println("Invalid end time. It must be after the start time and no later than 21:00.");
            }
        }

        // Generate 30-minute time slots
        List<String> timeSlots = generateTimeSlots(startTime, endTime);

        try {
            writeToCSV(doctor.getHospitalID(), doctor.getName(), date, timeSlots);
            System.out.println("Availability set for Doctor ID " + doctor.getHospitalID() + " from " + startTime + " to " + endTime);
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    /**
     * Validates if a given date string matches the expected format (YYYY-MM-DD).
     *
     * @param date The date string to validate.
     * @return true if the date is valid, false otherwise.
     */
    // Helper method to validate date format
    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if a given date is in the future compared to the current date.
     *
     * @param date The date to check.
     * @return true if the date is in the future, false otherwise.
     */
    // Helper method to check if the date is in the future
    private boolean isFutureDate(String date) {
        LocalDate inputDate = LocalDate.parse(date, DATE_FORMATTER);
        return inputDate.isAfter(LocalDate.now());
    }

    /**
     * Parses a time string in the format HH:mm and returns a LocalTime object.
     *
     * @param time The time string to parse.
     * @return A LocalTime object if parsing is successful, null otherwise.
     */
    // Helper method to parse time in HH:mm format
    private LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use HH:mm.");
            return null;
        }
    }

    /**
     * Checks if a specified time falls within the valid range of 10:00 to 21:00.
     *
     * @param time The time to check.
     * @return true if the time is within the range, false otherwise.
     */
    // Helper method to check if time falls within the 10:00 to 21:00 range
    private boolean isValidTimeRange(LocalTime time) {
        LocalTime minTime = LocalTime.of(10, 0);
        LocalTime maxTime = LocalTime.of(21, 0);
        return !time.isBefore(minTime) && !time.isAfter(maxTime);
    }

    /**
     * Generates a list of 30-minute time slots between a specified start and end time.
     * Rounds up the start time to the nearest half-hour if necessary.
     *
     * @param startTime The start time of availability.
     * @param endTime The end time of availability.
     * @return A list of 30-minute time slots as strings.
     */
    private List<String> generateTimeSlots(LocalTime startTime, LocalTime endTime) {
        List<String> timeSlots = new ArrayList<>();

        // Round the start time to the nearest 30-minute mark
        int startMinute = startTime.getMinute();
        if (startMinute > 0 && startMinute < 30) {
            startTime = startTime.withMinute(30); // Round up to 30 minutes
        } else if (startMinute > 30) {
            startTime = startTime.plusHours(1).withMinute(0); // Round up to the next hour
        }

        while (startTime.plusMinutes(30).isBefore(endTime) || startTime.plusMinutes(30).equals(endTime)) {
            String slot = startTime.format(TIME_FORMATTER) + " - " + startTime.plusMinutes(30).format(TIME_FORMATTER);
            timeSlots.add(slot);
            startTime = startTime.plusMinutes(30);
        }

        return timeSlots;
    }

    /**
     * Writes the specified date and time slots for the doctor to the appointment CSV file.
     * Ensures no duplicate slots are added.
     *
     * @param doctorId The ID of the doctor.
     * @param doctorName The name of the doctor.
     * @param date The date of availability.
     * @param timeSlots The list of 30-minute time slots to write.
     * @throws IOException if an I/O error occurs during file writing.
     */
    private void writeToCSV(String doctorId, String doctorName, String date, List<String> timeSlots) throws IOException {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);

        // Load existing time slots for the specified doctor and date
        List<String> existingSlots = new ArrayList<>();
        int totalRows = 0;
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isFirstRow = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstRow) {
                        isFirstRow = false;
                        continue;
                    }

                    totalRows++;
                    String[] fields = line.split(",");
                    String existingDate = fields[1].trim();
                    String existingDoctorId = fields[5].trim();
                    String existingSlot = fields[2].trim();

                    if (existingDate.equals(date) && existingDoctorId.equals(doctorId)) {
                        existingSlots.add(existingSlot);
                    }
                }
            }
        }

        // Append new non-duplicate slots to the CSV file
        try (FileWriter fw = new FileWriter(file, true); PrintWriter pw = new PrintWriter(fw)) {
            if (totalRows == 0) {
                // Create header row if the file does not exist
                pw.println("Appointment ID,Appointment Date,Appointment Time,Patient ID,Patient Name,Doctor ID,Doctor Name,Status");
            }

            int appointmentCounter = totalRows + 1;
            for (String slot : timeSlots) {
                if (!existingSlots.contains(slot)) { // Only add if it's not a duplicate
                    String appointmentID = "PA" + appointmentCounter++;
                    pw.println(appointmentID + "," + date + "," + slot + ",," + "," + doctorId + "," + doctorName + ",UNRESERVED");
                } else {
                    System.out.println("Duplicate slot detected for date " + date + " and time " + slot + ". Skipping this slot.");
                }
            }
        }

        System.out.println("Time slots successfully appended to Appointment_List.csv");
    }
}
