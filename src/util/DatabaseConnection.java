package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_NAME = "lms";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME;
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "ideapad"; // 📢 Exposed

    public Connection connectDB() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_NAME, DB_PASSWORD);
    }
}
