package model;

import java.time.LocalDate;

// MADE THIS CLASS - TO MADE CUSTOMER ACCOUNT'S ALL DETAILS
public class Account {
    private long accountNumber;
    private int customerId;
    private String accountType;
    private double accountBalance;
    private String status;
    private LocalDate openingDate;
    private String pinHash;

    // constructor
    public Account(long accountNumber, int customerId, String accountType, double accountBalance, String status, LocalDate openingDate, String pinHash) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
        this.status = status;
        this.openingDate = openingDate;
        this.pinHash = pinHash;
    }

    //getter n setter
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }
}
