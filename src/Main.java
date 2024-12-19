import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("+-----------------------------+");
            System.out.println("|        Quiz System          |");
            System.out.println("+-----------------------------+");
            System.out.println("| 1. Sign Up Admin            |");
            System.out.println("| 2. Sign Up User             |");
            System.out.println("| 3. Admin Login              |");
            System.out.println("| 4. User Login               |");
            System.out.println("| 5. Exit                     |");
            System.out.println("+-----------------------------+");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // Admin Sign Up
                    System.out.print("Enter username: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String adminPassword = scanner.nextLine();
                    QuizDBOperations.addUser(adminUsername, adminPassword, "Admin");
                    break;

                case 2: // User Sign Up
                    System.out.print("Enter username: ");
                    String userUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String userPassword = scanner.nextLine();
                    QuizDBOperations.addUser(userUsername, userPassword, "User");
                    break;

                case 3: // Admin Login
                    System.out.print("Enter username: ");
                    String loginAdminUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginAdminPassword = scanner.nextLine();
                    if (QuizDBOperations.authenticateUser(loginAdminUsername, loginAdminPassword, "Admin")) {
                        System.out.println("Admin logged in successfully.");
                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                    break;

                case 4: // User Login
                    System.out.print("Enter username: ");
                    String loginUserUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginUserPassword = scanner.nextLine();
                    if (QuizDBOperations.authenticateUser(loginUserUsername, loginUserPassword, "User")) {
                        System.out.println("User logged in successfully.");
                    } else {
                        System.out.println("Invalid user credentials.");
                    }
                    break;

                case 5: // Exit
                    System.out.println("Exiting the application...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}
