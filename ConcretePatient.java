import java.util.Scanner;

public class ConcretePatient extends Patient implements viewMedicalRecords, PersonalInfoUpdater {

    public ConcretePatient(String hospitalID, String name, String role, String dob, String gender, String bloodType, String contactInformation) {
        super(hospitalID, name, role, dob, gender, bloodType, contactInformation);
    }

    @Override
    public void viewMedicalRecords(User user) {
        // id, name, dob, gender, email, blood type, past diagnosis, treatments
        System.out.println("\nMedical Records for " + user.getName() + "\n");
        System.out.println("-------------------------------------\n" +
                            "Patient ID: " + user.getHospitalID() + "\n" +
                            "Date of Birth: " + getDoB() + "\n" +
                            "Gender: " + getGender() + "\n" +
                            "Email: " + getEmail() + "\n" +
                            "Blood Type: " + getBloodType() + "\n" +
                            "Past Diagnosis: " + getPastDiagnosis() + "\n" +
                            "Past Treatment: " + getPastTreatment()
        );
    }

    @Override
    public void updatePersonalInformation(User user) {
        Scanner sc = new Scanner(System.in);
        boolean exec = true;

        do{
            System.out.print("""
                \nWhat information do you want to update?
                1. Name
                2. Date of Birth
                3. Email
                4. Blood Type
                5. Exit
                Enter Your Choice : 
                """);

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    System.out.print("\nEnter New Name: ");
                    String name = sc.nextLine();
                    user.setName(name);
                    break;
                case 2:
                    System.out.println("\nEnter New Date of Birth:");
                    System.out.print("Enter Day: ");
                    int day = sc.nextInt();
                    System.out.print("Enter Month: ");
                    int month = sc.nextInt();
                    System.out.print("Enter Year: ");
                    int year = sc.nextInt();
                    String dob = String.format("%02d-%02d-%04d", day, month, year);
                    setDob(dob);
                    break;
                case 3:
                    System.out.print("\nEnter New Email: ");
                    String email = sc.nextLine();
                    setEmail(email);
                    break;
                case 4:
                    System.out.print("\nEnter New Blood Type: ");
                    String bloodType = sc.nextLine();
                    setBloodType(bloodType);
                    break;
                case 5:
                    exec = false;
                    break;
                default:
                    System.out.println("Invalid choice");

            }
        }while(exec);
    }
}
