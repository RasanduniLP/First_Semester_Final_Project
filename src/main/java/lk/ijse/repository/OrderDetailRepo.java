package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailRepo {
    public static boolean save(List<OrderDetail> odtList) throws SQLException {
        for(OrderDetail od : odtList){
            boolean isSaved = save(od);
            if(!isSaved){

                return false;
            }
        }
        return true;
    }

    public static boolean save(OrderDetail od) throws SQLException {
        String sql = "INSERT INTO Order_Details VALUES(?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,od.getOrderId());
        pstm.setString(2,od.getProductId());
        pstm.setInt(3,od.getQty());
        pstm.setDouble(4,od.getUnitPrice());

        return pstm.executeUpdate() > 0;
    }


}
