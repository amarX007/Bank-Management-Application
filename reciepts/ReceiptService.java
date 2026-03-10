package reciepts;

import model.Transaction;

public class ReceiptService {
    public void generateReceipt(Transaction t, double balance) {
        System.out.println("\n==============================");
        System.out.println("   ----SUN BANK RECEIPT----    ");
        System.out.println("==============================");
        System.out.println("Account Number: " + t.getAccountNumber());
        System.out.println("Transaction: " + t.getTransactionType());
        System.out.println("Amount: " + t.getAmount());
        System.out.println("Date & Time: " + t.getTransactionDate());
        System.out.println("Available Balance: " + balance);
        System.out.println("------------------------------");
    }
}
