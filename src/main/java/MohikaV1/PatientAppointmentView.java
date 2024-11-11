package MohikaV1;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.FilePaths;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PatientAppointmentView implements patientViewAppointment {

    public void viewAppointmentSlots() {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }
        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            //Printing Excel sheet here for easy access
            System.out.println("\n+======= Available Appointments to Book =======+");
            System.out.printf("%-15s %-15s %-20s %-20s %-15s \n", "Appointment ID", "Date", "Time Slot", "Doctor Name", "Status");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Cell statusCell = row.getCell(7);

                if (statusCell != null && statusCell.getStringCellValue().equals("UNRESERVED")) {
                    String appointmentId = row.getCell(0).getStringCellValue();
                    String date = row.getCell(1).getStringCellValue();
                    String timeSlot = row.getCell(2).getStringCellValue();
                    String doctorName = row.getCell(6).getStringCellValue();
                    System.out.printf("%-15s %-15s %-20s %-20s %-15s\n", appointmentId, date, timeSlot, doctorName, statusCell.getStringCellValue());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }
    }

    public void viewAppointmentStatus(String patientId) {
        File file = new File(FilePaths.APPOINTMENT_LIST_PATH);
        if (!file.exists()) {
            System.err.println("Error: " + FilePaths.APPOINTMENT_LIST_PATH + " does not exist.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            //Printing Excel sheet here for easy access
            System.out.println("\n+======= Scheduled Appointments =======+");
            System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", "Appointment ID", "Date", "Time Slot", "Doctor ID", "Doctor Name", "Status");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell patientIdCell = row.getCell(3);
                Cell statusCell = row.getCell(7);

                if (patientIdCell != null && patientIdCell.getStringCellValue().equals(patientId) && statusCell != null) {
                    String appointmentId = row.getCell(0).getStringCellValue();
                    String date = (row.getCell(1).getStringCellValue() != null) ? row.getCell(1).getStringCellValue() : "";
                    String timeSlot = (row.getCell(2).getStringCellValue() != null) ? row.getCell(2).getStringCellValue() : "";
                    String doctorId = (row.getCell(5).getStringCellValue() != null) ? row.getCell(3).getStringCellValue() : "";
                    String doctorName = (row.getCell(6).getStringCellValue() != null) ? row.getCell(4).getStringCellValue() : "";
                    String status = (row.getCell(7).getStringCellValue() != null) ? row.getCell(7).getStringCellValue() : "";
                    System.out.printf("%-15s %-15s %-20s %-15s %-20s %-15s\n", appointmentId, date, timeSlot, doctorId, doctorName, status);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }
    }
}
