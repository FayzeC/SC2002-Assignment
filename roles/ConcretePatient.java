package roles;

import filemanager.CSVDataLoader;
import filemanager.CSVUpdater;
import filemanager.FilePaths;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Represents a concrete implementation of a Patient with the ability to view and update their medical records.
 * Implements the {@link InformationAccess} interface to provide methods for accessing and updating personal information.
 * Inherits from the {@link Patient} class.
 */
public class ConcretePatient extends Patient implements InformationAccess {

    /**
     * Constructs a ConcretePatient object with the specified details.
     *
     * @param hospitalID    the hospital ID of the patient
     * @param name          the name of the patient
     * @param password      the password of the patient
     * @param dob           the date of birth of the patient
     * @param gender        the gender of the patient
     * @param bloodType     the blood type of the patient
     * @param email         the email of the patient
     * @param doctorAssigned the doctor assigned to the patient
     * @param pastDiagnosis the past diagnoses of the patient
     * @param pastTreatment the past treatments of the patient
     * @param firstLogin    whether it's the patient's first login
     * @param role          the role of the patient
     */
    public ConcretePatient(String hospitalID, String name, String password, String dob, String gender, String bloodType, String email, String doctorAssigned, String pastDiagnosis, String pastTreatment, boolean firstLogin, String role) {
        super(hospitalID, name, password, dob, gender, bloodType, email, doctorAssigned, pastDiagnosis, pastTreatment, firstLogin, role);
    }

    /**
     * Allows the patient to view their medical records.
     * Loads and filters records based on the patient's hospital ID and displays the medical history.
     *
     * @param user the current logged-in user
     */
    @Override
    public void viewMedicalRecords(User user) {
        try {
            // Load all patient records from the CSV
            List<Patient> allPatients = CSVDataLoader.loadPatients(FilePaths.PATIENT_LIST_PATH);

            // Filter records for the current patient's ID
            List<Patient> patientRecords = allPatients.stream()
                    .filter(record -> record.getHospitalID().equals(this.getHospitalID()))
                    .collect(Collectors.toList());

            if (patientRecords.isEmpty()) {
                System.out.println("No records found for Patient ID: " + this.getHospitalID());
                return;
            }

            System.out.println("\n+======= Medical Records =======+");
            System.out.println("Patient ID: " + getHospitalID() + ", Patient Name: " + getName() + "\n" +
                    "Date of Birth: " + getDoB() + ", Gender: " + getGender() + "\n" +
                    "Contact: " + getEmail() + ", Blood Type: " + getBloodType() + "\n" +
                    "---------- Medical History ----------");

            // Display each record, with doctor-specific details
            for (Patient record : patientRecords) {
                System.out.println("Doctor ID: " + record.getDoctorAssigned() + "\n" +
                        "Past Diagnosis: " + record.getPastDiagnosis() + "\n" +
                        "Past Treatment: " + record.getPastTreatment() + "\n" +
                        "------------------------------------");
            }

        } catch (IOException e) {
            System.err.println("Error loading medical records: " + e.getMessage());
        }
    }

    /**
     * Allows the patient to update their personal information such as name, date of birth, email, and blood type.
     * Updates the CSV file with the new information.
     *
     * @param user the current logged-in user
     * @throws IOException if there is an error during file handling
     */
    @Override
    public void updatePersonalInformation(User user) throws IOException {
//        ExcelUpdater excelUpdater = new ExcelUpdater();
        Scanner sc = new Scanner(System.in);
        boolean exec = true;

        do{
            System.out.print("""
                \nWhat information do you want to update?
                1. Name
                2. Date of Birth
                3. Email
                4. Blood Type
                5. Exit
                Enter Your Choice : 
                """);

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    System.out.print("\nEnter New Name: ");
                    String name = sc.nextLine();
                    user.setName(name);
                    CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, getHospitalID(), null , "Name", name, 0, 0);
                    System.out.println("Name updated successfully.");
                    break;
                case 2:
                    System.out.println("\nEnter New Date of Birth:");

                    int day, month, year;
                    boolean validDate = false;

                    while (!validDate) {
                        System.out.print("Enter Day: ");
                        day = sc.nextInt();
                        System.out.print("Enter Month: ");
                        month = sc.nextInt();
                        System.out.print("Enter Year: ");
                        year = sc.nextInt();

                        if (isValidDate(day, month, year)) {
                            validDate = true;
                            String dob = String.format("%02d-%02d-%04d", day, month, year);
                            setDob(dob);
                            CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, getHospitalID(), null , "Date of Birth", dob, 0, 0);
                            System.out.println("Date of birth updated successfully.");
                        } else {
                            System.out.println("Invalid date. Please enter a valid date.");
                        }
                    }
                    break;
                case 3:
                    boolean validEmail = false;
                    while(!validEmail) {
                        System.out.print("\nEnter New Email: ");
                        String email = sc.nextLine();
                        if(email.contains("@")){
                            validEmail = true;
                            setEmail(email);
                            CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, getHospitalID(), null, "Contact Information", email, 0, 0);
                            System.out.println("Email updated successfully.");
                        }else{
                            System.out.println("Invalid email. Please enter a valid email address. Be sure to include '@'");
                        }
                    }
                    break;
                case 4:
                    boolean validBloodType = false;
                    while(!validBloodType) {
                        System.out.print("\nEnter New Blood Type: ");
                        String bloodType = sc.nextLine();
                        if(bloodType.matches("A\\+|A-|B\\+|B-|AB\\+|AB-|O\\+|O-")){
                            validBloodType = true;
                            setBloodType(bloodType);
                            CSVUpdater.updater(FilePaths.PATIENT_LIST_PATH, getHospitalID(), null , "Blood Type", bloodType, 0, 0);
                            System.out.println("Blood type updated successfully.");
                        }else{
                            System.out.println("Invalid blood type. Please enter a valid blood type.");
                        }
                    }
                    break;
                case 5:
                    exec = false;
                    break;
                default:
                    System.out.println("Invalid choice");

            }
        }while(exec);
    }

    /**
     * Validates the date of birth entered by the patient.
     * Ensures that the date is realistic (e.g., year between 1900 and current year, valid month and day).
     *
     * @param day the day of the month
     * @param month the month of the year
     * @param year the year
     * @return true if the date is valid, false otherwise
     */
    private boolean isValidDate(int day, int month, int year) {
        int currentYear = java.time.Year.now().getValue(); // Get the current year

        if (year < 1900 || year > currentYear) {
            System.out.println("Year should be between 1900 and the current year (" + currentYear + ").");
            return false;
        }

        if (month < 1 || month > 12) {
            System.out.println("Month should be between 1 and 12.");
            return false;
        }

        int maxDaysInMonth;
        switch (month) {
            case 4: case 6: case 9: case 11:
                maxDaysInMonth = 30; break;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) { // Check if leap year
                    maxDaysInMonth = 29;
                } else {
                    maxDaysInMonth = 28;
                }
                break;
            default:
                maxDaysInMonth = 31;
        }

        if (day < 1 || day > maxDaysInMonth) {
            System.out.println("Day should be between 1 and " + maxDaysInMonth + " for the given month.");
            return false;
        }

        return true;
    }

}