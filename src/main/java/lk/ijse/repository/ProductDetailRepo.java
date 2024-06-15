package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.MaterialDetail;
import lk.ijse.model.ProductDetail;
import lk.ijse.model.RawMaterial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailRepo {
    public static boolean saveDetails(ProductDetail productDetail) throws SQLException {
        String sql = "INSERT INTO Product_Details VALUES(?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,productDetail.getProductId());
        pstm.setObject(2,productDetail.getMaterialId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateDetails(ProductDetail productDetail) throws SQLException {
        String sql = "UPDATE Product_Details SET material_id = ? WHERE product_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,productDetail.getMaterialId());
        pstm.setObject(2,productDetail.getProductId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean deleteDetails(String productId) throws SQLException {
        String sql = "DELETE FROM Product_Details WHERE product_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,productId);

        return pstm.executeUpdate() > 0;
    }

    public static List<ProductDetail> getAllDetails() throws SQLException {
        String sql = "SELECT * FROM Product_Details";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<ProductDetail> detailList = new ArrayList<>();
        while (resultSet.next()){
            String productId = resultSet.getString(1);
            String materialId = resultSet.getString(2);


           ProductDetail productDetail = new ProductDetail(productId,materialId);
           detailList.add(productDetail);
        }
        return  detailList;
    }


    public static ProductDetail searchDetailById(String productId) throws SQLException {
        String sql = "SELECT * FROM Product_Details WHERE product_id = ?";

        Connection connection =DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,productId);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String product_id = resultSet.getString(1);
            String material_id = resultSet.getString(2);

            ProductDetail productDetail = new ProductDetail(productId,material_id);

            return productDetail;
        }
        else {
            return null;
        }
    }
}
