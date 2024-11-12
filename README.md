# SC2002-Assignment

Package basic has all of Faye's classes in them- I've modified the Patient class and the Doctor Class 

Patient Menu: Case 8: View Past Appointments Outcome Records left

Doctor Menu cases left- 
Case 1: View Patient Medical Records ---- done
Case 2: Update Patient Medical Records
Case 7: Record Appointment Outcome

Package Appointment System consists of (almost) all the codes for my part-

# Current Implementation
Current implementation has the doctor giving us in hrs the times they are available (capped from 1000 to 2100)- so they enter say from 1400 to 2000 then the code generates 30 mins intervals and creates a blank document with all neccessary information (Appointment_List.csv). This document is then viewed by the patient who based on their availability chooses which slot to book- as soon as they confirm the appointment ID they wanna book the status on the document becomes PENDING. The doctor can view their schedule (viewPersonalSchedule() function) to see which slots have been selected and can accept and decline accordingly.

If the doctor accepts then the status changes to CONFIRMED and if they decline it changes to CANCELLED. Till the doctor has not confirmed if the patient views their upcoming appointment status then they will be seeing the PENDING status. If the doctor accepts then that particular appointment id is reflected to the patient as CONFIRMED and is also added into the doctor's upcoming appointment list (viewUpcomingAppointments()) which just checks which status is CONFIRMED for the doctor logged in.

The patient is allowed to cancel/reschedule appointments even if it is still in the PENDING status. If they do cancel or reschedule then the status of the appointment they had booked goes back to UNRESERVED (default status) and the appointment is removed from both the doctor's upcoming appointment list and the patient's appointment list also. 
ALTERNATE FLOW for this one- I'm looking into implementing the following: If the doctor accepts the appointment before the patient cancels/reschedules then for the doctor's upcoming appointment list it should reflect that the patient CANCELLED/RESCHEDULED rather than just making it UNRESERVED 

-----------------
Update: wrote the code for doctor viewing the medical files for all the patients under him/her. Need to just complete the patient viewing AOR records and the doctor update thing and then should be done because whatever update the doctor makes here gets added to the AOR.csv 
