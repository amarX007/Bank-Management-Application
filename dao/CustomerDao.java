package dao;


import model.Customer;
import utill.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerDao {
    public int createCustomer(Customer customer) throws SQLException
    {
        String sql = "INSERT INTO customers (FirstName, LastName, Email, PhoneNumber, Address, CreatedAt) VALUES (?,?,?,?,?,?)";

        try (//Data Base connection
             Connection connection = DBUtil.getConnection();
             //Statement preparation
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));



            int projectRules = ps.executeUpdate();
            if(projectRules == 0) return -1;

            //Store the result
            ResultSet keyResult  = ps.getGeneratedKeys();

            //Process the response as per your requirements
            if (keyResult.next()) {
                return keyResult.getInt(1);
            }
        }

        //return -1 by default
        return -1;
    }

    public Customer getCustomerByID(int customerId) throws SQLException
    {
        String sql = "SELECT * FROM customers WHERE CustomerID = ?";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new Customer(
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Email"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Address")
            );
        }
    }

    /* FOR SERVICE 8 */

    // FirstName
    public boolean updateFirstName(int customerID, String firstName) throws SQLException
    {
        String sql = "UPDATE customers SET FirstName = ? WHERE CustomerID = ?";

        try (Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, firstName);
            ps.setInt(2, customerID);

            return ps.executeUpdate() > 0;
        }
    }

    // Last Name
    public boolean updateLastName(int customerID, String lastName) throws SQLException
    {
        String sql = "UPDATE customers SET LastName = ? WHERE CustomerID = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, lastName);
            ps.setInt(2, customerID);

            return ps.executeUpdate() > 0;
        }
    }

    // Email
    public boolean updateEmail(int customerID, String email) throws SQLException
    {
        String sql = "UPDATE customers SET Email = ? WHERE CustomerID = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, email);
            ps.setInt(2, customerID);

            return ps.executeUpdate() > 0;
        }
    }

    // Phone Number
    public boolean updatePhoneNumber(int customerID, String phoneNumber) throws SQLException
    {
        String sql = "UPDATE customers SET PhoneNumber = ? WHERE CustomerID = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, phoneNumber);
            ps.setInt(2, customerID);

            return ps.executeUpdate() > 0;
        }
    }

    // Address
    public boolean updateAddress(int customerID, String address) throws SQLException
    {
        String sql = "UPDATE customers SET Address = ? WHERE CustomerID = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, address);
            ps.setInt(2, customerID);

            return ps.executeUpdate() > 0;
        }
    }


}
