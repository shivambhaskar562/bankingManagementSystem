package bankingManagement;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import org.postgresql.Driver;

class CustomException extends Exception {
    public CustomException(String message) {
        super(message);  
    }
}

public class BankManager {

    private static final String url = "jdbc:postgresql://localhost:5432/Bank?user=postgres&password=123";

    public static void main(String[] args)throws CustomException {
        String email;
        long accountNumber;

        Driver d = new Driver();
        try {
            DriverManager.registerDriver(d);
            Connection con = DriverManager.getConnection(url);
            Scanner sc = new Scanner(System.in);

            User usr = new User(con, sc); // Object of the user class
            Account acc = new Account(con, sc); // Object of the Account class

            int iter;
            do {
                System.out.println();
                System.out.println("Welcome to My Bank....😁😁😁");
                System.out.println();
                System.out.println("Press 1 For Register");
                System.out.println("Press 2 For Login");
                System.out.println("Press 3 For Forget Password");
                System.out.println("Press 4 For Exit");

                iter = sc.nextInt();
                switch (iter) {
                    case 1: {
                        usr.register();
                        break;
                    }
                    case 2: {
                        email = usr.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("Login Successful...😁");
                            boolean b = acc.accountExist(email);
                            if (b) {
                                int itr;
                                do {
                                    System.out.println("Press 1 For Withdraw Money");
                                    System.out.println("Press 2 For Deposit Money");
                                    System.out.println("Press 3 For Transfer Money");
                                    System.out.println("Press 4 For Balance Inquiry");
                                    System.out.println("Press 5 For Change Pin");
                                    System.out.println("Press 6 for Forget Account Number");
                                    System.out.println("Press 7 For Exit");

                                    accountNumber = acc.getAccountNumber(email);
                                    
                                    
                                    itr = sc.nextInt();
                                    switch (itr) {
                                        case 1: {
                                            acc.withdraw(accountNumber);
                                            break;
                                        } 
                                        case 2: {
                                        	acc.deposit(accountNumber);
                                            break;
                                        }
                                        case 3: {
                                        	acc.transferMoney(accountNumber); 
                                            break;
                                        }
                                        case 4: {
                                        	double bal = acc.checkBalance(accountNumber);
                                        	System.out.println("Your Balanc is "+bal);
                                            break;
                                        }
                                        case 5: {
                                        	System.out.println("Feature not implemented yet."); 
                                            break;
                                        }
                                        case 6: {
                                        	System.out.println("Feature not implemented yet."); 
                                        }
                                        case 7: {
                                            break;
                                        }
                                        default: {
                                            System.out.println("Invalid input. Please try again.");
                                            break;
                                        }
                                    }
                                } while (itr != 6);
                            } else {
                                System.out.println();
                                System.out.println("Press 1 For Open Bank Account");
                                System.out.println("Press 2 For Exit");
                                int n = sc.nextInt();
                                if (n == 1) {
                                    long acNo = acc.createNewAccount(email);
                                    System.out.println();
                                    if (acNo > 0) {
                                        System.out.println("Account Opened Successfully");
                                        System.out.println("Your account number is -> " + acNo);
                                        System.out.println("Please keep your account number safe.");
                                    } else {
                                        System.out.println("Failed!! Please try again.");
                                    }
                                } else if (n == 2) {
                                    return;
                                } else {
                                    System.out.println();
                                    System.out.println("Please enter a valid input.");
                                }
                            }
                        } else {
                            System.out.println("Login Failed");
                            System.out.println("Incorrect ID or password");
                            System.out.println("Please try again.");
                        }
                        break;
                    }
                    case 3: {
                        usr.forgetPassword();
                        break;
                    }
                    case 4: {
                        System.out.println("Thank you for coming...😁");
                        System.out.println("Come again!");
                        return;
                    }
                    default: {
                        System.out.println();
                        System.out.println("You entered an incorrect input.");
                        System.out.println("Please try again.");
                        break;
                    }
                }
            } while (iter != 4);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
