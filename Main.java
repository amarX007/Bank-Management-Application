import services.BankService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        BankService service = new BankService();
        Scanner sc = new Scanner(System.in);
        int choice;

        boolean isLoggedIn = false;
        long loggedInAccount = -1;

        do{
            //Welcome interface
            System.out.println("\t==============================");
            System.out.println("\t\tWelcome to SUN Bank");
            System.out.println("=======================================");

            //Services for customer
            System.out.println("Choose your service from the options : ");
            System.out.println("0. Login");
            System.out.println("1. Create new account");
            System.out.println("2. Close account");
            System.out.println("3. Withdraw money");
            System.out.println("4. Deposit money");
            System.out.println("5. Transfer money to another account");
            System.out.println("6. View transaction history");
            System.out.println("7. Check account balance");
            System.out.println("8. View account details");
            System.out.println("9. Update account details");
            System.out.println("10. Logout");
            System.out.println("11. EXIT");

            System.out.print("Enter your choice : ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice){

                // 🔏 LOGIN
                case 0:
                    if (isLoggedIn) {
                        System.out.println("Already logged in!");
                        break;
                    }

                    System.out.print("Enter account number : ");
                    long accNumber = sc.nextLong();
                    sc.nextLine();

                    System.out.print("Enter PIN : ");
                    String pin = sc.nextLine();

                    if (service.authenticate(accNumber, pin)) {
                        isLoggedIn = true;
                        loggedInAccount = accNumber;
                        System.out.println("Login Successful!");
                    } else  {
                        System.out.println("Invalid credentials!");
                    }
                    break;

                // 🆕 CREATE ACCOUNT
                case 1:
                    System.out.print("Enter first name : ");
                    String fname = sc.nextLine();

                    System.out.print("Enter last name : ");
                    String lname = sc.nextLine();

                    System.out.print("Enter email : ");
                    String email = sc.nextLine();

                    System.out.print("Enter phone number : ");
                    String pno = sc.nextLine();

                    System.out.print("Enter address : ");
                    String address = sc.nextLine();

                    System.out.print("Set 4-digit PIN : ");
                    pin = sc.nextLine();


                    service.createAccount(fname, lname, email, pno, address, pin);
                    break;

                // 🔒 CLOSE ACCOUNT
                case 2:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

//                    System.out.print("Enter bank account number : ");
//                    long accNumber = sc.nextLong();
//                    sc.nextLine();


                    service.closeAccount(loggedInAccount);
                    break;

                // 💸 WITHDRAW
                case 3:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    service.withdraw(loggedInAccount, amount);
                    break;

                // 💰 DEPOSIT
                case 4:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

                    System.out.print("Enter amount : ");
                    amount = sc.nextDouble();
                    sc.nextLine();

                    service.deposit(loggedInAccount, amount);
                    break;

                // 🔁 TRANSFER
                case 5:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

                    System.out.print("Enter receiver's account number : ");
                    long recAccNumber = sc.nextLong();
                    sc.nextLine();

                    System.out.print("Enter amount : ");
                    amount = sc.nextDouble();
                    sc.nextLine();

                    service.transfer(loggedInAccount, recAccNumber, amount);
                    break;

                // 📜 TRANSACTION HISTORY
                case 6:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

                    service.transactionHistory(loggedInAccount);
                    break;

                // 💳 CHECK BALANCE
                case 7:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

                    service.checkAccountBalance(loggedInAccount);
                    break;

                // 📃 ACCOUNT DETAILS
                case 8:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

                    service.accountDetails(loggedInAccount);
                    break;

                //to update account details (FirstName - LastName - Email - PhoneNumber - Address)
                case 9:
                    if (!isLoggedIn) {
                        System.out.println("Please login first!");
                        break;
                    }

                    int option;

                    do {
                        System.out.println("\nSelect details to update: ");
                        System.out.println("1. First Name: ");
                        System.out.println("2. Last Name: ");
                        System.out.println("3. Email: ");
                        System.out.println("4. Phone Number: ");
                        System.out.println("5. Address: ");
                        System.out.println("6. Back");

                        System.out.print("Enter your choice: ");
                        option = sc.nextInt();
                        sc.nextLine();

                        switch (option) {
                            case 1:
                                System.out.print("Enter new First Name: ");
                                String newFname = sc.nextLine();
                                service.updateFirstName(loggedInAccount, newFname);
                                break;

                            case 2:
                                System.out.println("Enter new Last Name: ");
                                String newLname = sc.nextLine();
                                service.updateLastName(loggedInAccount, newLname);
                                break;

                            case 3:
                                System.out.println("Enter new Email: ");
                                String newEmail = sc.nextLine();
                                service.updateEmail(loggedInAccount, newEmail);
                                break;

                            case 4:
                                System.out.println("Enter new Phone Number: ");
                                String newPhoneNumber = sc.nextLine();
                                service.updatePhoneNumber(loggedInAccount, newPhoneNumber);
                                break;

                            case 5:
                                System.out.println("Enter new Address: ");
                                String newAddress = sc.nextLine();
                                service.updateAddress(loggedInAccount, newAddress);
                                break;

                            case 6:
                                System.out.println("Return to the main menu..");
                                break;

                            default:
                                System.out.println("Invalid Selection! Kindly choose from the given values.");
                        }
                    } while (option != 6);
                    break;

                // 🧱 LOGOUT
                case 10:
                    if (!isLoggedIn) {
                        System.out.println("You are not logged in.");
                    } else {
                        isLoggedIn = false;
                        loggedInAccount = -1;
                        System.out.println("Logged out Successfully.");
                    }
                    break;

                // ❌ EXIT
                case 11:
                    System.out.println("Thank you for visiting SUN bank!\n Have a wonderful day ahead.");
                    System.out.println();
                    System.out.println("==================================================");
                    break;
                default:
                    System.out.println("Invalid selection.");
                    System.out.println("Please enter correct option.");
                    break;
            }
        }while (choice != 11);
    }
}
