# Setting up the project
- If you are using IntelliJ, you need a project with Maven. Check this link to see how to add Maven to an existing project https://www.jetbrains.com/help/idea/convert-a-regular-project-into-a-maven-project.html#add_maven_support or find out how to create a projec with Maven yourself.
- Copy my pom.xml file. Make sure your groupID is valid (I think can just follow mine). For artifactId, it should be your project folder name (your project folder name should'nt have spaces cuz it wont work with Maven)
- After you do all this, if it still doesn't work then try relinking Maven like so: https://imgur.com/2bVzAuf
- Run HospitalManagementClass to start the program.

# Explanation of the files
- HospitalManagementClass - runs an instance of HMS (don't think yall need to touch this file)
- RoleAccessController - allows the system to run menus of specific users (don't think yall need to touch this file)
- FilePaths - store constant filepaths, if you need the filepaths anywhere in your code just use FilePaths.<whatever path you need>
- ExcelLoader - contains everything related to loading stuff from excel lists, each list has its respective load function. If you want to add/remove any columns from the excel file, just modify the helper method that retrieves String value in the load functions. After that update the object attributes if needed
- ExcelUpdater (For now it seems to be a universal function, haven't tested for appointment stuff and inventory) - pass in filepath, ID, the header name of the column to be updated, the new value to modify a specific cell in a file. to call add this line ExcelUpdater excelUpdater = new ExcelUpdater(); then pass your parameters like this excelUpdater.updater(FilePaths.PATIENT_LIST_PATH, getHospitalID(), "Name", name);
- AuthService - runs the ExcelLoader and loads everything into their respective lists + handles login
- Administrator, Appointment, AppointmentOutcomeRecord, Doctor, Inventory, Patient, Pharmacist are just the objects, they are used to create lists of objects
- User - base class for admin, doctor, pharmacist, patient
- InformationAccess - interface
- ConcretePatient - extends Patient and implements interface functions

# Current feature implementation 
(Idk if this applies to how yall gna implement but this is how i did it, whatever function u gna call to handle the user choice should be in the handleOption function in each role tho)
If you find this confusing you can just look at these files for each step (1) InformationAccess interface, (2) ConcretePatient, (3) Patient (line 14, 94), (4) ExcelLoader in loadPatient line 76 and 77, (5) Patient in handleOption
1. Define whatever function in an interface class
2. Create a concrete class that implements that function. Have that concrete class extends a role and define the relevant attributes. 
3. In the role class like Patient class, define the interface (e.g. private InformationAccess informationAccess;) and add this setter (e.g. public void setInformationAccess(InformationAccess informationAccess) { this.informationAccess = informationAccess; }).
4. In the respective loader class in ExcelLoader, call the setter function (e.g. patient.setInformationAccess(patient);) and change the object to ConcretePatient also
5. Finally you can call it in Patient class like so (e.g. informationAccess.viewMedicalRecords(this); )

ALSO PLEASE REMIND ME TO ADD PASSWORD VALIDATION WHEN WE ARE DONE CODING ALL THE FEATURES
