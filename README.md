Just run HospitalManagementClass.java to start the program
- LoadStaff and LoadPatient.java loads into patient, doctor, pharmacist, administrator list
- Laodinventory loads into inventory list
- Authservice only handles login
- Concrete Patient implements the interface functions for and is used in handleOptions in Patient class \
Err basically I
- provide the implementation in the ConcretePatient class
- inject the implementation in Patient class (e.g. private PersonalInfoUpdater personalInfoUpdater;)
- add a setter e.g. (public void setPersonalInfoUpdater(PersonalInfoUpdater updater) { this.personalInfoUpdater = updater; }) in Patient class
- in loadpatient.java add this line patient.setPersonalInfoUpdater(patient);
- then you can call it in handleOptions like this personalInfoUpdater.updatePersonalInformation(this);

Idk if there's a better way to do this T_T
