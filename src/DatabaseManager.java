package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:banking.db";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try {
            // Create Users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                )
            """;

            // Create Accounts table
            String createAccountsTable = """
                CREATE TABLE IF NOT EXISTS accounts (
                    account_id TEXT PRIMARY KEY,
                    balance REAL NOT NULL,
                    account_type TEXT NOT NULL,
                    username TEXT NOT NULL,
                    FOREIGN KEY (username) REFERENCES users(username)
                )
            """;

            // Create Transactions table
            String createTransactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    transaction_id TEXT PRIMARY KEY,
                    timestamp TEXT NOT NULL,
                    type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    from_account_id TEXT,
                    to_account_id TEXT,
                    status TEXT NOT NULL,
                    FOREIGN KEY (from_account_id) REFERENCES accounts(account_id),
                    FOREIGN KEY (to_account_id) REFERENCES accounts(account_id)
                )
            """;

            Statement stmt = connection.createStatement();
            stmt.execute(createUsersTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // User operations
    public void addUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Account operations
    public void addAccount(String accountId, double balance, String accountType, String username) {
        String sql = "INSERT INTO accounts (account_id, balance, account_type, username) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            pstmt.setDouble(2, balance);
            pstmt.setString(3, accountType);
            pstmt.setString(4, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Account> getAccountsByUsername(String username) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Account account;
                if (rs.getString("account_type").equals("Checking")) {
                    account = new checkingAccount();
                } else {
                    account = new savingAccount();
                }
                account.setAccountId(rs.getString("account_id"));
                account.setBalance(rs.getDouble("balance"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // Transaction operations
    public void addTransaction(String transactionId, String timestamp, String type, 
                             double amount, String fromAccountId, String toAccountId, String status) {
        String sql = "INSERT INTO transactions (transaction_id, timestamp, type, amount, from_account_id, to_account_id, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, transactionId);
            pstmt.setString(2, timestamp);
            pstmt.setString(3, type);
            pstmt.setDouble(4, amount);
            pstmt.setString(5, fromAccountId);
            pstmt.setString(6, toAccountId);
            pstmt.setString(7, status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactionsByAccountId(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            pstmt.setString(2, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getString("transaction_id"));
                transaction.setTimestamp(rs.getString("timestamp"));
                transaction.setType(rs.getString("type"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setFromAccountId(rs.getString("from_account_id"));
                transaction.setToAccountId(rs.getString("to_account_id"));
                transaction.setStatus(rs.getString("status"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void updateAccountBalance(String accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 