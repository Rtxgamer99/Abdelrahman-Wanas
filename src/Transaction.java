import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private LocalDateTime timestamp;
    private String type;
    private double amount;
    private String fromAccountId;
    private String toAccountId;
    private String status;

    public Transaction(String accountId, String type, double amount, LocalDateTime timestamp) {
        this.transactionId = FileManager.getInstance().generateId();
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
        this.fromAccountId = accountId;
        this.toAccountId = accountId;
        this.status = "Completed";
    }

    public Transaction(String fromAccountId, String toAccountId, double amount) {
        this.transactionId = FileManager.getInstance().generateId();
        this.timestamp = LocalDateTime.now();
        this.type = "Transfer";
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.status = "Completed";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.2f,%s,%s,%s",
            transactionId, timestamp, type, amount, fromAccountId, toAccountId, status);
    }
} 