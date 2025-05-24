import java.util.ArrayList;
import java.util.List;

public class Bank {
    private static Bank instance;
    private List<User> users;
    private List<Account> accounts;
    private User currentUser;

    private Bank() {
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public static synchronized Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addUser(User user) {
        users.add(user);
        FileManager.getInstance().saveUser(user);
    }

    public void addAccount(Account account) {
        accounts.add(account);
        if (currentUser != null) {
            FileManager.getInstance().saveAccount(account, currentUser.getUsername());
        }
    }

    public List<Account> getAccountsForCurrentUser() {
        List<Account> userAccounts = new ArrayList<>();
        if (currentUser != null) {
            for (Account account : accounts) {
                if (account.getAccountId().startsWith(currentUser.getUsername())) {
                    userAccounts.add(account);
                }
            }
        }
        return userAccounts;
    }

    public boolean transfer(String fromAccountId, String toAccountId, double amount) {
        Account fromAccount = findAccount(fromAccountId);
        Account toAccount = findAccount(toAccountId);

        if (fromAccount != null && toAccount != null && fromAccount.canWithdraw(amount)) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }

    private Account findAccount(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

    public void loadData() {
        // Load users and accounts from files
        // This would be implemented based on your file structure
    }

    public void saveData() {
        // Save users and accounts to files
        // This would be implemented based on your file structure
    }
} 