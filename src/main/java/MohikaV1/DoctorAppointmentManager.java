package MohikaV1;
import org.example.FilePaths;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DoctorAppointmentManager extends DoctorScheduleView implements doctorAppointmentManagement {

    @Override
    public void acceptAppointment(String doctorId, String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            System.out.println("Invalid appointment ID.");
            return;
        }
        if (isAppointmentAssignedToDoctor(doctorId.trim(), appointmentId.trim())) {
            updateAppointmentStatus(appointmentId.trim(), "CONFIRMED");
            System.out.println("Appointment ID " + appointmentId + " has been accepted.");
        } else {
            System.out.println("Error: Appointment ID " + appointmentId + " is not assigned to Doctor ID " + doctorId);
        }
    }

    @Override
    public void declineAppointment(String doctorId, String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            System.out.println("Invalid appointment ID.");
            return;
        }
        if (isAppointmentAssignedToDoctor(doctorId.trim(), appointmentId.trim())) {
            updateAppointmentStatus(appointmentId.trim(), "CANCELLED");
            System.out.println("Appointment ID " + appointmentId + " has been declined.");
        } else {
            System.out.println("Error: Appointment ID " + appointmentId + " is not assigned to Doctor ID " + doctorId);
        }
    }

    // Method to verify if an appointment is assigned to a specific doctor
    private boolean isAppointmentAssignedToDoctor(String doctorId, String appointmentId) {
        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Cell appointmentIdCell = row.getCell(0);
                Cell doctorIdCell = row.getCell(5);

                if (appointmentIdCell != null && doctorIdCell != null &&
                        appointmentIdCell.getCellType() == CellType.STRING &&
                        doctorIdCell.getCellType() == CellType.STRING) {

                    String cellAppointmentId = appointmentIdCell.getStringCellValue().trim();
                    String cellDoctorId = doctorIdCell.getStringCellValue().trim();

                    // Check if both appointment ID and doctor ID match
                    if (cellAppointmentId.equalsIgnoreCase(appointmentId) && cellDoctorId.equalsIgnoreCase(doctorId)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error verifying appointment assignment: " + e.getMessage());
        }
        return false;
    }

    // Update the status of the appointment if assigned to the doctor
    private void updateAppointmentStatus(String appointmentId, String newStatus) {
        try (FileInputStream fis = new FileInputStream(FilePaths.APPOINTMENT_LIST_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Cell appointmentIdCell = row.getCell(0);

                if (appointmentIdCell != null && appointmentIdCell.getCellType() == CellType.STRING &&
                        appointmentIdCell.getStringCellValue().trim().equalsIgnoreCase(appointmentId)) {
                    row.getCell(7).setCellValue(newStatus); // Update the status column
                    break;
                }
            }

            // Write the updated workbook to the file
            try (FileOutputStream fos = new FileOutputStream(FilePaths.APPOINTMENT_LIST_PATH)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }
    }
}
