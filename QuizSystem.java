import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class QuizSystem {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz_system";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            System.out.println("Connected to the database successfully!");

            do {
                System.out.println("+-----------------------------+");
                System.out.println("| ********** Main Menu ********* |");
                System.out.println("+-----------------------------+");
                System.out.println("| 1. Admin SignUp             |");
                System.out.println("| 2. User SignUp              |");
                System.out.println("| 3. Admin Login              |");
                System.out.println("| 4. User Login               |");
                System.out.println("| 5. Exit                     |");
                System.out.println("+-----------------------------+");

                System.out.print("Enter choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        signUp(connection, scanner, "admin");
                        break;
                    case 2:
                        signUp(connection, scanner, "user");
                        break;
                    case 3:
                        login(connection, scanner, "admin");
                        break;
                    case 4:
                        login(connection, scanner, "user");
                        break;
                    case 5:
                        System.out.println("Exiting application.");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 5);

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static void signUp(Connection connection, Scanner scanner, String role) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            String hashedPassword = hashPassword(password); // Replace with a real hashing method.

            String query = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, role);
                stmt.executeUpdate();
                System.out.println(role + " signed up successfully.");
            }

        } catch (SQLException e) {
            System.out.println("Error signing up: " + e.getMessage());
        }
    }

    private static void login(Connection connection, Scanner scanner, String role) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            String query = "SELECT * FROM users WHERE username = ? AND role = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, role);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password_hash");
                    if (password.equals(storedPassword)) { // Replace with real password validation.
                        System.out.println(role + " login successful.");
                        if (role.equals("admin")) {
                            adminMenu(connection, scanner);
                        } else {
                            int userId = rs.getInt("id");
                            userMenu(connection, scanner, userId);
                        }
                    } else {
                        System.out.println("Invalid password.");
                    }
                } else {
                    System.out.println("Invalid username or role.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    private static void adminMenu(Connection connection, Scanner scanner) {
        int choice;
        do {
            System.out.println("+----------------------------------+");
            System.out.println("| ******** Admin Menu ************ |");
            System.out.println("+----------------------------------+");
            System.out.println("| 1. Add Quiz Question            |");
            System.out.println("| 2. View All Questions           |");
            System.out.println("| 3. Logout                       |");
            System.out.println("+----------------------------------+");
            System.out.print("Choose option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addQuestion(connection, scanner);
                    break;
                case 2:
                    viewAllQuestions(connection, scanner);
                    break;
                case 3:
                    System.out.println("Admin logged out.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 3);
    }

    private static void userMenu(Connection connection, Scanner scanner, int userId) {
        int choice;
        do {
            System.out.println("+----------------------------------+");
            System.out.println("| ******** User Menu ************* |");
            System.out.println("+----------------------------------+");
            System.out.println("| 1. Take Quiz                    |");
            System.out.println("| 2. View Scores                  |");
            System.out.println("| 3. Logout                       |");
            System.out.println("+----------------------------------+");
            System.out.print("Choose option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    takeQuiz(connection, scanner, userId);
                    break;
                case 2:
                    viewScores(connection, userId);
                    break;
                case 3:
                    System.out.println("User logged out.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 3);
    }

    private static void addQuestion(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Quiz ID: ");
            String quizId = scanner.nextLine();
            System.out.print("Enter Question: ");
            String question = scanner.nextLine();
            System.out.print("Enter Answer: ");
            String answer = scanner.nextLine();

            String query = "INSERT INTO questions (quiz_id, question, answer) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, quizId);
                stmt.setString(2, question);
                stmt.setString(3, answer);
                stmt.executeUpdate();
                System.out.println("Question added successfully.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding question: " + e.getMessage());
        }
    }

    private static void viewAllQuestions(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Quiz ID: ");
            String quizId = scanner.nextLine();

            String query = "SELECT * FROM questions WHERE quiz_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, quizId);
                ResultSet rs = stmt.executeQuery();

                System.out.println("+---------------------------------+");
                System.out.println("| Questions for Quiz ID: " + quizId + "         |");
                System.out.println("+---------------------------------+");
                while (rs.next()) {
                    System.out.println("Question: " + rs.getString("question"));
                    System.out.println("Answer: " + rs.getString("answer"));
                    System.out.println();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error viewing questions: " + e.getMessage());
        }
    }

    private static void takeQuiz(Connection connection, Scanner scanner, int userId) {
        try {
            System.out.print("Enter Quiz ID: ");
            String quizId = scanner.nextLine();

            String query = "SELECT * FROM questions WHERE quiz_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, quizId);
                ResultSet rs = stmt.executeQuery();

                int score = 0;
                while (rs.next()) {
                    System.out.println(rs.getString("question"));
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase(rs.getString("answer"))) {
                        score++;
                    }
                }

                String insertResultQuery = "INSERT INTO results (user_id, quiz_id, score, date_taken) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertResultQuery)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, quizId);
                    insertStmt.setInt(3, score);
                    insertStmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    insertStmt.executeUpdate();
                }

                System.out.println("Quiz completed. Your score: " + score);
            }

        } catch (SQLException e) {
            System.out.println("Error taking quiz: " + e.getMessage());
        }
    }

    private static void viewScores(Connection connection, int userId) {
        try {
            String query = "SELECT * FROM results WHERE user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                System.out.println("+---------------------------------+");
                System.out.println("| Quiz Scores                    |");
                System.out.println("+---------------------------------+");
                while (rs.next()) {
                    System.out.println("Quiz ID: " + rs.getString("quiz_id"));
                    System.out.println("Score: " + rs.getInt("score"));
                    System.out.println("Date Taken: " + rs.getTimestamp("date_taken"));
                    System.out.println();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error viewing scores: " + e.getMessage());
        }
    }

    private static String hashPassword(String password) {
        // Placeholder for real password hashing
        return password;
    }
}
