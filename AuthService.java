import java.util.List;
import java.util.Scanner;

public class AuthService {
    private List<Patient> patientList;
    private List<Pharmacist> pharmacistList;
    private List<Doctor> doctorList;
    private List<Administrator> administratorList;

    public AuthService(List<Patient> patientList, List<Doctor> doctorList, List<Pharmacist> pharmacistList, List<Administrator> administratorList) {
        this.patientList = patientList;
        this.pharmacistList = pharmacistList;
        this.doctorList = doctorList;
        this.administratorList = administratorList;
    }

    public User login() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        System.out.print("Enter Hospital ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = null;

        // Determine the list to search based on the prefix of the ID
        if (id.startsWith("D")) {
            user = getUserById(id, doctorList);
        } else if (id.startsWith("A")) {
            user = getUserById(id, administratorList);
        } else if (id.startsWith("P")) {
            // Check both patient and pharmacist lists if ID starts with 'P'
            user = getUserById(id, patientList);
            if (user == null) {  // If not found in patient list, check pharmacist list
                user = getUserById(id, pharmacistList);
            }
        }

        // Authenticate if user is found
        if (user != null && user.authenticate(password)) {
            System.out.println("Login successful. Welcome " + user.getRole() + " " + user.getName() + "!");

            if (user.getFirstLogin()) { // Ask user if they want to change password after initial login
                System.out.println("This is your first login.");
                do {
                    System.out.print("Do you want to change your password?\nEnter Y or N: ");
                    choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("Y")) {
                        user.changePassword();
//                        user.setFirstLogin(false);
                        break;
                    } else if (choice.equalsIgnoreCase("N")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Try again.");
                    }
                } while (true);
            }
            return user;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }

    // Helper method to get user by ID from a specific list of Users
    private User getUserById(String id, List<? extends User> userList) {
        for (User user : userList) {
            if (user.getHospitalID().equals(id)) {
                return user;
            }
        }
        return null; // If no user found
    }
}
