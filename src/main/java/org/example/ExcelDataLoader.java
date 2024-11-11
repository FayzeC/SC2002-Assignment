package org.example;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExcelDataLoader {
    public static List<Pharmacist> loadStaff(String filePath, List<Pharmacist> pharmacists, List<Doctor> doctors, List<Administrator> administrators) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while(rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) continue; // Skip header row

            // Using a helper method to safely retrieve String values from cells
            String staffID = getCellStringValue(row.getCell(0));
            String name = getCellStringValue(row.getCell(1));
            String password = getCellStringValue(row.getCell(2));
            String role = getCellStringValue(row.getCell(3));
            String gender = getCellStringValue(row.getCell(4));
            String age = getCellStringValue(row.getCell(5));
            boolean firstLogin = Objects.equals(getCellStringValue(row.getCell(6)), "Yes");

            // Add to respective list based on staff ID prefix
            if (staffID != null && !staffID.isEmpty() && name != null && !name.isEmpty()) {
                if (staffID.startsWith("P")) {
                    pharmacists.add(new Pharmacist(staffID, name, password, role, gender, age, firstLogin));
                } else if (staffID.startsWith("D")) {
                    doctors.add(new Doctor(staffID, name, password, role, gender, age, firstLogin));
                } else if (staffID.startsWith("A")) {
                    administrators.add(new Administrator(staffID, name, password, role, gender, age, firstLogin));
                }
            }
        }
        workbook.close();
        fis.close();
        return pharmacists; // Return the pharmacists list; doctors and administrators lists are modified in place
    }

    public static List<Patient> loadPatients(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while(rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) continue; // Skip header row

            // Using a helper method to safely retrieve String values from cells
            String patientID = getCellStringValue(row.getCell(0));
            String name = getCellStringValue(row.getCell(1));
            String password = getCellStringValue(row.getCell(2));
            String dob = getCellStringValue(row.getCell(3));
            String gender = getCellStringValue(row.getCell(4));
            String bloodType = getCellStringValue(row.getCell(5));
            String email = getCellStringValue(row.getCell(6));
            String doctorAssigned = getCellStringValue(row.getCell(7));
            String pastDiagnosis = getCellStringValue(row.getCell(8));
            String pastTreatment = getCellStringValue(row.getCell(9));
            boolean firstLogin = Objects.equals(getCellStringValue(row.getCell(10)), "Yes");

            // Ensure that essential fields are not null or empty before creating a new Patient object
            if (patientID != null && !patientID.isEmpty() && name != null && !name.isEmpty()) {
                ConcretePatient patient = new ConcretePatient(patientID, name, password, dob, gender, bloodType, email, doctorAssigned, pastDiagnosis, pastTreatment, firstLogin, "Patient");
                patient.setInformationAccess(patient);
                patients.add(patient);
            }
        }
        workbook.close();
        fis.close();
        return patients;
    }

    public static List<AppointmentOutcomeRecord> loadApptOutcomeRecord(String filePath) throws IOException {
        List<AppointmentOutcomeRecord> apptOutcomeRecords = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while(rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) continue; // Skip header row

            // Using a helper method to safely retrieve String values from cells
            String appointmentID = getCellStringValue(row.getCell(0));
            String patientID = getCellStringValue(row.getCell(1));
            String doctorAssigned = getCellStringValue(row.getCell(2));
            String appointmentDate = getCellStringValue(row.getCell(3));
            String service = getCellStringValue(row.getCell(4));
            String medication = getCellStringValue(row.getCell(5));
            String consultationNotes = getCellStringValue(row.getCell(6));

            AppointmentOutcomeRecord apptOutcomeRecord = new AppointmentOutcomeRecord(appointmentID, patientID, doctorAssigned, appointmentDate, service, medication, consultationNotes);
            apptOutcomeRecords.add(apptOutcomeRecord);
        }
        workbook.close();
        fis.close();
        return apptOutcomeRecords;
    }

    public static List<Appointment> loadAppointments(String filePath) throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while(rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) continue; // Skip header row

            // Using a helper method to safely retrieve String values from cells
            String appointmentID = getCellStringValue(row.getCell(0));
            String appointmentDate = getCellStringValue(row.getCell(1));
            String appointmentTime = getCellStringValue(row.getCell(2));
            String patientAssigned = getCellStringValue(row.getCell(3));
            String doctorAssigned = getCellStringValue(row.getCell(4));
            String status = getCellStringValue(row.getCell(5));

            Appointment appointment = new Appointment(appointmentID, appointmentDate, appointmentTime, patientAssigned, doctorAssigned, status);
            appointments.add(appointment);
        }
        workbook.close();
        fis.close();
        return appointments;
    }

    public static List<Inventory> loadInventory(String filePath) throws IOException {
        List<Inventory> inventoryList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while(rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) continue; // Skip header row

            // Using a helper method to safely retrieve String values from cells
            String medicineName = getCellStringValue(row.getCell(0));
            int initialStock = Integer.parseInt(getCellStringValue(row.getCell(1)));
            int lowStockAlert = Integer.parseInt(getCellStringValue(row.getCell(2)));

            Inventory inventory = new Inventory(medicineName, initialStock, lowStockAlert);
            inventoryList.add(inventory);
        }
        workbook.close();
        fis.close();
        return inventoryList;
    }

    private static String getCellStringValue(Cell cell) {
        // Check if the cell is null or the cell type is BLANK
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return ""; // Return empty string if cell is null or blank
        } else {
            // Return the cell's string value, safely handling different cell types
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> {
                    // Check if the numeric value is an integer
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (int) numericValue) {
                        yield String.valueOf((int) numericValue); // Convert to integer string if no decimals
                    } else {
                        yield String.valueOf(numericValue); // Use double string representation otherwise
                    }
                }
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                default -> cell.toString(); // For other types, convert to string as a fallback
            };
        }
    }

}