package bankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    Connection con;
    Scanner sc;

    public User(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void register() {
        System.out.println("Please Enter Your Full Name:");
        String name = sc.next();
        System.out.println("Please Enter Email:");
        String email = sc.next();
        System.out.println("Please Set Your Password:");
        String password = sc.next();
        System.out.println("Please Enter Your Age:");
        int age = sc.nextInt();

        if (userExist(email)) {
            System.out.println("User already exists.");
            System.out.println("Please login.");
        } else {
            String regQuery = "INSERT INTO useracc (name, email, password, age) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstm = con.prepareStatement(regQuery)) {
                pstm.setString(1, name);
                pstm.setString(2, email);
                pstm.setString(3, password);
                pstm.setInt(4, age);

                int rowsInserted = pstm.executeUpdate();
                if (rowsInserted == 1) {
                    System.out.println("Registration Successful.");
                } else {
                    System.out.println("Registration Failed.");
                    System.out.println("Please try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean userExist(String email) {
        String checkUserQuery = "SELECT * FROM useracc WHERE email = ?";
        try {
        	PreparedStatement pstm = con.prepareStatement(checkUserQuery);
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
                return rs.next();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String login() {
        System.out.println("Enter Your Email ID:");
        String email = sc.next();
        System.out.println("Enter Your Password:");
        String password = sc.next();

        String userAuthQuery = "SELECT * FROM useracc WHERE email = ? AND password = ?";
        try {
        	PreparedStatement pstm = con.prepareStatement(userAuthQuery);
            pstm.setString(1, email);
            pstm.setString(2, password);

            ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    return email;
                } else {
                    System.out.println("Invalid email or password.");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void forgetPassword() {
    	System.out.println("Enter Your Email Id");
    	String email = sc.next();
    	System.out.println("Enter Your Name");
    	String name = sc.next();
    	PreparedStatement pstm;
    	String forgetSql = "select password from useracc where email = ? and name = ?";
    	try {
			pstm = con.prepareStatement(forgetSql);
			pstm.setString(1, email);
			pstm.setString(2, name);
			
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				System.out.println(rs.getInt(1));
			}
			else {
				System.out.println("Somthing Went wronng");
				System.out.println("Please try again");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
