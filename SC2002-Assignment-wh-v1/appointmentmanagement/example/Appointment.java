package appointmentmanagement.example;

public class Appointment {
    private String appointmentID;
    private String appointmentDate;
    private String appointmentTime;
    private String patientAssigned;
    private String doctorAssigned;
    private AppointmentStatus status;
    private int counter;

    public Appointment(String appointmentID, String appointmentDate, String appointmentTime, String patientAssigned, String doctorAssigned, AppointmentStatus status, int counter) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientAssigned = patientAssigned;
        this.doctorAssigned = doctorAssigned;
        this.status = status;
        this.counter = counter;
    }

    public String getAppointmentID(){
        return appointmentID;
    }

    public String getAppointmentDate(){
        return appointmentDate;
    }

    public String getAppointmentTime(){
        return appointmentTime;
    }

    public String getPatientAssigned(){
        return patientAssigned;
    }

    public String getDoctorAssigned(){
        return doctorAssigned;
    }

    public AppointmentStatus getStatus(){
        return status;
    }

    public int getCounter(){
        return counter;
    }




    public void print(){ // Remove after final submission this is just to check if data is loaded correctly
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Appointment Time: " + appointmentTime);
        System.out.println("Patient Assigned: " + patientAssigned);
        System.out.println("Doctor Assigned: " + doctorAssigned);
        System.out.println("Status: " + status);
        System.out.println("Counter: " + counter);
    }
}
