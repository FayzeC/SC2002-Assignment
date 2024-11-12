package appointmentmanagement.example;
import login.example.FilePaths;
import role.example.Doctor;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DoctorAvailability {
    private Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Method to set availability for a doctor
    public void setAvailability(Doctor doctor) {
        String date;
        LocalTime startTime;
        LocalTime endTime;

        // Prompt for date and validate the format
        while (true) {
            System.out.print("Enter the date for the appointment (YYYY-MM-DD): ");
            date = scanner.nextLine();
            if (isValidDate(date)) {
                break;
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

    // Helper method to validate date format
    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Helper method to parse time in HH:mm format
    private LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use HH:mm.");
            return null;
        }
    }

    // Helper method to check if time falls within the 10:00 to 21:00 range
    private boolean isValidTimeRange(LocalTime time) {
        LocalTime minTime = LocalTime.of(10, 0);
        LocalTime maxTime = LocalTime.of(21, 0);
        return !time.isBefore(minTime) && !time.isAfter(maxTime);
    }

    private List<String> generateTimeSlots(LocalTime startTime, LocalTime endTime) {
        List<String> timeSlots = new ArrayList<>();

        while (startTime.plusMinutes(30).isBefore(endTime) || startTime.plusMinutes(30).equals(endTime)) {
            String slot = startTime.format(TIME_FORMATTER) + " - " + startTime.plusMinutes(30).format(TIME_FORMATTER);
            timeSlots.add(slot);
            startTime = startTime.plusMinutes(30);
        }

        return timeSlots;
    }

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