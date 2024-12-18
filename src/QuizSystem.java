import java.sql.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

abstract class Person {
    int id;
    String username;
    String password;

    Person(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    abstract void menu(Scanner scanner);
}

class Admin extends Person {
    Admin(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    void menu(Scanner scanner) {
        QuizSystem.menuAdmin(scanner);
    }
}

class User extends Person {
    User(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    void menu(Scanner scanner) {
        QuizSystem.menuUser(scanner, this.id);
    }
}

class Question {
    String quizId;
    String question;
    String answer;

    Question(String quizId, String question, String answer) {
        this.quizId = quizId;
        this.question = question;
        this.answer = answer;
    }
}

class QuizSystem {
    static final String DB_URL = "jdbc:mysql://localhost:3306/QuizSystem";
    static final String DB_USERNAME = "root"; // 
    static final String DB_PASSWORD = "Keller "; // 

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean isUsernameExists(String username) {
        try {
            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean authenticateAdmin(String username, String password) {
        try {
            String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ? AND role = 'admin'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean authenticateUser(String username, String password) {
        try {
            String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ? AND role = 'user'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static void signUpAdmin(String username, String password) {
        try {
            if (isUsernameExists(username)) {
                System.out.println("Username already exists.");
                return;
            }
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'admin')";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("Admin signed up successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void signUpUser(String username, String password) {
        try {
            if (isUsernameExists(username)) {
                System.out.println("Username already exists.");
                return;
            }
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("User signed up successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static List<Question> getAllQuestions(String quizId) {
        List<Question> questions = new ArrayList<>();
        try {
            String query = "SELECT * FROM questions WHERE quiz_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                        rs.getString("quiz_id"),
                        rs.getString("question"),
                        rs.getString("answer")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    static void recordScore(int userId, String quizId, int score) {
        try {
            String query = "INSERT INTO results (user_id, quiz_id, score, date_taken) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, quizId);
            stmt.setInt(3, score);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
            System.out.println("Score recorded successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static List<String> getScores(int userId) {
        List<String> scores = new ArrayList<>();
        try {
            String query = "SELECT quiz_id, score, date_taken FROM results WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                scores.add("Quiz ID: " + rs.getString("quiz_id") + ", Score: " + rs.getInt("score") +
                        ", Date: " + rs.getTimestamp("date_taken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    static void menuAdmin(Scanner scanner) {
        int choice;
        do {
            try {
                System.out.println("+-----------------------------+");
                System.out.println("| ********* Admin Menu ********* |");
                System.out.println("+-----------------------------+");
                System.out.println("| 1. Add Question             |");
                System.out.println("| 2. View Scores              |");
                System.out.println("| 3. Log out                  |");
                System.out.println("+-----------------------------+");

                System.out.print("Enter choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter quiz ID: ");
                        String quizId = scanner.nextLine();
                        System.out.print("Enter question: ");
                        String question = scanner.nextLine();
                        System.out.print("Enter answer: ");
                        String answer = scanner.nextLine();
                        addQuestion(quizId, question, answer);
                        break;
                    case 2:
                        System.out.print("Enter user ID to view scores: ");
                        int userId = scanner.nextInt();
                        List<String> scores = getScores(userId);
                        if (scores.isEmpty()) {
                            System.out.println("No scores found for this user.");
                        } else {
                            scores.forEach(System.out::println);
                        }
                        break;
                    case 3:
                        System.out.println("Logging out.");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = -1;
            }
        } while (choice != 3);
    }

    static void menuUser(Scanner scanner, int userId) {
        int choice;
        do {
            try {
                System.out.println("+-----------------------------+");
                System.out.println("| ********* User Menu ********* |");
                System.out.println("+-----------------------------+");
                System.out.println("| 1. Take Quiz                |");
                System.out.println("| 2. View Scores              |");
                System.out.println("| 3. Log out                  |");
                System.out.println("+-----------------------------+");

                System.out.print("Enter choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter quiz ID to take the quiz: ");
                        String quizId = scanner.nextLine();
                        takeQuiz(userId, quizId);
                        break;
                    case 2:
                        List<String> scores = getScores(userId);
                        if (scores.isEmpty()) {
                            System.out.println("No scores found for this user.");
                        } else {
                            scores.forEach(System.out::println);
                        }
                        break;
                    case 3:
                        System.out.println("Logging out.");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = -1;
            }
        } while (choice != 3);
    }

    static void addQuestion(String quizId, String question, String answer) {
        try {
            String query = "INSERT INTO questions (quiz_id, question, answer) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, quizId);
            stmt.setString(2, question);
            stmt.setString(3, answer);
            stmt.executeUpdate();
            System.out.println("Question added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void takeQuiz(int userId, String quizId) {
        List<Question> questions = getAllQuestions(quizId);
        if (questions.isEmpty()) {
            System.out.println("No questions found for this quiz.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        int score = 0;

        for (Question question : questions) {
            System.out.println(question.question);
            System.out.print("Your answer: ");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase(question.answer)) {
                score++;
            }
        }

        System.out.println("Your score: " + score);
        recordScore(userId, quizId, score);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            try {
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
                        System.out.print("Enter username: ");
                        String adminUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String adminPassword = scanner.nextLine();
                        signUpAdmin(adminUsername, adminPassword);
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        String userUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String userPassword = scanner.nextLine();
                        signUpUser(userUsername, userPassword);
                        break;

                    case 3:
                        System.out.print("Enter username: ");
                        String loginAdminUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginAdminPassword = scanner.nextLine();
                        if (authenticateAdmin(loginAdminUsername, loginAdminPassword)) {
                            Admin admin = new Admin(1, loginAdminUsername, loginAdminPassword);
                            admin.menu(scanner);
                        } else {
                            System.out.println("Invalid admin credentials.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter username: ");
                        String loginUserUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginUserPassword = scanner.nextLine();
                        if (authenticateUser(loginUserUsername, loginUserPassword)) {
                            User user = new User(1, loginUserUsername, loginUserPassword);
                            user.menu(scanner);
                        } else {
                            System.out.println("Invalid user credentials.");
                        }
                        break;

                    case 5:
                        System.out.println("Exiting application.");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = -1;
            }
        } while (choice != 5);
    }
}
