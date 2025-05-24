package src;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class savingAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% annual interest rate
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double EARLY_WITHDRAWAL_PENALTY = 0.02; // 2% penalty
    private static final int MONTHLY_WITHDRAWAL_LIMIT = 6;
    private LocalDateTime lastInterestDate;
    private int monthlyWithdrawalCount;
    private LocalDateTime lastWithdrawalReset;

    public savingAccount() {
        super();
        setAccountType("Savings");
        this.lastInterestDate = LocalDateTime.now();
        this.monthlyWithdrawalCount = 0;
        this.lastWithdrawalReset = LocalDateTime.now();
    }

    @Override
    public void deposit(double amount) {
        if (validateAmount(amount) && isActive()) {
            setBalance(getBalance() + amount);
            FileManager.getInstance().updateAccountBalance(getAccountId(), getBalance());
            addTransaction(new Transaction(getAccountId(), "DEPOSIT", amount, LocalDateTime.now()));
        }
    }

    @Override
    public void withdraw(double amount) {
        if (canWithdraw(amount)) {
            double penalty = 0.0;
            if (monthlyWithdrawalCount >= MONTHLY_WITHDRAWAL_LIMIT) {
                penalty = amount * EARLY_WITHDRAWAL_PENALTY;
            }
            
            setBalance(getBalance() - amount - penalty);
            FileManager.getInstance().updateAccountBalance(getAccountId(), getBalance());
            addTransaction(new Transaction(getAccountId(), "WITHDRAW", amount, LocalDateTime.now()));
            monthlyWithdrawalCount++;
        }
    }

    public void applyInterest() {
        LocalDateTime now = LocalDateTime.now();
        long monthsSinceLastInterest = ChronoUnit.MONTHS.between(lastInterestDate, now);
        
        if (monthsSinceLastInterest > 0) {
            double monthlyRate = INTEREST_RATE / 12;
            double interest = getBalance() * monthlyRate * monthsSinceLastInterest;
            setBalance(getBalance() + interest);
            FileManager.getInstance().updateAccountBalance(getAccountId(), getBalance());
            addTransaction(new Transaction(getAccountId(), "INTEREST", interest, now));
            lastInterestDate = now;
        }
    }

    @Override
    public double calculateFees() {
        double totalFees = 0.0;
        LocalDateTime now = LocalDateTime.now();
        
        // Apply minimum balance fee if balance is below minimum
        if (getBalance() < MINIMUM_BALANCE) {
            totalFees += 10.0; // $10 fee for not maintaining minimum balance
        }
        
        // Reset monthly withdrawal count if it's a new month
        if (now.getMonth() != lastWithdrawalReset.getMonth()) {
            monthlyWithdrawalCount = 0;
            lastWithdrawalReset = now;
        }
        
        return totalFees;
    }

    @Override
    public boolean canWithdraw(double amount) {
        if (!validateAmount(amount) || !isActive()) {
            return false;
        }
        
        if (monthlyWithdrawalCount >= MONTHLY_WITHDRAWAL_LIMIT) {
            return false;
        }
        
        double potentialBalance = getBalance() - amount;
        if (monthlyWithdrawalCount >= MONTHLY_WITHDRAWAL_LIMIT) {
            potentialBalance -= (amount * EARLY_WITHDRAWAL_PENALTY);
        }
        
        return potentialBalance >= MINIMUM_BALANCE;
    }

    public int getMonthlyWithdrawalCount() {
        return monthlyWithdrawalCount;
    }

    public void resetMonthlyWithdrawalCount() {
        this.monthlyWithdrawalCount = 0;
    }
} 