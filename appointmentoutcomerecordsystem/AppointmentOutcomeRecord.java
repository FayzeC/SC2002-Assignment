package appointmentoutcomerecordsystem;

public class AppointmentOutcomeRecord {
    private String appointmentID;
    private String patientID;
    private String doctorAssigned;
    private String appointmentDate;
    private String service;
    private String medication;
    private String status;
    private String quantity;
    private String consultationNotes;

    public AppointmentOutcomeRecord(String appointmentID, String patientID, String doctorAssigned,
                                    String appointmentDate, String service, String medication,
                                    String quantity, String status, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.patientID = patientID; // Make sure patientID is not null
        this.doctorAssigned = doctorAssigned;
        this.appointmentDate = appointmentDate;
        this.service = service;
        this.medication = medication;
        this.quantity = quantity;
        this.status = status;
        this.consultationNotes = consultationNotes;
    }

    public String getAppointmentID() {return appointmentID;}
    public String getPatientID() {return patientID;}
    public String getDoctorAssigned() {return doctorAssigned;}
    public String getAppointmentDate() {return appointmentDate;}
    public String getService() {return service;}
    public String getMedication() {return medication;}
    public String getQuantity() {return quantity;}
    public String getStatus() {return status;}
    public String getConsultationNotes() {return consultationNotes;}


    public void print(){ // Remove after final submission this is just to check if data is loaded correctly
        System.out.println("AppointmentID: " + appointmentID);
        System.out.println("PatientID: " + patientID);
        System.out.println("DoctorAssigned: " + doctorAssigned);
        System.out.println("AppointmentDate: " + appointmentDate);
        System.out.println("Service: " + service);
        System.out.println("Medication: " + medication);
        System.out.println("Quantity: " + quantity);
        System.out.println("Status: " + status);
        System.out.println("ConsultationNotes: " + consultationNotes);
    }
}