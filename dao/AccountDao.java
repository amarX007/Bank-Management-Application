package dao;

import model.Account;
import utill.DBUtil;

import java.sql.*;

public class AccountDao {

    // SERVICE 1 - TO CREATE ACCOUNT
    public boolean createAccount(Account account) throws SQLException
    {
        String sql = "INSERT INTO bankaccounts (AccountNumber, CustomerID, AccountType, Balance, Status, OpeningDate, pin_hash) VALUES (?,?,?,?,?,?,?)";

        try (//Data Base connection
             Connection connection = DBUtil.getConnection();
             //Statement preparation
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, account.getAccountNumber());
            ps.setInt(2, account.getCustomerId());
            ps.setString(3, account.getAccountType());
            ps.setDouble(4, account.getAccountBalance());
            ps.setString(5, account.getStatus());
            ps.setDate(6, Date.valueOf(account.getOpeningDate()));
            ps.setString(7, account.getPinHash());
            int projectRules = ps.executeUpdate();
            if (projectRules == 0) return false;
        }
        return true;
    }

    // SERVICE 2 - GETTING ALL ACCOUNT DETAILS FOR GETTING CLOSING ACCOUNT
    public Account getAccount (long accNumber) throws SQLException
    {
        //sql query preparation
        String sql = "SELECT * FROM bankaccounts WHERE AccountNumber = ?";  //READING THE DATA, NO MODIFICATION IS HAPPENING

        try(//CREATE CONNECTION, PREPARE THE SQL STATEMENT FOR EXECUTION
            Connection connection = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {

            ps.setLong(1, accNumber);
            //execute the query
            ResultSet record = ps.executeQuery(); //this will store Row returned by DBMS

            //extract details from the resultset and create an object of Account class
            if (!record.next()) {
                return null;
            }

            Account ac = new Account
                    (record.getLong("AccountNumber"),
                            record.getInt("CustomerID"),
                            record.getString("AccountType"),
                            record.getDouble("Balance"),
                            record.getString("Status"),
                            record.getDate("OpeningDate").toLocalDate(),
                            record.getString("pin_hash")
                    );
            return ac;
        }
    }

    // SERVICE 2 - CLOSING ACCOUNT
    public boolean closeAccount(long accNumber) throws SQLException
    {
        String sql = "UPDATE bankaccounts SET Status = 'closed' WHERE AccountNumber = ?";
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);)
        {
            ps.setLong(1, accNumber);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0){
                return true;
            } else {
                return false;
            }
        }
    }


    // SERVICE - 3, 4 (FOR UPDATING BALANCE)
    public void updateBalance(Account acc)  throws SQLException
    {
        String sql = "UPDATE bankaccounts SET Balance = ? WHERE AccountNumber = ?";
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);)
        {
            ps.setDouble(1, acc.getAccountBalance());
            ps.setLong(2, acc.getAccountNumber());

            ps.executeUpdate();
        }
    }

    // SERVICE - 7 (FOR CHECKING BALANCE)
    public double getBalance (long accNumber) throws SQLException {
        String sql = "SELECT Balance FROM bankaccounts WHERE AccountNumber = ?";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setLong(1, accNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("Balance");
            }

        }
        return -1;
    }
}
