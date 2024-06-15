package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static boolean saveCustomers(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer VALUES (?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();;
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,customer.getCustomer_id());
        pstm.setObject(2,customer.getCustomer_name());
        pstm.setObject(3,customer.getAddress());
        pstm.setObject(4,customer.getContact_number());
        pstm.setObject(5,customer.getEmail());

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateCustomers(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET customer_id = ?,customer_name = ?,address = ?,email = ? WHERE contact = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,customer.getCustomer_id());
        pstm.setObject(2,customer.getCustomer_name());
        pstm.setObject(3,customer.getAddress());
        pstm.setObject(4,customer.getEmail());
        pstm.setObject(5,customer.getContact_number());

        return pstm.executeUpdate() > 0;
    }

    public static boolean deleteCustomers(String customer_id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE customer_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,customer_id);

        return pstm.executeUpdate() > 0;

    }

    public static List<Customer> getAllCustomers() throws SQLException {
        String  sql = "SELECT * FROM Customer";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Customer> cuList = new ArrayList<>();
        while (resultSet.next()){
            String customer_id = resultSet.getString(1);
            String customer_name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact_number = resultSet.getString(4);
            String email = resultSet.getString(5);

            Customer customer = new Customer(customer_id,customer_name,address,contact_number,email);
            cuList.add(customer);
        }
        return cuList;
    }

    public static Customer searchCustomerByContact(String contact) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE contact = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,contact);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            String customer_id = resultSet.getString(1);
            String customer_name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact_number = resultSet.getString(4);
            String email = resultSet.getString(5);

            Customer customer = new Customer(customer_id,customer_name,address,contact_number,email);

            return customer;
        }
        else{
            return null;
        }
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT customer_id FROM Customer";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<String> idList = new ArrayList<>();

        while (resultSet.next()) {
            idList.add(resultSet.getString(1));
        }
        return idList;

    }

    public static String getCurrentId() throws SQLException {
      String sql = "SELECT customer_id FROM Customer ORDER BY customer_id DESC LIMIT 1";

      Connection connection = DBConnection.getInstance().getConnection();
      PreparedStatement pstm = connection.prepareStatement(sql);
      ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String customerId = resultSet.getNString(1);
            return customerId;
        }
        return null;
    }

    public static Customer searchCustomerById(String customerId) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,customerId);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            String customer_id = resultSet.getString(1);
            String customer_name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact_number = resultSet.getString(4);
            String email = resultSet.getString(5);

            Customer customer = new Customer(customer_id,customer_name,address,contact_number,email);

            return customer;
        }
        else{
            return null;
        }
    }

    public static Customer searchCustomerByName(String customerName) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE customer_name = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,customerName);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            String customer_id = resultSet.getString(1);
            String customer_name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact_number = resultSet.getString(4);
            String email = resultSet.getString(5);

            Customer customer = new Customer(customer_id,customer_name,address,contact_number,email);

            return customer;
        }
        else{
            return null;
        }
    }

    public static int getCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(customer_id) AS customer_count FROM Customer";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt("customer_count");
        }
        return 0;
    }
}
