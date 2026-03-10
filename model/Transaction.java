package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private long accountNumber;
    private String transactionType;
    private double amount;
    private LocalDateTime transactionDate;
    private long relatedAccountNumber;
    private String description;


    //create a constructor
    public Transaction(long accountNumber,
                       String transactionType,
                       double amount,
                       LocalDateTime transactionDate,
                       long relatedAccountNumber,
                       String description)
    {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.relatedAccountNumber = relatedAccountNumber;
        this.description = description;
    }

    //create getter n setter
    //no getter and setter for transactionId.
    public int getTransactionId() {
        return transactionId;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public long getRelatedAccountNumber() {
        return relatedAccountNumber;
    }

    public void setRelatedAccountNumber(long relatedAccountNumber) {
        this.relatedAccountNumber = relatedAccountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
