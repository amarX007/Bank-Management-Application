package utill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/bankmanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "steve@sql7";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
