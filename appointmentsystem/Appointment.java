package appointmentsystem;

public class Appointment {
    private final String appointmentID;
    private String appointmentDate;
    private String appointmentTime;
    private String patientID;
    private String patientName;
    private String doctorID;
    private String doctorName;
    private String status;

    public Appointment(String appointmentID, String appointmentDate, String appointmentTime, String patientID, String patientName, String doctorID, String doctorName, String status) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.status = status;
    }

    public void print() { // Remove after final submission this is just to check if data is loaded correctly
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Appointment Time: " + appointmentTime);
        System.out.println("Patient ID: " + patientID);
        System.out.println("Patient Name: " + patientName);
        System.out.println("Doctor ID: " + doctorID);
        System.out.println("Doctor Name: " + doctorName);
        System.out.println("Status: " + status);
    }
}