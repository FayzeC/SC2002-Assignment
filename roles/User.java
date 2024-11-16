package roles;

import filemanager.CSVUpdater;
// import org.mindrot.jbcrypt.BCrypt; // uncomment this during submission

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Abstract class representing a User in the hospital system.
 * This class contains common properties and methods for doctors, patients, pharmacists and administrators.
 */
public abstract class User {
    private String hospitalID;
    private String name;
    private String password;
    private String role;
    private String gender;
    private boolean firstLogin;   // Flag to check if it's the user's first login

    /**
     * Constructs a new User instance.
     *
     * @param hospitalID The hospital ID of the user
     * @param name The name of the user
     * @param password The password for the user
     * @param role The role of the user (e.g., doctor, patient)
     * @param gender The gender of the user
     * @param firstLogin A flag indicating whether this is the user's first login
     */
    public User(String hospitalID,  String name, String password, String role, String gender, boolean firstLogin) {
        this.hospitalID = hospitalID;
        this.name = name;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.firstLogin = firstLogin;
    }

    /**
     * Validates the password based on specific criteria (at least 8 characters, one digit, one special character, and one uppercase letter).
     *
     * @param password The password to validate
     * @return true if the password meets the criteria, false otherwise
     */
    private boolean validatePassword(String password) {
        // Password must be more than 8 characters, contain at least one digit, and one special character
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    // /**
    //  * Hashes the password using BCrypt for secure storage.
    //  *
    //  * @param password The password to hash
    //  * @return The hashed password
    //  */
    // Hash password using BCrypt (uncomment this during submission)
//    private String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }

    /**
     * Changes the user's password after validating the new password.
     * The password is updated in the system, and the changes are reflected in the file.
     *
     * @param filePath The file path where the user's information is stored
     * @throws IOException If an error occurs while writing to the file
     */
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

    // /**
    //  * Authenticates the user by comparing the provided password with the hashed password.
    //  *
    //  * @param password The password entered by the user
    //  * @return true if the password matches, false otherwise
    //  */
//    public boolean authenticate(String password) {
//        return BCrypt.checkpw(password, this.password);  // Compares plain-text password with hashed password
//    }

    /**
     * Abstract method to display the user menu.
     * Each subclass (e.g., Doctor, Patient, Pharmacist, Administrator) should implement this method.
     */
    public abstract void displayMenu();

    /**
     * Abstract method to get the logout option for the user.
     * Each subclass (e.g., Doctor, Patient, Pharmacist, Administrator) should implement this method.
     *
     * @return The logout option number
     */
    public abstract int getLogoutOption();

    /**
     * Abstract method to handle the user's menu option.
     * Each subclass (e.g., Doctor, Patient, Pharmacist, Administrator) should implement this method.
     *
     * @param choice The option selected by the user
     * @throws IOException If an error occurs during file I/O
     */
    public abstract void handleOption(int choice) throws IOException;

    // Getters
    /**
     * Gets the hospital ID of the user.
     *
     * @return The user's hospital ID
     */
    public String getHospitalID() { return hospitalID; }

    /**
     * Gets the role of the user.
     *
     * @return The user's role
     */
    public String getRole() { return role; }

    /**
     * Gets the name of the user.
     *
     * @return The user's name
     */
    public String getName() { return name; }

    /**
     * Gets the gender of the user.
     *
     * @return The user's gender
     */
    public String getGender() { return gender; }

    /**
     * Gets the first login status of the user.
     *
     * @return The user's first login status
     */
    public boolean getFirstLogin() { return firstLogin; }

    /**
     * Gets the password of the user.
     *
     * @return The user's password
     */
    public String getPassword() { return password; }

    // Setter
    /**
     * Sets the user's first login flag to false and updates the corresponding information in the file.
     *
     * @param filePath The file path where the user's information is stored
     * @throws IOException If an error occurs while writing to the file
     */
    public void setFirstLogin(String filePath) throws IOException {
        firstLogin = false;
        CSVUpdater.updater(filePath, hospitalID, null, "First Login", "No", 0, 0);
    }

    /**
     * Sets the name of the user.
     *
     * @param name The user's name to be set
     */
    public void setName(String name) { this.name = name; }
}