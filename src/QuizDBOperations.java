import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDBOperations {

    // Add a user (Admin or User)
    public static void addUser(String username, String password, String role) {
        String query = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Authenticate a user
    public static boolean authenticateUser(String username, String password, String role) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ? AND role = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If a result exists, authentication is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add a quiz question
    public static void addQuizQuestion(String quizId, String question, String answer) {
        String query = "INSERT INTO Questions (quiz_id, question, answer) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, quizId);
            stmt.setString(2, question);
            stmt.setString(3, answer);
            stmt.executeUpdate();
            System.out.println("Question added to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get questions by quiz ID
    public static List<String> getQuestionsByQuizId(String quizId) {
        List<String> questions = new ArrayList<>();
        String query = "SELECT question FROM Questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(rs.getString("question"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Add a quiz result
    public static void addQuizResult(int userId, String quizId, int score) {
        String query = "INSERT INTO Results (user_id, quiz_id, score, date_taken) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, quizId);
            stmt.setInt(3, score);
            stmt.executeUpdate();
            System.out.println("Result saved to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get quiz results for a user
    public static void viewUserResults(int userId) {
        String query = "SELECT quiz_id, score, date_taken FROM Results WHERE user_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.printf("Quiz ID: %s | Score: %d | Date Taken: %s\n",
                        rs.getString("quiz_id"), rs.getInt("score"), rs.getString("date_taken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
