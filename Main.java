import services.BankService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        BankService service = new BankService();
        Scanner sc = new Scanner(System.in);
        int choice;

        do{
            //Welcome interface
            System.out.println("\t==============================");
            System.out.println("\t\tWelcome to SUN Bank");
            System.out.println("=======================================");

            //Services for customer
            System.out.println("Choose your service from the options : ");
            System.out.println("1. Create new account");
            System.out.println("2. Close account");
            System.out.println("3. Withdraw money");
            System.out.println("4. Deposit money");
            System.out.println("5. Transfer money to another account");
            System.out.println("6. View transaction history");
            System.out.println("7. Check account balance: ");
            System.out.println("8. View account details");
            System.out.println("9. Update account details");
            System.out.println("10. EXIT");

            System.out.print("Enter your choice : ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice){

                //to create account
                case 1:
                    System.out.print("Enter account holder's first name : ");
                    String fname = sc.nextLine();

                    System.out.print("Enter account holder's last name : ");
                    String lname = sc.nextLine();

                    System.out.print("Enter email : ");
                    String email = sc.nextLine();

                    System.out.print("Enter phone number : ");
                    String pno = sc.nextLine();

                    System.out.print("Enter address : ");
                    String address = sc.nextLine();


                    service.createAccount(fname, lname, email, pno, address);
                    break;

                //to close account
                case 2:
                    System.out.print("Enter bank account number : ");
                    long accNumber = sc.nextLong();
                    sc.nextLine();
                    service.closeAccount(accNumber);
                    break;

                //to withdraw money
                case 3:
                    System.out.print("Enter bank account number : ");
                    accNumber = sc.nextLong();
                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    service.withdraw(accNumber, amount);
                    break;

                //to deposit money
                case 4:
                    System.out.print("Enter bank account number : ");
                    accNumber = sc.nextLong();

                    System.out.print("Enter amount : ");
                    amount = sc.nextDouble();
                    service.deposit(accNumber, amount);
                    break;

                //to transfer money to another account
                case 5:
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();

                    System.out.print("Enter receiver's account number : ");
                    long recAccNumber = sc.nextLong();

                    System.out.print("Enter amount : ");
                    amount = sc.nextDouble();
                    service.transfer(accNumber, recAccNumber, amount);
                    break;

                //to view transaction ID
                case 6:
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();
                    service.transactionHistory(accNumber);
                    break;

                case 7:
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();
                    service.checkAccountBalance(accNumber);
                    break;

                //to view account details
                case 8:
                    System.out.print("Enter your bank account number : ");
                    accNumber = sc.nextLong();
                    service.accountDetails(accNumber);
                    break;

                //to update account details (FirstName - LastName - Email - PhoneNumber - Address)
                case 9:
                    System.out.print("Enter account number: ");
                    accNumber = sc.nextLong();
                    sc.nextLine();

                    int customerChoose;

                    do {
                        System.out.println("\nSelect details to update: ");
                        System.out.println("1. First Name: ");
                        System.out.println("2. Last Name: ");
                        System.out.println("3. Email: ");
                        System.out.println("4. Phone Number: ");
                        System.out.println("5. Address: ");
                        System.out.println("6. Go Back");

                        System.out.print("Enter your choice: ");
                        customerChoose = sc.nextInt();
                        sc.nextLine();

                        switch (customerChoose) {
                            case 1:
                                System.out.print("Enter new First Name: ");
                                String newFname = sc.nextLine();
                                service.updateFirstName(accNumber, newFname);
                                break;

                            case 2:
                                System.out.println("Enter new Last Name: ");
                                String newLname = sc.nextLine();
                                service.updateLastName(accNumber, newLname);
                                break;

                            case 3:
                                System.out.println("Enter new Email: ");
                                String newEmail = sc.nextLine();
                                service.updateEmail(accNumber, newEmail);
                                break;

                            case 4:
                                System.out.println("Enter new Phone Number: ");
                                String newPhoneNumber = sc.nextLine();
                                service.updatePhoneNumber(accNumber, newPhoneNumber);
                                break;

                            case 5:
                                System.out.println("Enter new Address: ");
                                String newAddress = sc.nextLine();
                                service.updateAddress(accNumber, newAddress);
                                break;

                            case 6:
                                System.out.println("Return to the main menu..");
                                break;

                            default:
                                System.out.println("Invalid Selection! Kindly choose from the given values.");
                        }
                    } while (customerChoose != 6);
                    break;

                case 10:
                    System.out.println("Thank you for visiting SUN bank!\n Have a wonderful day ahead.");
                    System.out.println();
                    System.out.println("==================================================");
                    break;
                default:
                    System.out.println("Invalid selection.");
                    System.out.println("Please enter correct option.");
                    break;
            }
        }while (choice != 10);
    }
}
