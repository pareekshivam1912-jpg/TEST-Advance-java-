import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class RegisterUser {

    // database settings
    static String url = "jdbc:mysql://localhost:3306/testdb?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false";
    static String user = "root";
    static String pass = "Shivam@1912#";

    public static void main(String[] args) {
        System.out.println("Enter user record");

        String name = "Amit Sharma";
        String email = "amit@gmail.com";
        String password = "mypassword123";

        // input validation
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Error: All fields are required");
            return;
        }

        Connection conn = null;
        Statement setupStmt = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            // connect to database
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to MySQL");

            // create users table if not exists
            setupStmt = conn.createStatement();
            setupStmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100), "
                    + "email VARCHAR(100) UNIQUE, "
                    + "password VARCHAR(100))");

            // check if email already exists
            String checkQuery = "SELECT id FROM users WHERE email = ?";
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);

            rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Registration failed: Email already registered");
                return;
            }

            // insert new user
            String insertQuery = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, name);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);

            int rows = insertStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("User registered successfully");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Registration failed: Duplicate email address");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());

        } finally {
            // clean up resources
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (setupStmt != null) setupStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
