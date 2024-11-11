package MohikaV1;
import org.example.FilePaths;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class DoctorScheduleView implements doctorViewAppointment {

    public void viewPersonalSchedule(String doctorId) {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            //Printing Excel sheet here for easy access
            System.out.println("\n+======= Doctor Schedule =======+");
            System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", "Appointment ID", "Date", "Time Slot", "Patient ID", "Patient Name", "Status");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Cell doctorIdCell = row.getCell(5);
                if (doctorIdCell != null && doctorIdCell.getStringCellValue().equals(doctorId)) {
                    String appointmentId = row.getCell(0).getStringCellValue();
                    String date = (row.getCell(1).getStringCellValue() != null) ? row.getCell(1).getStringCellValue() : "";
                    String timeSlot = (row.getCell(2).getStringCellValue() != null) ? row.getCell(2).getStringCellValue() : "";
                    String patientId = (row.getCell(3).getStringCellValue() != null) ? row.getCell(3).getStringCellValue() : "";
                    String patientName = (row.getCell(4).getStringCellValue() != null) ? row.getCell(4).getStringCellValue() : "";
                    String status = (row.getCell(7).getStringCellValue() != null) ? row.getCell(7).getStringCellValue() : "";
                    System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", appointmentId, date, timeSlot, patientId, patientName, status);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading schedule: " + e.getMessage());
        }
    }

    public void viewUpcomingAppointments(String doctorId) {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            //Printing Excel sheet here for easy access
            System.out.println("\n+======= Upcoming Accepted Appointments =======+");
            System.out.printf("%-15s %-15s %-20s %-15s %-20s\n", "Appointment ID", "Date", "Time Slot", "Patient ID", "Patient Name");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell doctorIdCell = row.getCell(5);
                Cell statusCell = row.getCell(7);

                if (doctorIdCell != null && doctorIdCell.getStringCellValue().equals(doctorId)
                        && statusCell != null && statusCell.getStringCellValue().equals("CONFIRMED")) {
                    String appointmentId = row.getCell(0).getStringCellValue();
                    String date = (row.getCell(1).getStringCellValue() != null) ? row.getCell(1).getStringCellValue() : "";
                    String timeSlot = (row.getCell(2).getStringCellValue() != null) ? row.getCell(2).getStringCellValue() : "";
                    String patientId = (row.getCell(3).getStringCellValue() != null) ? row.getCell(3).getStringCellValue() : "";
                    String patientName = (row.getCell(4).getStringCellValue() != null) ? row.getCell(4).getStringCellValue() : "";
                    System.out.printf("%-15s %-15s %-20s %-15s %-20s\n", appointmentId, date, timeSlot, patientId, patientName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }
    }

}
