package MohikaV1;
import org.example.ConcretePatient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.FilePaths;

import java.io.*;

public class PatientAppointmentManager implements patientAppointmentManagement {

    @Override
    public void scheduleAppointment(String patientId, String patientName, String appointmentId) {
        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean appointmentFound = false;

            for (Row row : sheet) {
                Cell appointmentIdCell = row.getCell(0);
                Cell statusCell = row.getCell(7);

                // Check if the appointment ID matches and the status is "UNRESERVED"
                if (appointmentIdCell != null && appointmentIdCell.getStringCellValue().equals(appointmentId) &&
                        statusCell != null && statusCell.getStringCellValue().equalsIgnoreCase("UNRESERVED")) {

                    // Update the appointment details with the patient's ID and name, and change status
                    row.getCell(3).setCellValue(patientId);
                    row.createCell(4).setCellValue(patientName);
                    row.getCell(7).setCellValue("PENDING"); // Update status to "CONFIRMED"
                    appointmentFound = true;
                    break;
                }
            }

            if (appointmentFound) {
                // Write changes back to the file
                try (FileOutputStream fos = new FileOutputStream(FilePaths.APPOINTMENT_LIST_PATH)) {
                    workbook.write(fos);
                }
                System.out.println("Appointment " + appointmentId + " is pending confirmation from the doctor.");
            }
        } catch (IOException e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    @Override
    public void cancelAppointment(String appointmentId) {
        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean appointmentFound = false;

            for (Row row : sheet) {
                Cell appointmentIdCell = row.getCell(0);
                Cell statusCell = row.getCell(7);

                if (appointmentIdCell != null && appointmentIdCell.getStringCellValue().equals(appointmentId) &&
                        statusCell != null && (statusCell.getStringCellValue().equalsIgnoreCase("PENDING")
                        || statusCell.getStringCellValue().equalsIgnoreCase("CONFIRMED"))) {
                    // Update the appointment details with the patient's ID and name, and change status
                    row.getCell(3).setBlank();
                    row.createCell(4).setBlank();
                    row.getCell(7).setCellValue("UNRESERVED");
                    appointmentFound = true;
                    break;
                }
            }

            if (appointmentFound) {
                // Write changes back to the file
                try (FileOutputStream fos = new FileOutputStream(FilePaths.APPOINTMENT_LIST_PATH)) {
                    workbook.write(fos);
                }
                System.out.println("Appointment " + appointmentId + " has been cancelled.");
            }
        } catch (IOException e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    @Override
    public void rescheduleAppointment(String patientId, String patientName, String appointmentId, String newAppointmentId) throws IOException {
        cancelAppointment(appointmentId);
        scheduleAppointment(patientId, patientName, newAppointmentId);
        if (isAppointmentScheduled(newAppointmentId))
            System.out.println("Appointment rescheduled successfully to " + newAppointmentId);
        else
            System.out.println("Failed to reschedule: the requested slot is unavailable.");
    }

    public boolean isAppointmentScheduled(String appointmentId) throws IOException {
        FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell appointmentIdCell = row.getCell(0);
            Cell statusCell = row.getCell(7);

            // Check if the appointment ID matches and the status is "UNRESERVED"
            if (appointmentIdCell != null && appointmentIdCell.getStringCellValue().equals(appointmentId) &&
                    statusCell != null) {
                return true;

            }
        }
        return false;
    }
}




