package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class DataMigration {
    private static final String BASE_PATH = Paths.get("").toAbsolutePath().toString();
    private static final String USERS_FILE = BASE_PATH + "/OOP_Project/users.txt";
    private static final String ACCOUNTS_FILE = BASE_PATH + "/OOP_Project/accounts.txt";
    private static final String TRANSACTIONS_FILE = BASE_PATH + "/OOP_Project/transactions.txt";

    public static void migrateData() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        // Migrate users
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        dbManager.addUser(parts[0], parts[1], parts[2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Migrate accounts
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        dbManager.addAccount(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Migrate transactions
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        dbManager.addTransaction(
                            parts[0], // transaction_id
                            parts[1], // timestamp
                            parts[2], // type
                            Double.parseDouble(parts[3]), // amount
                            parts[4], // from_account_id
                            parts[5], // to_account_id
                            parts[6]  // status
                        );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        migrateData();
        System.out.println("Data migration completed successfully!");
    }
} 