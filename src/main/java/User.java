import java.io.IOException;
import java.util.Scanner;

public abstract class User {
    private String hospitalID;
    private String name;
    private String password;
    private String role;
    private String gender;
    private boolean firstLogin; // Flag to check if it's the user's first login
    // CSVUpdater csvUpdater = new CSVUpdater();

    public User(String hospitalID, String name, String password, String role, String gender, boolean firstLogin) {
        this.hospitalID = hospitalID;
        this.name = name;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.firstLogin = firstLogin;
    }

    // Method to change password
    public void changePassword(String filePath) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your new password: ");
        String newPassword = scanner.nextLine();
        this.password = newPassword;
        System.out.println("Password changed successfully.");
        CSVUpdater.updater(filePath, hospitalID, "Password", newPassword);
    }

    // Method to authenticate user
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public abstract void displayMenu();

    public abstract int getLogoutOption();

    public abstract void handleOption(int choice) throws IOException;

    // Getters
    public String getHospitalID() {
        return hospitalID;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public boolean getFirstLogin() {
        return firstLogin;
    }

    public String getPassword() {
        return password;
    }

    // Setter
    public void setFirstLogin(String filePath) throws IOException {
        firstLogin = false;
        CSVUpdater.updater(filePath, hospitalID, "First Login", "No");
    }

    public void setName(String name) {
        this.name = name;
    }
}