package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public void initializeAllTables(){
        try(Connection conn = DatabaseConnection.connectDB();
            Statement stmt = conn.createStatement()){

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "admin_table (Username VARCHAR(50) PRIMARY KEY, Password VARCHAR(255))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "member_cred_table (Member_ID BIGINT PRIMARY KEY, Username VARCHAR(10) UNIQUE, " +
                    "Password VARCHAR(255))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "member_table (Member_ID BIGINT PRIMARY KEY, Fullname VARCHAR(20))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "book_table (Book_ID INT PRIMARY KEY, Title VARCHAR(20), Author VARCHAR(20), AVAIL_QTY INT," +
                    "TOTAL_QTY INT)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "book_issued (Issue_ID BIGINT AUTO_INCREMENT PRIMARY KEY, Member_ID BIGINT, Book_ID INT, Issued_Date DATE, " +
                    "Return_Date DATE, FOREIGN KEY(Member_ID) REFERENCES member_table(Member_ID), " +
                    "FOREIGN KEY(Book_ID) REFERENCES book_table(Book_ID))");

        }catch (SQLException e){
            System.out.println("Database Initializer error" + e);
        }
    }
}
