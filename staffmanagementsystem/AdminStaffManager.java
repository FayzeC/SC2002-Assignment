package staffmanagementsystem;

import roles.Administrator;
import roles.Doctor;
import roles.Pharmacist;
import roles.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The AdminStaffManager class manages the staff-related functionalities for the administrator.
 * It provides methods for viewing, filtering, adding, updating, and removing staff members.
 * The class uses services like {@link StaffCreationService} and {@link StaffRemovalService}
 * to ensure proper adherence to business logic and validation.
 */
public class AdminStaffManager implements StaffManager {
    private final StaffDisplay staffDisplay;           // For displaying staff information
    private final List<User> staffList;                // In-memory list of staff members
    private final StaffRepository staffRepository;     // For loading/saving staff data
    StaffValidator validator;                          // For validating input data
    private StaffCreationService staffCreationService; // Service for creating staff members
    private StaffRemovalService staffRemovalService;   // Service for removing staff members

    /**
     * Constructs an instance of AdminStaffManager with dependencies for staff management.
     *
     * @param staffRepository The repository for loading and saving staff data.
     * @param staffDisplay    The display service for showing staff information.
     * @throws IOException If there is an issue loading the initial staff data.
     */
    public AdminStaffManager(StaffRepository staffRepository, StaffDisplay staffDisplay) throws IOException {
        this.staffRepository = staffRepository; // Assign repository dependency
        this.staffDisplay = staffDisplay;       // Assign display dependency
        this.staffList = new ArrayList<>(staffRepository.loadAllStaff()); // Load staff list
        this.staffCreationService = new StaffCreationService();
        this.staffRemovalService = new StaffRemovalService();
        this.validator = new StaffValidator();
    }

    /**
     * Displays all staff members and optionally applies filters based on user input.
     */
    @Override
    public void viewAndFilterStaff() {
        Scanner scanner = new Scanner(System.in);

        // Display all staff using StaffDisplay
        System.out.println("========== Existing Staffs ==========");
        staffDisplay.displayAllStaff(staffList);
        System.out.println("Total number of staffs: " + staffList.size());

        // Ask if filtering is required
        String wantsToFilter;
        while (true) {
            System.out.print("Do you want to apply filters? (yes/no): ");
            wantsToFilter = scanner.nextLine().trim().toLowerCase();

            if (wantsToFilter.equals("yes") || wantsToFilter.equals("no")) {
                break;
            }
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
        }

        if (!wantsToFilter.equals("yes")) {
            System.out.println("No filters applied.");
            return;
        }

        // Prompt the user for a filter choice
        int choice = -1;
        while (true) {
            System.out.println("Filter by:");
            System.out.println("1. Role");
            System.out.println("2. Age");
            System.out.println("3. Gender");
            System.out.print("Choose a filter option (1, 2, or 3): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice >= 1 && choice <= 3) {
                    break;
                }
            } else {
                scanner.nextLine(); // Consume invalid input
            }

            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
        }

        FilterCriteria selectedFilter = null;
        switch (choice) {
            case 1: // Role filter
                String role;
                while (true) {
                    System.out.print("Enter Role to filter by (e.g., Doctor, Pharmacist, Administrator): ");
                    role = scanner.nextLine().trim();

                    if (validator.isValidRole(role)) {
                        selectedFilter = new RoleFilter(role);
                        break;
                    }
                    System.out.println("Invalid role. Please enter 'Doctor', 'Pharmacist', or 'Administrator'.");
                }
                break;

            case 2: // Age filter
                System.out.print("Enter Age to filter by: ");
                String age = scanner.nextLine().trim();
                selectedFilter = new AgeFilter(age); // Assuming no specific validation for age
                break;

            case 3: // Gender filter
                String gender;
                while (true) {
                    System.out.print("Enter Gender to filter by (e.g., Male, Female): ");
                    gender = scanner.nextLine().trim();

                    if (validator.isValidGender(gender)) {
                        selectedFilter = new GenderFilter(gender);
                        break;
                    }
                    System.out.println("Invalid gender. Please enter 'Male' or 'Female'.");
                }
                break;
        }

        // Apply the selected filter
        List<User> filteredStaff = selectedFilter.filter(staffList);

        // Display the filtered results
        System.out.println("========== Filtered Staffs ==========");
        staffDisplay.displayFilteredStaff(filteredStaff);
        System.out.println("Total number of filtered staffs: " + filteredStaff.size());
    }

    /**
     * Adds a new staff member by gathering input data from the administrator.
     *
     * @param scanner The scanner for reading user input.
     * @throws IOException If there is an issue saving the updated staff data.
     */
    @Override
    public void addStaff(Scanner scanner) throws IOException {
        String id, role;

        // Loop until a valid unique ID is entered
        while (true) {
            System.out.print("Enter Hospital ID: ");
            id = scanner.nextLine();

            System.out.print("Enter Role (Doctor, Pharmacist, Administrator): ");
            role = validator.capitalize(scanner.nextLine()); // Use the StaffValidator for capitalization

            if (!validator.isValidRole(role)) {
                System.out.println("Invalid role. Please enter either 'Doctor', 'Pharmacist', or 'Administrator'.");
                continue;
            }

            if (!validator.isValidIDForRole(id, role)) {
                System.out.println(role + " ID must start with '" + role.charAt(0) + "'. Please enter a valid ID.");
                continue;
            }

            if (!validator.isUniqueID(id, staffList)) {
                System.out.println("Hospital ID " + id + " is already taken. Please enter a different ID.");
            } else {
                break;
            }
        }

        // Get name, gender, and age with validation
        System.out.print("Enter Name: ");
        String name = validator.capitalize(scanner.nextLine());

        String gender;
        while (true) {
            System.out.print("Enter Gender (Male/Female): ");
            gender = validator.capitalize(scanner.nextLine());
            if (validator.isValidGender(gender)) break;
            System.out.println("Invalid input. Please enter 'Male' or 'Female'.");
        }

        System.out.print("Enter Age: ");
        String age = scanner.nextLine();

        // Default password setup
        String password = "defaultPassword";

        // Use StaffCreationService to create the new staff member
        User newUser = staffCreationService.createStaffMember(id, role, name, gender, age, password);

        // Add the new user to the staff list and save the updated list
        staffList.add(newUser);
        staffRepository.saveAllStaff(staffList);

        System.out.println("Added new staff member: " + name);
        System.out.println("Total number of staff members: " + staffList.size());
    }

    /**
     * Updates an existing staff member's information based on user input.
     *
     * @param scanner The scanner for reading user input.
     * @throws IOException If there is an issue saving the updated staff data.
     */
    @Override
    public void updateStaff(Scanner scanner) throws IOException {
        // Step 1: Get the Hospital ID of the staff to update
        User staffToUpdate;
        while (true) {
            System.out.print("Enter Hospital ID of staff to update: ");
            String id = scanner.nextLine();

            // Find the staff member
            staffToUpdate = staffList.stream()
                    .filter(staff -> staff.getHospitalID().equals(id))
                    .findFirst()
                    .orElse(null);

            if (staffToUpdate != null) {
                break;
            }
            System.out.println("Staff with Hospital ID " + id + " not found. Please try again.");
        }

        // Step 2: Prompt for the field to update
        System.out.println("Fields available for update: Name, Age, Gender, Role");
        System.out.print("Enter field to update: ");
        String field = scanner.nextLine().toLowerCase();

        while (!List.of("name", "age", "gender", "role").contains(field)) {
            System.out.println("Invalid field. Only 'Name', 'Age', 'Gender', and 'Role' can be updated.");
            System.out.print("Enter a valid field to update: ");
            field = scanner.nextLine().toLowerCase();
        }

        // Initialize fields for the new staff object
        String updatedName = staffToUpdate.getName();
        String updatedGender = staffToUpdate.getGender();
        String updatedAge = null;
        String updatedRole = staffToUpdate.getRole();
        String updatedId = staffToUpdate.getHospitalID();

        if (staffToUpdate instanceof Doctor doctor) {
            updatedAge = doctor.getAge();
        } else if (staffToUpdate instanceof Pharmacist pharmacist) {
            updatedAge = pharmacist.getAge();
        } else if (staffToUpdate instanceof Administrator administrator) {
            updatedAge = administrator.getAge();
        }

        // Step 3: Handle field-specific logic
        switch (field) {
            case "name" -> {
                System.out.print("Enter new Name: ");
                updatedName = validator.capitalize(scanner.nextLine());
            }
            case "age" -> {
                System.out.print("Enter new Age: ");
                updatedAge = scanner.nextLine();
            }
            case "gender" -> {
                while (true) {
                    System.out.print("Enter new Gender (Male/Female): ");
                    String gender = validator.capitalize(scanner.nextLine());
                    if (validator.isValidGender(gender)) {
                        updatedGender = gender;
                        break;
                    } else {
                        System.out.println("Invalid gender. Please enter 'Male' or 'Female'.");
                    }
                }
            }
            case "role" -> {
                while (true) {
                    System.out.print("Enter new Role (Doctor, Pharmacist, Administrator): ");
                    String role = validator.capitalize(scanner.nextLine());
                    if (!validator.isValidRole(role)) {
                        System.out.println("Invalid role. Please enter 'Doctor', 'Pharmacist', or 'Administrator'.");
                        continue;
                    }
                    if (role.equalsIgnoreCase(updatedRole)) {
                        System.out.println("New role is the same as the current role. Update canceled.");
                        return;
                    }
                    updatedRole = role;
                    break;
                }

                // Prompt for a new Hospital ID if the role changes
                while (true) {
                    System.out.print("Enter new Hospital ID for the role '" + updatedRole + "': ");
                    String newId = scanner.nextLine();
                    if (validator.isValidIDForRole(newId, updatedRole) && validator.isUniqueID(newId, staffList)) {
                        updatedId = newId;
                        break;
                    } else {
                        System.out.println("Invalid or duplicate ID. Ensure the prefix matches the role (e.g., 'D' for Doctor).");
                    }
                }
            }
        }

        // Step 4: Create the updated staff member using StaffCreationService
        User updatedStaff = staffCreationService.createStaffMember(
                updatedId, updatedRole, updatedName, updatedGender, updatedAge, staffToUpdate.getPassword()
        );

        // Step 5: Remove the old user and add the updated user
        StaffRemovalService.removeStaffById(staffToUpdate.getHospitalID(), staffList);
        staffList.add(updatedStaff);

        // Step 6: Save the updated staff list
        staffRepository.saveAllStaff(staffList);

        System.out.println("Staff data updated successfully.");
    }

    /**
     * Removes an existing staff member based on the provided Hospital ID.
     *
     * @param scanner The scanner to read user input.
     * @throws IOException If an error occurs during saving staff data.
     */
    @Override
    public void removeStaff(Scanner scanner) throws IOException {
        while (true) {
            System.out.print("Enter Hospital ID of staff to remove: ");
            String id = scanner.nextLine();

            // Check if the ID exists in the staff list
            if (staffList.stream().anyMatch(staff -> staff.getHospitalID().equals(id))) {
                StaffRemovalService.removeStaffById(id, staffList); // Pass only `id` and `staffList`
                System.out.println("Staff member with ID " + id + " has been removed.");
                System.out.println("Total number of staff members: " + staffList.size());
                staffRepository.saveAllStaff(staffList);
                break;
            } else {
                System.out.println("Staff member with ID " + id + " not found. Please enter a valid Hospital ID.");
            }
        }
    }
}
