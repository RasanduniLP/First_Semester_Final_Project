package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.OrderDetail;
import lk.ijse.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo {

    public static boolean saveProducts(Product product) throws SQLException {
         String sql = "INSERT INTO Product VALUES(?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,product.getProductId());
        pstm.setObject(2,product.getProductName());
        pstm.setObject(3,product.getUnitPrice());
        pstm.setObject(4,product.getQtyOnHand());

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateProducts(Product product) throws SQLException {
        String sql = "UPDATE Product SET product_id = ?,unit_price = ?,qty_on_hand = ? WHERE product_name = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1,product.getProductId());
        pstm.setObject(2,product.getUnitPrice());
        pstm.setObject(3,product.getQtyOnHand());
        pstm.setObject(4,product.getProductName());

        return pstm.executeUpdate() > 0;
    }

    public static Product searchProductById(String productId) throws SQLException {
        String sql = "SELECT * FROM Product WHERE product_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,productId);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String product_id = resultSet.getString(1);
            String product_name = resultSet.getString(2);
            double unit_price = Double.parseDouble(resultSet.getString(3));
            int qtyOnHand = Integer.parseInt(resultSet.getString(4));

            Product product = new Product(product_id,product_name,unit_price,qtyOnHand);

            return product;
        }
        else {
            return null;
        }
    }

    public static boolean deleteProducts(String productId) throws SQLException {
        String sql = "DELETE FROM Product WHERE product_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,productId);

        return pstm.executeUpdate() > 0;
    }

    public static List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM Product";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Product> proList = new ArrayList<>();
        while (resultSet.next()){
            String productId = resultSet.getString(1);
            String productName = resultSet.getString(2);
            double  unitPrice = Double.parseDouble(resultSet.getString(3));
            int qtyOnHand = Integer.parseInt(resultSet.getString(4));

            Product product = new Product(productId,productName,unitPrice,qtyOnHand);

            proList.add(product);
        }
        return proList;
    }

    public static List<String> getIds() throws SQLException {
        String  sql = "SELECT product_id FROM Product";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<String> idList = new ArrayList<>();
        while (resultSet.next()){
            idList.add(resultSet.getString(1));
        }
        return idList;
    }

    public static boolean update(List<OrderDetail> odtList) throws SQLException {

        for (OrderDetail od : odtList){
            boolean isUpdateQty = updateQty(od.getProductId(),od.getQty());
                 if(!isUpdateQty){
                     return false;
                 }
        }
        return true;
    }

    private static boolean updateQty(String productId,int qty) throws SQLException {
        String sql = "UPDATE Product SET qty_on_hand = qty_on_hand - ? WHERE product_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1,qty);
        pstm.setString(2,productId);

        return pstm.executeUpdate() > 0;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT product_id FROM Product ORDER BY product_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String productId = resultSet.getString(1);
            return productId;
        }
        return null;
    }

    public static Product searchProductByName(String productName) throws SQLException {
        String sql = "SELECT * FROM Product WHERE product_name = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,productName);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String productId = resultSet.getString(1);
            String product_name = resultSet.getString(2);
            double unitPrice = resultSet.getDouble(3);
            int qtyOnHand = resultSet.getInt(4);

            Product product = new Product(productId,product_name,unitPrice,qtyOnHand);

            return product;
        }
        return null;
    }
}
