import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileManager {
    private static FileManager instance;
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String USERS_FILE = "users.txt";

    private FileManager() {}

    public static synchronized FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    // User operations
    public void saveUser(User user) {
        try (FileWriter fw = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user.getUsername() + "," + user.getPassword() + "," + user.getRole());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User loadUser(String username) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    // Account operations
    public void saveAccount(Account account, String username) {
        // Implementation needed
    }

    public List<Account> loadAccounts(String username) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    public void updateAccountBalance(String accountId, double newBalance) {
        try {
            File inputFile = new File(ACCOUNTS_FILE);
            File tempFile = new File("temp.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(accountId)) {
                    writer.write(accountId + "," + newBalance + "," + parts[2] + "," + parts[3]);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
            
            writer.close();
            reader.close();
            
            if (!tempFile.renameTo(inputFile)) {
                throw new IOException("Failed to update account balance");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Transaction operations
    public void saveTransaction(Transaction transaction) {
        try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(transaction.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> loadTransactions(String accountId) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    // Helper methods
    public String generateId() {
        return UUID.randomUUID().toString();
    }
} 