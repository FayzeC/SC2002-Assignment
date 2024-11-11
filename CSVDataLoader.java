import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVDataLoader {
    public static List<Pharmacist> loadStaff(String filePath, List<Pharmacist> pharmacists, List<Doctor> doctors, List<Administrator> administrators) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String staffID = fields[0].trim();
                String name = fields[1].trim();
                String password = fields[2].trim();
                String role = fields[3].trim();
                String gender = fields[4].trim();
                String age = fields[5].trim();
                boolean firstLogin = "Yes".equalsIgnoreCase(fields[6].trim());

                if (staffID.startsWith("P")) {
                    pharmacists.add(new Pharmacist(staffID, name, password, role, gender, age, firstLogin));
                } else if (staffID.startsWith("D")) {
                    doctors.add(new Doctor(staffID, name, password, role, gender, age, firstLogin));
                } else if (staffID.startsWith("A")) {
                    administrators.add(new Administrator(staffID, name, password, role, gender, age, firstLogin));
                }
            }
        }
        return pharmacists; // Doctors and administrators lists are modified in place
    }

    public static List<Patient> loadPatients(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String patientID = fields[0].trim();
                String name = fields[1].trim();
                String password = fields[2].trim();
                String dob = fields[3].trim();
                String gender = fields[4].trim();
                String bloodType = fields[5].trim();
                String email = fields[6].trim();
                String doctorAssigned = fields[7].trim();
                String pastDiagnosis = fields[8].trim();
                String pastTreatment = fields[9].trim();
                boolean firstLogin = "Yes".equalsIgnoreCase(fields[10].trim());

                ConcretePatient patient = new ConcretePatient(patientID, name, password, dob, gender, bloodType, email, doctorAssigned, pastDiagnosis, pastTreatment, firstLogin, "Patient");
                patient.setInformationAccess(patient);
                patients.add(patient);
            }
        }
        return patients;
    }

    public static List<AppointmentOutcomeRecord> loadApptOutcomeRecord(String filePath) throws IOException {
        List<AppointmentOutcomeRecord> apptOutcomeRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String appointmentID = fields[0].trim();
                String patientID = fields[1].trim();
                String doctorAssigned = fields[2].trim();
                String appointmentDate = fields[3].trim();
                String service = fields[4].trim();
                String medication = fields[5].trim();
                String consultationNotes = fields[6].trim();

                AppointmentOutcomeRecord apptOutcomeRecord = new AppointmentOutcomeRecord(appointmentID, patientID, doctorAssigned, appointmentDate, service, medication, consultationNotes);
                apptOutcomeRecords.add(apptOutcomeRecord);
            }
        }
        return apptOutcomeRecords;
    }

    public static List<Appointment> loadAppointments(String filePath) throws IOException {
        List<Appointment> appointments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String appointmentID = fields[0].trim();
                String appointmentDate = fields[1].trim();
                String appointmentTime = fields[2].trim();
                String patientAssigned = fields[3].trim();
                String doctorAssigned = fields[4].trim();
                String status = fields[5].trim();
                int counter = Integer.parseInt(fields[6].trim());

                Appointment appointment = new Appointment(appointmentID, appointmentDate, appointmentTime, patientAssigned, doctorAssigned, status, counter);
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public static List<Inventory> loadInventory(String filePath) throws IOException {
        List<Inventory> inventoryList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstRow = true;

            while ((line = br.readLine()) != null) {
                if (isFirstRow) { // Skip header row
                    isFirstRow = false;
                    continue;
                }

                String[] fields = line.split(",");
                String medicineName = fields[0].trim();
                int initialStock = Integer.parseInt(fields[1].trim());
                int lowStockAlert = Integer.parseInt(fields[2].trim());

                Inventory inventory = new Inventory(medicineName, initialStock, lowStockAlert);
                inventoryList.add(inventory);
            }
        }
        return inventoryList;
    }
}
