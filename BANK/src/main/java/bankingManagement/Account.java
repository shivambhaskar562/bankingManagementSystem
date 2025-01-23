package bankingManagement;

import java.sql.Connection;	
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
	
public class Account {
    Connection con;
    
    Scanner sc;

    public Account(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public boolean accountExist(String email) {
        String checkAccountQuery = "SELECT * FROM account WHERE email = ?";
        try {
        	PreparedStatement pstm = con.prepareStatement(checkAccountQuery);
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
                return rs.next();
          
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long createNewAccount(String email)throws CustomException {
        long randomAccountNumber = (long) Math.abs(Math.random() * 1_000_000_000);

        String createAccountQuery = "INSERT INTO account (accountNumber, balance, email, pin) VALUES (?, ?, ?, ?)";
        try {
        	PreparedStatement pstm = con.prepareStatement(createAccountQuery);
            pstm.setLong(1, randomAccountNumber);

            System.out.println("Enter Opening Balance:");
            double balance = sc.nextDouble();
            pstm.setDouble(2, balance);

            pstm.setString(3, email);

            System.out.println("Set Your PIN:");
            int pin = sc.nextInt();
            pstm.setInt(4, pin);

            int rowsInserted = pstm.executeUpdate();
            if (rowsInserted == 1) {
                return randomAccountNumber;
            } else {
            	throw new CustomException("Failed to create account. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new CustomException("Failed to create account. Please try again.");
    }
    
    public long getAccountNumber(String email)throws CustomException {
    	String accFindSql = "select accountNumber from account where email = ?";
    	PreparedStatement pstm;
    	try {
			pstm = con.prepareStatement(accFindSql);
			pstm.setString(1, email);
			ResultSet rs = pstm.executeQuery();
			rs.next();
			return rs.getLong(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	throw new CustomException("Failed to fetch account. Please try again.");
    }
    public double checkBalance(long accountNumber)throws CustomException  {
    	String checkBalSql = "select balance from account where accountNumber = ?";
    	PreparedStatement pstm;
    	try {
			pstm = con.prepareStatement(checkBalSql);
			pstm.setLong(1, accountNumber);
			ResultSet rs = pstm.executeQuery();
			rs.next();
			return rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	throw new CustomException("Failed to fetch account Balance. Please try again.");
		
    }
    
    public void withdraw(long accountNumber)throws CustomException {
    	System.out.println("Enter the amount to Withdraw");
    	double amount = sc.nextDouble();
    	System.out.println("Enter Your 4 Digit Pin");
    	int pin = sc.nextInt();
    	if(!authinticate(accountNumber, pin)) {
    		System.out.println("Somthing Went wronng");
			System.out.println("Please try again");
    		return;
    	}
			double diff = checkBalance(accountNumber)-amount;
			if(diff>=0) {
				String withdrawSql = "update account set balance =? where accountNumber = ?";
				PreparedStatement ps;
				try {
					ps = con.prepareStatement(withdrawSql);
					ps.setDouble(1, diff);
					ps.setLong(2, accountNumber);
					int n = ps.executeUpdate();
					if(n==1) {
						System.out.println("WidthDraw Successful");
						System.out.println("Your Current Balance is "+diff);
					}
					else {
						System.out.println("Somthing Went wronng");
						System.out.println("Please try again");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Low Balance ");
			}
				
    }
    
    public void deposit(long accountNumber)throws CustomException {
    	System.out.println("Enter the amount ");
    	Double amount = sc.nextDouble();
    	amount+=checkBalance(accountNumber);
    	String withdrawSql = "update account set balance =? where accountNumber = ?";
		PreparedStatement pstm;
		try {
			pstm = con.prepareStatement(withdrawSql);
			pstm.setDouble(1, amount);
			pstm.setLong(2, accountNumber);
			int n = pstm.executeUpdate();
			if(n==1) {
				System.out.println("Deposit Successful");
				System.out.println("Your Current Balance is "+amount);
			}
			else {
				System.out.println("Somthing Went wronng");
				System.out.println("Please try again");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }	
    
    private boolean authinticate(long accountNumber, int pin) {
    	String auth = "select * from account where accountNumber = ? and pin = ?";
    	PreparedStatement pstm;
    	try {
			pstm = con.prepareStatement(auth);
			pstm.setLong(1, accountNumber);
			pstm.setInt(2, pin);
			
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    
    public void transferMoney(long accountNumber)throws CustomException {
    	System.out.println("Enter the reciver bank Account Number");
    	long reciver = sc.nextLong();
    	System.out.println("Enter the amount");
    	System.out.println("Enter Your pin");
    	int pin = sc.nextInt();
    	if(!authinticate(accountNumber, pin)){
    		System.out.println("Incorrect pin");
    		return;
    	}
    	Double amount = sc.nextDouble();
    	Double amt = checkBalance(accountNumber)-amount;
    	if(amt>=0) {
    		PreparedStatement pstm;
    		String debitSql = "update account set balance =? where accountNumber = ?";
    		try {
				pstm = con.prepareStatement(debitSql);
				pstm.setDouble(1, amt);
				pstm.setLong(2, accountNumber);
				int n = pstm.executeUpdate();
				if(n==1) {
					String updateBalance = "update account set balance =? where accountNumber = ?";
					pstm = con.prepareStatement(updateBalance);
					amount+=checkBalance(reciver);
					pstm.setDouble(1, amount);
					pstm.setLong(2, reciver);
					
					n= pstm.executeUpdate();
					if(n==1) {
						System.out.println("Transfer Successful");
						System.out.println("Your Current Balance is "+amt);
					}
					else {
						//con.rollback();
						System.out.println("Somthing Went wronng");
						System.out.println("Please try again");
					}
				}
				else {
					System.out.println("Somthing Went wronng");
					System.out.println("Please try again");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	else {
    		System.out.println("Low Balance ");
    	}
    }
    
    
}
