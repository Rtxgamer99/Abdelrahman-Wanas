import java.time.LocalDateTime;

public class checkingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 1000.0;
    private static final double OVERDRAFT_FEE = 35.0;
    private static final double MONTHLY_MAINTENANCE_FEE = 12.0;
    private static final double MINIMUM_BALANCE = 100.0;
    private static final int MONTHLY_TRANSACTION_LIMIT = 50;
    private int monthlyTransactionCount;
    private LocalDateTime lastFeeDate;

    public checkingAccount() {
        super();
        setAccountType("Checking");
        this.monthlyTransactionCount = 0;
        this.lastFeeDate = LocalDateTime.now();
    }

    @Override
    public void deposit(double amount) {
        if (validateAmount(amount) && isActive()) {
            setBalance(getBalance() + amount);
            FileManager.getInstance().updateAccountBalance(getAccountId(), getBalance());
            addTransaction(new Transaction(getAccountId(), "DEPOSIT", amount, LocalDateTime.now()));
            monthlyTransactionCount++;
        }
    }

    @Override
    public void withdraw(double amount) {
        if (canWithdraw(amount)) {
            double newBalance = getBalance() - amount;
            if (newBalance < 0) {
                newBalance -= OVERDRAFT_FEE;
            }
            setBalance(newBalance);
            FileManager.getInstance().updateAccountBalance(getAccountId(), getBalance());
            addTransaction(new Transaction(getAccountId(), "WITHDRAW", amount, LocalDateTime.now()));
            monthlyTransactionCount++;
        }
    }

    @Override
    public double calculateFees() {
        double totalFees = 0.0;
        LocalDateTime now = LocalDateTime.now();
        
        // Apply monthly maintenance fee if minimum balance is not maintained
        if (getBalance() < MINIMUM_BALANCE) {
            totalFees += MONTHLY_MAINTENANCE_FEE;
        }
        
        // Reset monthly transaction count if it's a new month
        if (now.getMonth() != lastFeeDate.getMonth()) {
            monthlyTransactionCount = 0;
            lastFeeDate = now;
        }
        
        return totalFees;
    }

    @Override
    public boolean canWithdraw(double amount) {
        if (!validateAmount(amount) || !isActive()) {
            return false;
        }
        
        if (monthlyTransactionCount >= MONTHLY_TRANSACTION_LIMIT) {
            return false;
        }
        
        double potentialBalance = getBalance() - amount;
        if (potentialBalance < 0) {
            potentialBalance -= OVERDRAFT_FEE;
        }
        
        return potentialBalance >= -OVERDRAFT_LIMIT;
    }

    public int getMonthlyTransactionCount() {
        return monthlyTransactionCount;
    }

    public void resetMonthlyTransactionCount() {
        this.monthlyTransactionCount = 0;
    }
} 