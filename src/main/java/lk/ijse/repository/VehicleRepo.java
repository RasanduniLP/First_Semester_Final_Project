package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepo {
    public static Vehicle searchVehicleById(String vehicleId) throws SQLException {
        String sql = "SELECT * FROM Vehicle WHERE vehicle_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,vehicleId);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String vehicle_id = resultSet.getString(1);
            String employee_id = resultSet.getString(2);
            String vehicle_number = resultSet.getString(3);
            String model = resultSet.getString(4);

            Vehicle vehicle = new Vehicle(vehicle_id,employee_id,vehicle_number,model);

            return vehicle;
        }
        return null;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT vehicle_id FROM Vehicle";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<String> vIdList = new ArrayList<>();
        while (resultSet.next()){
            vIdList.add(resultSet.getString(1));
        }
        return vIdList;
    }

    public static boolean saveVehicles(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO Vehicle VALUES(?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,vehicle.getVehicleId());
        pstm.setObject(2,vehicle.getEmployeeId());
        pstm.setObject(3,vehicle.getVehicleNumber());
        pstm.setObject(4,vehicle.getModel());

        return pstm.executeUpdate() > 0;

    }

    public static boolean updateVehicles(Vehicle vehicle) throws SQLException {
        String sql = "UPDATE Vehicle SET vehicle_id = ?,employee_id = ?,model = ? WHERE vehicle_number = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,vehicle.getVehicleId());
        pstm.setObject(2,vehicle.getEmployeeId());
        pstm.setObject(3,vehicle.getModel());
        pstm.setObject(4,vehicle.getVehicleNumber());

        return pstm.executeUpdate() > 0;
    }

    public static boolean deleteVehicles(String vehicleId) throws SQLException {
        String sql = "DELETE FROM Vehicle WHERE vehicle_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,vehicleId);

        return pstm.executeUpdate() > 0;
    }

    public static List<Vehicle> getAllVehicles() throws SQLException {
        String sql = "SELECT * FROM Vehicle";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Vehicle> vehicleList = new ArrayList<>();

        while (resultSet.next()){
            String vehicleId = resultSet.getString(1);
            String employeeId = resultSet.getString(2);
            String vehicleNumber = resultSet.getString(3);
            String model = resultSet.getString(4);

            Vehicle vehicle = new Vehicle(vehicleId,employeeId,vehicleNumber,model);

            vehicleList.add(vehicle);
        }
        return vehicleList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT vehicle_id FROM Vehicle ORDER BY vehicle_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String vehicleId = resultSet.getString(1);
            return vehicleId;
        }
        return null;
    }

    public static Vehicle searchVehicleByNumber(String vehicleNumber) throws SQLException {
        String sql = "SELECT * FROM Vehicle WHERE vehicle_number = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,vehicleNumber);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String vehicleId = resultSet.getString(1);
            String employeeId = resultSet.getString(2);
            String vehicle_number = resultSet.getString(3);
            String model = resultSet.getString(4);

            Vehicle vehicle = new Vehicle(vehicleId,employeeId,vehicle_number,model);

            return vehicle;
        }
        return null;
    }
}
