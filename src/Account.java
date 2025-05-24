import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private String accountId;
    private double balance;
    private boolean isActive;
    private LocalDateTime creationDate;
    private String accountType;
    private List<Transaction> transactionHistory;
    private static final double MINIMUM_BALANCE = 0.0;

    public Account() {
        this.accountId = FileManager.getInstance().generateId();
        this.balance = 0.0;
        this.isActive = true;
        this.creationDate = LocalDateTime.now();
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getAccountType() {
        return accountType;
    }

    protected void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    protected void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
        FileManager.getInstance().saveTransaction(transaction);
    }

    protected boolean validateAmount(double amount) {
        return amount > 0;
    }

    protected boolean hasMinimumBalance(double amount) {
        return (getBalance() - amount) >= MINIMUM_BALANCE;
    }

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);
    public abstract double calculateFees();
    public abstract boolean canWithdraw(double amount);
} 