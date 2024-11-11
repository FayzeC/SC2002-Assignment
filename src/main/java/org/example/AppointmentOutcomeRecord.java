package org.example;

public class AppointmentOutcomeRecord {
    private String appointmentID;
    private String patientID;
    private String doctorAssigned;
    private String appointmentDate;
    private String service;
    private String medication;
    private String consultationNotes;

    public AppointmentOutcomeRecord(String appointmentID, String patientID, String doctorAssigned, String appointmentDate, String service, String medication, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorAssigned = doctorAssigned;
        this.appointmentDate = appointmentDate;
        this.service = service;
        this.medication = medication;
        this.consultationNotes = consultationNotes;
    }

    public void print(){ // Remove after final submission this is just to check if data is loaded correctly
        System.out.println("AppointmentID: " + appointmentID);
        System.out.println("PatientID: " + patientID);
        System.out.println("DoctorAssigned: " + doctorAssigned);
        System.out.println("AppointmentDate: " + appointmentDate);
        System.out.println("Service: " + service);
        System.out.println("Medication: " + medication);
        System.out.println("ConsultationNotes: " + consultationNotes);
    }
}
