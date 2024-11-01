import java.util.Scanner;

public abstract class User {
    private String hospitalID;
    private String name;
    private String password;
    private String role;
    private boolean firstLogin;   // Flag to check if it's the user's first login

    public User(String hospitalID,  String name, String role) {
        this.hospitalID = hospitalID;
        this.name = name;
        this.role = role;
        this.password = "password";
        this.firstLogin = true; // Assume it's the first login when user is created
    }

    // Method to authenticate user
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    // Method to change password
    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your new password: ");
        this.password = scanner.nextLine();;
        System.out.println("Password changed successfully.");
        this.firstLogin = false;
    }

    public abstract void displayMenu();
    public abstract int getLogoutOption();
    public abstract void handleOption(int choice);

    // Getters
    public String getName() { return name; }

    public String getRole() { return role; }

    public boolean getFirstLogin() { return firstLogin; }

    public String getHospitalID() { return hospitalID; }

    // Setters
    public void setName(String name) { this.name = name; }

    // Other shared user methods can go here
}
