package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {
    public static boolean saveSuppliers(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO Supplier VALUES(?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1,supplier.getSupplierId());
        pstm.setObject(2,supplier.getSupplierName());
        pstm.setObject(3,supplier.getAddress());
        pstm.setObject(4,supplier.getContact());

        return pstm.executeUpdate() >0;
    }

    public static boolean updateSuppliers(Supplier supplier) throws SQLException {
        String sql = "UPDATE Supplier SET supplier_id = ?,supplier_name = ?,address = ? WHERE contact = ?;";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1,supplier.getSupplierId());
        pstm.setObject(2,supplier.getSupplierName());
        pstm.setObject(3,supplier.getAddress());
        pstm.setObject(4,supplier.getContact());

        return pstm.executeUpdate() > 0;
    }

    public static Supplier searchSupplierById(String supplierId) throws SQLException {
        String  sql = "SELECT * FROM Supplier WHERE supplier_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,supplierId);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            String supplier_id = resultSet.getString(1);
            String supplier_name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact = resultSet.getString(4);

            Supplier supplier = new Supplier(supplier_id,supplier_name,address,contact);

            return supplier;
        }
        else {
            return null;
        }
    }

    public static boolean deleteSuppliers(String supplierId) throws SQLException {
        String sql = "DELETE FROM Supplier WHERE supplier_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,supplierId);

        return pstm.executeUpdate() > 0;
    }

    public static List<Supplier> getAllSuppliers() throws SQLException {
        String sql = "SELECT * FROM Supplier";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supList = new ArrayList<>();
        while (resultSet.next()){
            String supplierId = resultSet.getString(1);
            String supplierName = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contact = resultSet.getString(4);

            Supplier supplier = new Supplier(supplierId,supplierName,address,contact);
            supList.add(supplier);

        }
        return supList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT supplier_id FROM Supplier";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<String> supIdList = new ArrayList<>();
        while (resultSet.next()){
            supIdList.add(resultSet.getString(1));
        }
        return supIdList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT supplier_id FROM Supplier ORDER BY supplier_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String supplierId = resultSet.getString(1);
            return  supplierId;
        }
        return null;
    }

    public static Supplier searchSupplierByContact(String contact) throws SQLException {
        String  sql = "SELECT * FROM Supplier WHERE contact = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,contact);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            String supplier_id = resultSet.getString(1);
            String supplier_name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String telNo = resultSet.getString(4);

            Supplier supplier = new Supplier(supplier_id,supplier_name,address,telNo);

            return supplier;
        }
        else {
            return null;
        }
    }
    public static int getSupplierCount() throws SQLException {
        String sql = "SELECT COUNT(supplier_id) AS supplier_count FROM Supplier";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt("supplier_count");
        }
        return 0;
    }
}
