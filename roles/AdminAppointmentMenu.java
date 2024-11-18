package roles;

import java.util.Scanner;

import appointmentoutcomerecordsystem.AdminAORView;
import appointmentsystem.adminViewAppointment;

/**
 * The AdminAppointmentMenu class provides a user interface for administrators
 * to manage and view appointment details and completed appointment outcomes.
 * This menu allows administrators to:
 * - View all appointments.
 * - Filter appointments by status.
 * - View completed appointment outcomes.
 */
public class AdminAppointmentMenu {

    /**
     * The {@link adminViewAppointment} instance for managing appointment views.
     */
    private final adminViewAppointment appointmentService;

    /**
     * The {@link AdminAORView} instance for viewing appointment outcome records.
     */
    private AdminAORView aorView = new AdminAORView();

    /**
     * Constructs an AdminAppointmentMenu with the specified services.
     *
     * @param appointmentService The service for managing and viewing appointments.
     * @param aorView The service for managing and viewing appointment outcome records.
     */
    public AdminAppointmentMenu(adminViewAppointment appointmentService, AdminAORView aorView) {
        this.appointmentService = appointmentService;
        this.aorView = aorView;
    }

    /**
     * Displays the appointment management menu and processes user input.
     * The menu allows administrators to:
     * - View all appointments.
     * - Filter appointments by their status.
     * - View completed appointment outcomes.
     * The menu loops until the administrator selects the option to exit.
     */
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n+======= Admin Appointment Menu =======+");
            System.out.println("1. View All Appointments");
            System.out.println("2. View Appointments by Status");
            System.out.println("3. View Completed Outcomes");
            System.out.println("4. Go Back to Main Menu");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> appointmentService.viewAllAppointments(); // View all appointments
                case 2 -> {
                    System.out.print("Enter appointment status (e.g. pending, unreserved, completed): ");
                    String status = scanner.nextLine().toUpperCase(); // Convert input to uppercase for uniformity
                    appointmentService.viewAppointmentsByStatus(status); // View appointments by status
                }
                case 3 -> aorView.viewAppointmentOutcomeRecord("approved"); // View approved appointment outcomes
                case 4 -> System.out.println("Exiting the menu..."); // Exit the menu
                default -> System.out.println("Invalid choice. Please select a valid option."); // Handle invalid input
            }
        } while (choice != 4);
    }
}
