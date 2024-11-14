package roles;

import filemanager.CSVUpdater;
// import org.mindrot.jbcrypt.BCrypt; // uncomment this during submission

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class User {
    private String hospitalID;
    private String name;
    private String password;
    private String role;
    private String gender;
    private boolean firstLogin;   // Flag to check if it's the user's first login

    public User(String hospitalID,  String name, String password, String role, String gender, boolean firstLogin) {
        this.hospitalID = hospitalID;
        this.name = name;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.firstLogin = firstLogin;
    }

    // Password validation method
    private boolean validatePassword(String password) {
        // Password must be more than 8 characters, contain at least one digit, and one special character
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    // Hash password using BCrypt (uncomment this during submission)
//    private String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }

    // Method to change password
    public void changePassword(String filePath) throws IOException {
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.print("Please enter your new password: ");
            String newPassword = scanner.nextLine();
            if (!validatePassword(newPassword)) {
                System.out.println("Password must be at least 8 characters long and contain at least one uppercase letter, one special character and one number.");
            }else{
//                this.password = hashPassword(newPassword); // uncomment this during submission
                 this.password = newPassword; // remove this during submission
                System.out.println("Password changed successfully.");
//                CSVUpdater.updater(filePath, hospitalID, null, "Password", this.password, 0, 0); // uncomment this during submission
                CSVUpdater.updater(filePath, hospitalID, null, "Password", newPassword, 0, 0); // remove this during submission
                break;
            }
        }while(true);
    }

    // Method to authenticate user (remove this function during submission)
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    // Authenticate by comparing hashed password (Uncomment this during submission)
//    public boolean authenticate(String password) {
//        return BCrypt.checkpw(password, this.password);  // Compares plain-text password with hashed password
//    }


    public abstract void displayMenu();
    public abstract int getLogoutOption();
    public abstract void handleOption(int choice) throws IOException;

    // Getters
    public String getHospitalID() { return hospitalID; }

    public String getRole() { return role; }

    public String getName() { return name; }

    public String getGender() { return gender; }

    public boolean getFirstLogin() { return firstLogin; }

    public String getPassword() { return password; }

    // Setter
    public void setFirstLogin(String filePath) throws IOException {
        firstLogin = false;
        CSVUpdater.updater(filePath, hospitalID, null, "First Login", "No", 0, 0);
    }

    public void setName(String name) { this.name = name; }
}
