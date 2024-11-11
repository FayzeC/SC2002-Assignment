package org.example;
import MohikaV1.DoctorAppointmentManager;
import MohikaV1.DoctorAvailability;
import MohikaV1.DoctorScheduleView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Doctor extends User {
    public static final int LOGOUT_OPTION = 9; // Define a constant for logout option
    private String age;
    private DoctorAvailability availabilityManager;
    private DoctorAppointmentManager appointmentManager;
    private DoctorScheduleView schedule;


    public Doctor(String hospitalID, String name, String password, String role, String gender, String age, boolean firstLogin) throws IOException {
        super(hospitalID, name, password, role, gender, firstLogin);
        this.age = age;
        this.availabilityManager = new DoctorAvailability();
        this.appointmentManager = new DoctorAppointmentManager();
        this.schedule = new DoctorScheduleView();
    }

    @Override
    public void displayMenu() {
        System.out.println("""
                \n+======= Doctor Menu =======+
                1. View Patient Medical Records
                2. Update Patient Medical Records
                3. View Personal Schedule
                4. Set Availability for Appointments
                5. Accept or Decline Appointment Requests
                6. View Upcoming Appointments
                7. Record Appointment Outcome
                8. Change Password
                9. Logout
                """);
    }

    @Override
    public void handleOption(int option) throws IOException {
        Scanner scanner = new Scanner(System.in);
        switch (option) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                schedule.viewPersonalSchedule(getHospitalID());
                break;
            case 4:
                availabilityManager.setAvailability(this);
                break;
            case 5:
                System.out.print("Enter Appointment ID to accept or decline: ");
                String appointmentId = scanner.nextLine();
                System.out.print("Enter 'A' to accept or 'D' to decline: ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (choice.equals("A")) {
                    appointmentManager.acceptAppointment(getHospitalID(), appointmentId);
                } else if (choice.equals("D")) {
                    appointmentManager.declineAppointment(getHospitalID(), appointmentId);
                } else {
                    System.out.println("Invalid choice.");
                }
                break;
            case 6: schedule.viewUpcomingAppointments(getHospitalID());
                break;
            case 7:
                break;
            case 8: changePassword(FilePaths.STAFF_LIST_PATH);
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    // Getters
    public int getLogoutOption() { return LOGOUT_OPTION; }

}
