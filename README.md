# SC2002-Assignment

Package org.example has all of Faye's classes in them- I've modified the Patient class and the Doctor Claass 

Patient Menu: Case 8: View Past Appointments Outcome Records left

Doctor Menu cases left- 
Case 1: View Patient Medical Records
Case 2: Update Patient Medical Records
Case 7: Record Appointment Outcome

Package MohikaV1 consists of all the codes for the Appointment System-
1. doctorAppointmentManagement- interface to implement code for doctor accepting/declining appointment request
2. DoctorAppointmentManager- implements (1)
3. DoctorAvailability- doctor sets their own availability
4. doctorViewAppointment- interface to implement doctor viewing functions (personal schedule and upcoming appointments)
upcoming appointments are those that the doctor has accepted
5. DoctorScheduleView- implements (4)
6. patientAppointmentManagement- interface to implement code for patient scheduling/cancelling/rescheduling appointment request
7. PatientAppointmentManager- implements (6)
8. patientViewAppointment- interface to implement doctor viewing functions (appointment slots based on doctor's availability schedule and status of appointments)
9. PatientAppointmentView- implements (8)

# Current Implementation
Current implementation has the doctor giving us in hrs the times they are available (capped from 1000 to 2100)- so they enter say from 1400 to 2000 then the code generates 30 mins intervals and creates a blank document with al neccessary information (Appointment_List.xlsx). This document is then viewed by the patient who based on their availability chooses which slot to book- as soon as they confirm the appointment ID they wanna book the status on the document becomes PENDING. The doctor can view their schedule (viewPersonalSchedule() function) to see which slots have been selected and can accept and decline accordingly.

If the doctor accepts then the status changes to CONFIRMED and if they decline it changes to CANCELLED. Till the doctor has not confirmed if the patient views their upcoming appointment status then they will be seeing the PENDING status. If the doctor accepts then that particular appointment id is reflected to the patient as CONFIRMED and is also added into the doctor's upcoming appointment list (viewUpcomingAppointments() functions which just checks which status is CONFIRMED for the doctor) logged in.

The patient is allowed to cancel/reschedule appointments even if it is still in the PENDING status. If they do cancel or reschedule then the status of the appointment they had booked goes back to UNRESERVED (default status) and the appointment is removed from both the doctor's upcoming appointment list and the patient's appointment list also. 
ALTERNATE FLOW for this one- I'm looking into implementing the following: If the doctor accepts the appointment before the patient cancels/reschedules then for the doctor's upcoming appointment list it should reflect that the patient CANCELLED/RESCHEDULED rather than just making it UNRESERVED 

-----------------

uhh i think that covers everything about my part rn- i dont think i have to do the COMPLETED status part right? also I have yet to add in error cases for all the sections- prolly doing that tomorrow so i'll just update later
