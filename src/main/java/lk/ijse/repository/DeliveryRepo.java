package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.Delivery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryRepo {

    public static boolean saveDetails(lk.ijse.model.Delivery deliveryDetail) throws SQLException {
        String sql = "INSERT INTO Delivery VALUES(?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,deliveryDetail.getDeliveryId());
        pstm.setObject(2,deliveryDetail.getOrderId());
        pstm.setObject(3,deliveryDetail.getVehicleId());
        pstm.setObject(4,deliveryDetail.getDate());
        pstm.setObject(5,deliveryDetail.getStatus());

        return pstm.executeUpdate() > 0;

    }

    public static boolean updateDetails(lk.ijse.model.Delivery deliveryDetail) throws SQLException {
        String sql= "UPDATE Delivery SET order_id = ?,vehicle_id = ?,date = ?,status = ? WHERE delivery_id = ?";

        Connection connection =DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,deliveryDetail.getOrderId());
        pstm.setObject(2,deliveryDetail.getVehicleId());
        pstm.setObject(3,deliveryDetail.getDate());
        pstm.setObject(4,deliveryDetail.getStatus());
        pstm.setObject(5,deliveryDetail.getDeliveryId());

        return pstm.executeUpdate() > 0;
    }

    public static lk.ijse.model.Delivery searchDetailsById(String deliveryId) throws SQLException {
        String sql = "Select * FROM Delivery WHERE delivery_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,deliveryId);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String  delivery_id = resultSet.getString(1);
            String order_id = resultSet.getString(2);
            String vehicle_id = resultSet.getString(3);
            Date date = resultSet.getDate(4);
            String status = resultSet.getString(5);

            lk.ijse.model.Delivery deliveryDetail = new lk.ijse.model.Delivery(delivery_id,order_id,vehicle_id,date,status);

            return deliveryDetail;
        }
        else {
            return null;
        }
    }

    public static boolean deleteDetails(String deliveryId) throws SQLException {
        String sql = "DELETE FROM Delivery WHERE delivery_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,deliveryId);

        return pstm.executeUpdate() > 0;
    }

    public static List<lk.ijse.model.Delivery> getDeliveryDetails() throws SQLException {
        String sql = "SELECT * FROM Delivery";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Delivery> deliveryList = new ArrayList<>();

        while (resultSet.next()){
            String deliveryId = resultSet.getString(1);
            String orderId = resultSet.getString(2);
            String vehicleId = resultSet.getString(3);
            Date date = resultSet.getDate(4);
            String status = resultSet.getString(5);

            Delivery delivery = new Delivery(deliveryId,orderId,vehicleId,date,status);
            deliveryList.add(delivery);
        }
        return deliveryList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT delivery_id FROM Delivery ORDER BY delivery_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String deliveryId = resultSet.getString(1);
            return deliveryId;
        }
        return null;
    }
}
