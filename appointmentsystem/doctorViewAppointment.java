package appointmentsystem;

/**
 * The doctorViewAppointment interface defines methods for viewing a doctor's appointment schedule.
 * It includes methods to view the full personal schedule as well as upcoming confirmed appointments.
 */
public interface doctorViewAppointment {

    /**
     * Displays the full schedule of appointments assigned to the specified doctor,
     * including both confirmed and unconfirmed appointments.
     *
     * @param doctorId The ID of the doctor whose personal schedule is to be viewed.
     */
    void viewPersonalSchedule(String doctorId);

    /**
     * Displays the upcoming confirmed appointments for the specified doctor,
     * showing only appointments with confirmed status.
     *
     * @param doctorId The ID of the doctor whose upcoming confirmed appointments are to be viewed.
     */
    void viewUpcomingAppointments(String doctorId);
}
