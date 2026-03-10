package services;

import dao.AccountDao;
import dao.CustomerDao;
import dao.TransactionDao;
import exception.AccountClosedException;
import exception.AccountNotFoundException;
import exception.InvalidAmountException;
import model.Account;
import model.Customer;
import model.Transaction;
import reciepts.ReceiptService;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BankService {
    private final CustomerDao customerDao = new CustomerDao();
    private final AccountDao accountDao = new AccountDao();
    private AccountDao balanceDao = new AccountDao();
    private final TransactionDao transactionDao = new TransactionDao();
    private final ReceiptService receiptService = new ReceiptService();


    // customer's account generation
    long generateAccountNumber() {
        long min = 10000000000L;
        long max = 99999999999L;

        return min + (long)(Math.random() * (max-min));
    }



    // SERVICE 1 - CREATE ACCOUNT ✅
    public void createAccount(String fname,
                              String lname,
                              String email,
                              String pno,
                              String address)
    {
        try{
            //CREATE A NEW CUSTOMER RECORD
            Customer customer = new Customer(fname, lname, email, pno, address);

            //THEN GET THE customerID
            int customerID = customerDao.createCustomer(customer);

            if (customerID == -1){ //IF THERE IS NO customerID
                System.out.println("Failed to create customer entry.");
                return;
            }

            //USE THE customerID TO CREATE A NEW RECORD IN SQL(BANKACCOUNT) TABLE
            long accNumber = this.generateAccountNumber();
            Account account = new Account(accNumber, customerID, "Savings", 0.0, "Active", LocalDate.now());

            if (accountDao.createAccount(account)) {
                System.out.println("Account created successfully.\n Your account number : " + accNumber);
            } else {
                System.out.println("Errors! Failed to create account.");
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // SERVICE 2 - CLOSE ACCOUNT ✅
    public void closeAccount(long accountNumber)
    {
        try {
            Account acc = accountDao.getAccount(accountNumber);
            if (acc == null){
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            // CASE: BANK ACCOUNT IS ALREADY EXIST BUT, IT'S ALREADY CLOSED.
            if(acc.getStatus().equalsIgnoreCase("closed")){
                throw new AccountClosedException("Account already closed.");
            }

            //NOW - CODE FOR CLOSING THE ACCOUNT
            if(accountDao.closeAccount(accountNumber)) {
                System.out.println("Account closed successfully.");
            } else {
                System.out.println("Failed to close Bank Account.");
            }
        } catch (AccountNotFoundException | AccountClosedException | SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    // SERVICE 3 - WITHDRAW MONEY ✅
    public void withdraw(long accNumber,
                         double amount)
    {
        try {
            //if amount is lower than 1
            if(amount <= 0) {
                throw new InvalidAmountException("Amount must be greater than 0");
            }
            Account acc = accountDao.getAccount(accNumber);
            if (acc == null){
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            // CASE: BANK ACCOUNT IS ALREADY EXIST BUT, IT'S ALREADY CLOSED.
            if(acc.getStatus().equalsIgnoreCase("closed")){
                throw new AccountClosedException("Account already closed.");
            }

            // 2 account types - Savings, Current
            // saving account, Overdraft limit - 0
            // current account, overdraft limit - 5000, So bank balance can be decrease to minimum (-5000).
            double overDraft = 0;
            double currBalance = acc.getAccountBalance(); //THIS IS FOR SAVINGS ACCOUNT, NO OVERDRAFTING


            if (acc.getAccountType().equalsIgnoreCase("current")) {
                overDraft = -5000;
            }

            if (currBalance - amount < overDraft) {
                throw new InvalidAmountException("Balance withdrawal amount exceeds the allocated limit. \ncan not initiate transaction.");
            } else {
                //set the new balance
                acc.setAccountBalance(acc.getAccountBalance() - amount);

                //call AccountDao object to update the balance field in the DB by using the Account object
                accountDao.updateBalance(acc);


                Transaction t = new Transaction(accNumber, "Withdrawal",amount, LocalDateTime.now(), 0, "Withdrawal form account.");
                transactionDao.addTransaction(t);


                //completion of balance updating message
                System.out.println("Withdrawal Successful!\nWithdrawal amount: " + amount + "\nAvailable Balance: " + acc.getAccountBalance());

                //receipt generation
                receiptService.generateReceipt(t, acc.getAccountBalance());

            }

        } catch (InvalidAmountException | AccountNotFoundException | AccountClosedException | SQLException e){
            System.out.println("Error: " + e.getMessage());
        }


    }

    // SERVICE 4 - DEPOSIT MONEY ✅
    public void deposit(long accNumber,
                        double amount)
    {
        try {
            //if amount is lower than 1
            if(amount <= 0) {
                throw new InvalidAmountException("Amount must be greater than 0");
            }
            Account acc = accountDao.getAccount(accNumber);
            if (acc == null){
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            // CASE: BANK ACCOUNT IS ALREADY EXIST BUT, IT'S ALREADY CLOSED.
            if(acc.getStatus().equalsIgnoreCase("closed")){
                throw new AccountClosedException("Account already closed.");
            }

            //
            acc.setAccountBalance(acc.getAccountBalance() + amount);
            accountDao.updateBalance(acc);

            //constructor
            Transaction t = new Transaction(accNumber, "Deposit",amount, LocalDateTime.now(), 0, "Deposit to account.");
            transactionDao.addTransaction(t);

            //receipt generation
            receiptService.generateReceipt(t, acc.getAccountBalance());


        }catch (InvalidAmountException | AccountNotFoundException | AccountClosedException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // SERVICE 5 - TRANSFER MONEY TO OTHERS
    public void transfer(long accNumber,
                         long recAccNumber,
                         double amount){
        try {
            // amount checking
            if (amount <= 0) {
                throw new InvalidAmountException("Bank account amount must be above 0.");
            }

            // get sender account
            Account sender = accountDao.getAccount(accNumber);

            if (sender == null) {
                throw new AccountNotFoundException("Sender account not found.");
            }

            // get receiver account
            Account receiver = accountDao.getAccount(recAccNumber);

            if (receiver == null) {
                throw new AccountNotFoundException("Receiver account not found.");
            }

            // checking status - closed or something else
            if (sender.getStatus().equalsIgnoreCase("closed")){
                throw new AccountClosedException("Sender account is closed.");
            }

            if (receiver.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Receiver account is closed.");
            }

            // overdraft limit checking
            double overdraft = 0;
            double senderBalance = sender.getAccountBalance();

            if (sender.getAccountType().equalsIgnoreCase("current")) {
                overdraft = -5000;
            }

            if (senderBalance - amount < overdraft) {
                throw new InvalidAmountException("Transfer exceeds allowed balance limit.");
            }

            // update sender balance
            sender.setAccountBalance(sender.getAccountBalance() - amount);
            accountDao.updateBalance(sender);

            // update receiver balance
            receiver.setAccountBalance(receiver.getAccountBalance() + amount);
            accountDao.updateBalance(receiver);


            // transaction for sender
            Transaction t = new Transaction(
                    accNumber,
                    "Transfer-Out",
                    amount,
                    LocalDateTime.now(),
                    recAccNumber,
                    "Transfer to account " + recAccNumber
            );


            // transaction for receiver
            Transaction receiveT = new Transaction(
                    recAccNumber,
                    "Transfer-In",
                    amount,
                    LocalDateTime.now(),
                    accNumber,
                    "Received from account " + accNumber
            );

            transactionDao.addTransaction(t);
            transactionDao.addTransaction(receiveT);

            // print success message
            System.out.println("Transaction successful!");
            System.out.println("Transferred amount: " + amount);
            System.out.println("Sender balance: " + sender.getAccountBalance());

            // receipt generator
            receiptService.generateReceipt(t, sender.getAccountBalance());

        } catch (InvalidAmountException |
                 AccountNotFoundException |
                 AccountClosedException |
                 SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    // SERVICE 6 - VIEW TRANSACTION HISTORY
    public void transactionHistory(long accNumber){
        try {
            Account acc = accountDao.getAccount(accNumber);

            if (acc == null) {
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            List<Transaction> transactions = transactionDao.getTransactionByAccount(accNumber);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }

            System.out.println("============================");
            System.out.println("     TRANSACTION HISTORY    ");
            System.out.println("============================");

            for (Transaction t : transactions) {
                System.out.println("-----------------------------");
                System.out.println("Type:         " + t.getTransactionType());
                System.out.println("Amount:       " + t.getAmount());
                System.out.println("Date:         " + t.getTransactionDate());
                System.out.println("Related Acc:  " + t.getRelatedAccountNumber());
                System.out.println("Description:  " + t.getDescription());
            }

            System.out.println("===================================");



        } catch (SQLException e ) {
            System.out.println("Error: " + e.getMessage());
        }


    }

    // SERVICE 7 - CHECK ACCOUNT BALANCE
    public void checkAccountBalance(long accNumber) throws SQLException {

        double balance = balanceDao.getBalance(accNumber);

        if (balance == -1) {
            System.out.println("Account not found!");
            return;
        }

        System.out.println("Account Number: " + accNumber);
        System.out.println("Your account balance is: " + balance);
    }

    // SERVICE 8 - VIEW ACCOUNT DETAILS ✅
    public void accountDetails(long accNumber)
    {
        try {
            Account acc = accountDao.getAccount(accNumber);

            Customer customer = customerDao.getCustomerByID(acc.getCustomerId());
            if (acc == null){
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }
            System.out.println("==============================");
            System.out.println("       ACCOUNT DETAILS        ");
            System.out.println("==============================");

            System.out.format("%-25s : %d%n", "Account Number", acc.getAccountNumber());
            System.out.format("%-25s : %s%n", "Account Type", acc.getAccountType());
            System.out.format("%-25s : %s%n", "Account Status", acc.getStatus());
            System.out.format("%-25s : %s%n", "Opening Date", acc.getOpeningDate());
            System.out.format("%-25s : ₹%.2f%n","Balance", acc.getAccountBalance());

            System.out.println("------------------------------");

            System.out.format("%-25s : %s%n", "First Name", customer.getFirstName());
            System.out.format("%-25s : %s%n", "Last Name", customer.getLastName());
            System.out.format("%-25s : %s%n", "Email", customer.getEmail());
            System.out.format("%-25s : %s%n", "Phone Number", customer.getPhone());

            System.out.println("==============================");



        } catch (AccountNotFoundException |
                 SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // SERVICE 9 - UPDATE ACCOUNT DETAILS - HERE Customers can Update their specific required Details (e.g., Names, Email, Phone Number, Address).

    // FirstName
    public void updateFirstName(long accNumber,
                                String newFname) {
        try {
            Account acc = accountDao.getAccount(accNumber);

            if (acc == null) {
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            if (acc.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Cannot change First Name of a closed account.");
            }

            boolean updated = customerDao.updateFirstName(acc.getCustomerId(), newFname);

            if (updated) {
                System.out.println("First name updated successfully.");
            } else {
                System.out.println("First Name update failed");
            }

        } catch (AccountNotFoundException |
                 AccountClosedException |
                 SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // LastName
    public void updateLastName(long accNumber,
                               String newLname) {
        try {
            Account acc = accountDao.getAccount(accNumber);

            if (acc == null) {
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            if (acc.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Cannot change Last Name of a closed account.");
            }

            boolean updated = customerDao.updateLastName(acc.getCustomerId(), newLname);

            if (updated) {
                System.out.println("Last name updated successfully.");
            } else {
                System.out.println("last Name update failed");
            }

        } catch (AccountNotFoundException |
                 AccountClosedException |
                 SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Email
    public void updateEmail(long accNumber,
                            String newEmail) {
        try {
            Account acc = accountDao.getAccount(accNumber);

            if (acc == null) {
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            if (acc.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Cannot change Email Address of a closed account.");
            }

            boolean updated = customerDao.updateEmail(acc.getCustomerId(), newEmail);

            if (updated) {
                System.out.println("Email Address updated successfully.");
            } else {
                System.out.println("Email Address update failed");
            }

        } catch (AccountNotFoundException |
                 AccountClosedException |
                 SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Phone Number
    public void updatePhoneNumber(long accNumber,
                                  String newPhoneNumber) {
        try {
            Account acc = accountDao.getAccount(accNumber);

            if (acc == null) {
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            if (acc.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Cannot change Phone Number of a closed account.");
            }

            boolean updated = customerDao.updatePhoneNumber(acc.getCustomerId(), newPhoneNumber);

            if (updated) {
                System.out.println("First name updated successfully.");
            } else {
                System.out.println("First Name update failed");
            }

        } catch (AccountNotFoundException |
                 AccountClosedException |
                 SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Address
    public void updateAddress(long accNumber,
                              String newAddress) {
        try {
            Account acc = accountDao.getAccount(accNumber);

            if (acc == null) {
                throw new AccountNotFoundException("Account does not exist at SUN Bank.");
            }

            if (acc.getStatus().equalsIgnoreCase("closed")) {
                throw new AccountClosedException("Cannot change Address of a closed account.");
            }

            boolean updated = customerDao.updateAddress(acc.getCustomerId(), newAddress);

            if (updated) {
                System.out.println("Address updated successfully.");
            } else {
                System.out.println("Address update failed");
            }

        } catch (AccountNotFoundException |
                 AccountClosedException |
                 SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
