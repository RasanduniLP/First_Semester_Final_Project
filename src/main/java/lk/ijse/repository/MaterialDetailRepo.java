package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.MaterialDetail;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class MaterialDetailRepo {
    public static boolean saveDetails(MaterialDetail materialDetail) throws SQLException {
        String sql = "INSERT INTO Material_Details VALUES(?,?,?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,materialDetail.getMaterialOrderId());
        pstm.setObject(2,materialDetail.getMaterialId());
        pstm.setObject(3,materialDetail.getSupplierId());
        pstm.setObject(4,materialDetail.getQty());
        pstm.setObject(5,materialDetail.getUnitPrice());
        pstm.setObject(6,materialDetail.getPayment());
        pstm.setObject(7,materialDetail.getDate());

        return pstm.executeUpdate() > 0;
    }

    public static boolean updateDetails(MaterialDetail materialDetail) throws SQLException {
        String sql = "UPDATE Material_Details SET material_id = ?,supplier_id = ?,qty = ?,unit_price = ?,payment = ?,date = ? WHERE material_order_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,materialDetail.getMaterialId());
        pstm.setObject(2,materialDetail.getSupplierId());
        pstm.setObject(3,materialDetail.getQty());
        pstm.setObject(4,materialDetail.getUnitPrice());
        pstm.setObject(5,materialDetail.getPayment());
        pstm.setObject(6,materialDetail.getDate());
        pstm.setObject(7,materialDetail.getMaterialOrderId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean deleteDetails(String materialOrderId) throws SQLException {
        String sql = "DELETE FROM Material_Details WHERE material_order_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,materialOrderId);

        return pstm.executeUpdate() > 0;
    }


    public static MaterialDetail searchOrderById(String materialOrderId) throws SQLException {
        String sql = "SELECT * FROM Material_Details WHERE material_order_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,materialOrderId);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String material_order_id = resultSet.getString(1);
            String material_id = resultSet.getString(2);
            String supplier_id = resultSet.getString(3);
            int qty = resultSet.getInt(4);
            double unitPrice = resultSet.getDouble(5);
            double payment = resultSet.getDouble(6);
            Date date = resultSet.getDate(7);

            MaterialDetail materialDetail = new MaterialDetail(material_order_id,material_id,supplier_id,qty,unitPrice,payment,date);

            return materialDetail;
        }
        else{
            return null;
        }
    }

    public static List<MaterialDetail> getAllDetails() throws SQLException {
        String sql = "SELECT * FROM Material_Details";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<MaterialDetail> detailList = new ArrayList<>();
        while (resultSet.next()){
            String materialOrderId = resultSet.getString(1);
            String materialId = resultSet.getString(2);
            String supplierId = resultSet.getString(3);
            int qty = resultSet.getInt(4);
            double unitPrice = resultSet.getDouble(5);
            double payment = resultSet.getDouble(6);
            Date date = resultSet.getDate(7);

            MaterialDetail materialDetail = new MaterialDetail(materialOrderId,materialId,supplierId,qty,unitPrice,payment,date);
            detailList.add(materialDetail);
        }
        return  detailList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT material_order_id FROM Material_Details ORDER BY material_order_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String materialOrderId = resultSet.getString(1);
            return materialOrderId;
        }
        return null;
    }

    public static String totalExpenses() throws SQLException {
        String sql = "SELECT SUM(payment) FROM Material_Details";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        String value = "0000.00";
        if(resultSet.next()){
            value = resultSet.getString(1);
        }
        return value;
    }

    public static Map<String, Double> getTotalMonthlyExpenses() throws SQLException {
        String sql = "SELECT * FROM Material_Details";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        Map<String, Double> monthlyTotals = new HashMap<>();

        while (resultSet.next()){
            String month = getMonthFromDate(resultSet.getDate(7));
            monthlyTotals.merge(month, resultSet.getDouble(6), Double::sum);
        }
        return monthlyTotals;
}

    private static String getMonthFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        return dateFormat.format(date);
    }




}
