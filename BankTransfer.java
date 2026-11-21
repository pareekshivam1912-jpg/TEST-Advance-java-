import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankTransfer {

    // database settings
    static String url = "jdbc:mysql://localhost:3306/bankdb?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false";
    static String user = "root";
    static String pass = "Shivam@1912#";

    public static void main(String[] args) {
        System.out.println("Enter transfer record");

        String fromAcc = "A";
        String toAcc = "B";
        double amount = 10000;

        Connection con = null;
        Statement setupStmt = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;

        try {
            // connect to database
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connected");

            // create tables if not present
            setupStmt = con.createStatement();
            setupStmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts ("
                    + "account_no VARCHAR(20) PRIMARY KEY, "
                    + "balance DECIMAL(15, 2))");
            setupStmt.executeUpdate("CREATE TABLE IF NOT EXISTS transaction_history ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "from_acc VARCHAR(20), "
                    + "to_acc VARCHAR(20), "
                    + "amount DECIMAL(15, 2), "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            setupStmt.executeUpdate("INSERT IGNORE INTO accounts (account_no, balance) VALUES ('A', 50000.00)");
            setupStmt.executeUpdate("INSERT IGNORE INTO accounts (account_no, balance) VALUES ('B', 20000.00)");

            // turn off auto commit to manage transaction manually
            con.setAutoCommit(false);

            // check sender balance
            String sql = "SELECT balance FROM accounts WHERE account_no = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, fromAcc);
            rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Current balance: " + balance);

                if (balance >= amount) {

                    // deduct money from sender
                    String debit = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
                    ps1 = con.prepareStatement(debit);
                    ps1.setDouble(1, amount);
                    ps1.setString(2, fromAcc);
                    ps1.executeUpdate();

                    // add money to receiver
                    String credit = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";
                    ps2 = con.prepareStatement(credit);
                    ps2.setDouble(1, amount);
                    ps2.setString(2, toAcc);
                    ps2.executeUpdate();

                    // save record in transaction history
                    String history = "INSERT INTO transaction_history (from_acc, to_acc, amount) VALUES (?, ?, ?)";
                    ps3 = con.prepareStatement(history);
                    ps3.setString(1, fromAcc);
                    ps3.setString(2, toAcc);
                    ps3.setDouble(3, amount);
                    ps3.executeUpdate();

                    // commit transaction if all succeed
                    con.commit();
                    System.out.println("Money transferred successfully");

                } else {
                    System.out.println("Insufficient balance");
                    con.rollback();
                }

            } else {
                System.out.println("Sender account not found");
                con.rollback();
            }

        } catch (SQLException e) {
            // rollback changes if error occurs
            System.out.println("Transaction failed: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println("Rollback completed");
                } catch (SQLException ex) {
                    System.out.println("Rollback error: " + ex.getMessage());
                }
            }
        } finally {
            // clean up and close connection
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (ps3 != null) ps3.close();
                if (setupStmt != null) setupStmt.close();

                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
