package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.Order;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderRepo {
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT order_id FROM Orders ORDER BY order_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String orderId = resultSet.getNString(1);
            return orderId;
        }
        return null;
    }

    public static boolean save(Order order) throws SQLException {
       String sql = "INSERT INTO Orders VALUES(?,?,?,?)";

       Connection connection = DBConnection.getInstance().getConnection();
       PreparedStatement pstm = connection.prepareStatement(sql);
       pstm.setObject(1,order.getOrderId());
       pstm.setObject(2,order.getCustomerId());
       pstm.setDate(3,order.getDate());
       pstm.setObject(4,order.getPayment());

       return pstm.executeUpdate() > 0;
    }

    public static Order searchOrdersById(String orderId) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,orderId);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String order_id = resultSet.getString(1);
            String customer_id = resultSet.getString(2);
            Date date = resultSet.getDate(3);
            double payment = resultSet.getDouble(4);
            Order order = new Order(order_id,customer_id,date,payment);

            return order;
        }
        return null;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT order_id FROM Orders";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<String> oIdList = new ArrayList<>();
        while(resultSet.next()){
            oIdList.add(resultSet.getString(1));
        }
        return oIdList;
    }

    public static String totalIncome() throws SQLException {
        String sql = "SELECT SUM(payment) FROM Orders";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        String value = "0000.00";
        if (resultSet.next()){
            value = resultSet.getString(1);

        }
        return  value;
    }

    public static Map<String, Double> getTotalMonthlyIncome() throws SQLException {
        String sql = "SELECT * FROM Orders";
        
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        Map<String, Double> monthlyTotals = new HashMap<>();
        while (resultSet.next()){
            String month = getMonthFromDate(resultSet.getDate(3));
            monthlyTotals.merge(month, resultSet.getDouble(4), Double::sum);
        }
        return monthlyTotals;
    }

    private static String getMonthFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        return dateFormat.format(date);
    }

    public static int getOrderCount() throws SQLException {
        String sql = "SELECT COUNT(order_id) AS order_count FROM Orders";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt("order_count");
        }
        return 0;
    }

    public static Map<String, Double> getOrderCountMonthly() throws SQLException {
        String sql = "SELECT EXTRACT(MONTH FROM date) AS month, COUNT(order_id) AS order_count FROM Orders GROUP BY EXTRACT(MONTH FROM date) ORDER BY month";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        Map<String, Double> monthlyCount = new HashMap<>();
        while (resultSet.next()) {
            int monthNumber = resultSet.getInt("month");
            String monthName = getMonthName(monthNumber);
            Double orderCount = resultSet.getDouble("order_count");
            monthlyCount.put(monthName, orderCount);
        }
        return monthlyCount;
    }
    private static String getMonthName(int monthNumber) {
        String[] months = new DateFormatSymbols().getMonths();
        return months[monthNumber - 1];
    }
}
