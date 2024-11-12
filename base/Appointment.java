package base;

public class Appointment {
    private String appointmentID;
    private String appointmentDate;
    private String appointmentTime;
    private String patientAssigned;
    private String doctorAssigned;
    private String status;
    private int counter;

    public Appointment(String appointmentID, String appointmentDate, String appointmentTime, String patientAssigned, String doctorAssigned, String status, int counter) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientAssigned = patientAssigned;
        this.doctorAssigned = doctorAssigned;
        this.status = status;
        this.counter = counter;
    }

    public void print(){ // Remove after final submission this is just to check if data is loaded correctly
        System.out.println("base.Appointment ID: " + appointmentID);
        System.out.println("base.Appointment Date: " + appointmentDate);
        System.out.println("base.Appointment Time: " + appointmentTime);
        System.out.println("base.Patient Assigned: " + patientAssigned);
        System.out.println("base.Doctor Assigned: " + doctorAssigned);
        System.out.println("Status: " + status);
        System.out.println("Counter: " + counter);
    }
}
