package MohikaV1;
import org.example.FilePaths;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Doctor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

        // Generate 30-minute time slots and write to the Excel file
        List<String> timeSlots = generateTimeSlots(startTime, endTime);

        try {
            writeToExcel(doctor.getHospitalID(), doctor.getName(), date, timeSlots);
            System.out.println("Availability set for Doctor ID " + doctor.getHospitalID() + " from " + startTime + " to " + endTime);
        } catch (IOException e) {
            System.err.println("Error writing to Excel: " + e.getMessage());
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

        while ((startTime.plusMinutes(30)).isBefore(endTime) || startTime.plusMinutes(30).equals(endTime)) {
            String slot = startTime.format(TIME_FORMATTER) + " - " + startTime.plusMinutes(30).format(TIME_FORMATTER);
            timeSlots.add(slot);
            startTime = startTime.plusMinutes(30);
        }

        return timeSlots;
    }

    private void writeToExcel(String doctorId, String doctorName, String date, List<String> timeSlots) throws IOException {
        Workbook workbook;
        Sheet sheet;
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);

        // Check if the file already exists
        if (file.exists()) {
            // Load the existing workbook and sheet
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet("Appointment_List");
            fis.close();
        } else {
            // Create a new workbook and sheet if file does not exist
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Appointment_List");

            // Create header row with bold style
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);

            Row header = sheet.createRow(0);
            String[] headers = {"Appointment ID", "Appointment Date", "Appointment Time", "Patient ID", "Patient Name",
                    "Doctor ID", "Doctor Name", "Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
        }

        // Load existing time slots for the specified doctor and date
        List<String> existingSlots = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row
            Cell dateCell = row.getCell(1);
            Cell doctorIdCell = row.getCell(5);

            if (dateCell != null && doctorIdCell != null &&
                    dateCell.getStringCellValue().equals(date) &&
                    doctorIdCell.getStringCellValue().equals(doctorId)) {
                existingSlots.add(row.getCell(2).getStringCellValue());
            }
        }

        // Find the last row to start appending new data
        int rowNum = sheet.getLastRowNum() + 1;

        // Fill in only non-duplicate time slots
        for (String slot : timeSlots) {
            if (!existingSlots.contains(slot)) { // Only add if it's not a duplicate
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue("PA" + (rowNum - 1));
                row.createCell(1).setCellValue(date);
                row.createCell(2).setCellValue(slot);
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue(doctorId);
                row.createCell(6).setCellValue(doctorName);
                row.createCell(7).setCellValue("UNRESERVED");
            } else {
                System.out.println("Duplicate slot detected for date " + date + " and time " + slot + ". Skipping this slot.");
            }
        }

        // Write the workbook to the file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        System.out.println("Time slots successfully appended to Appointment_List.xlsx");
    }
}