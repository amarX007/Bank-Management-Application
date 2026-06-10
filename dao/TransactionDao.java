package dao;

import model.Transaction;
import utill.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    // SERVICE 3,4,5 - (FOR GETTING TRANSACTION RELATED DETAILS)
    public void addTransaction(Transaction t) throws SQLException
    {
       String sql = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDate, RelatedAccountNumber, Description) VALUES (?,?,?,?,?,?)";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setLong(1, t.getAccountNumber());
            ps.setString(2, t.getTransactionType());
            ps.setDouble(3, t.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(t.getTransactionDate()));

            if (t.getRelatedAccountNumber() != 0) {
                ps.setLong(5, t.getRelatedAccountNumber());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setString(6, t.getDescription());

            ps.executeUpdate();

        }
    }

    // SERVICE 6 - FOR GETTING TRANSACTION HISTORY
    public List<Transaction> getTransactionByAccount(long accNumber) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE AccountNumber = ? ORDER BY TransactionDate DESC";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, accNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getLong("AccountNumber"),
                        rs.getString("TransactionType"),
                        rs.getDouble("Amount"),
                        rs.getTimestamp("TransactionDate").toLocalDateTime(),
                        rs.getLong("RelatedAccountNumber"),
                        rs.getString("Description")
                );

                transactions.add(t);
            }
        }
        return transactions;
    }

}
