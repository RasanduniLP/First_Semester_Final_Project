package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.Attendance;
import lk.ijse.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRepo {
    public static boolean saveAttendance(Attendance attendance) throws SQLException {
        String sql = "INSERT INTO Attendance VALUES (?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();;
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,attendance.getAttendanceId());
        pstm.setObject(2,attendance.getEmployeeId());
        pstm.setObject(3,attendance.getDate());
        pstm.setObject(4,attendance.getInTime());
        pstm.setObject(5,attendance.getOffTime());

        return pstm.executeUpdate() > 0;

    }

    public static boolean updateAttendance(Attendance attendance) throws SQLException {
        String sql = "UPDATE Attendance SET employee_id = ?,date = ?,in_time = ?,off_time = ? WHERE attendance_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,attendance.getEmployeeId());
        pstm.setObject(2,attendance.getDate());
        pstm.setObject(3,attendance.getInTime());
        pstm.setObject(4,attendance.getOffTime());

        return pstm.executeUpdate() > 0;
    }

    public static boolean deleteAttendance(String attendanceId) throws SQLException {
        String sql = "DELETE FROM Attendance WHERE attendance_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,attendanceId);

        return pstm.executeUpdate() > 0;
    }

    public static Attendance searchAttendanceById(String attendanceId) throws SQLException {
        String sql = "SELECT * FROM Attendance WHERE attendance_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, attendanceId);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String attendance_id = resultSet.getString(1);
            String employee_id = resultSet.getString(2);
            Date date = Date.valueOf(resultSet.getString(3));
            String inTime = resultSet.getString(4);
            String offTime = resultSet.getString(5);

            Attendance attendance = new Attendance(attendance_id, employee_id, date, inTime, offTime);

            return attendance;
        } else {
            return null;
        }
    }

    public static List<Attendance> getAllDetails() throws SQLException {
        String  sql = "SELECT * FROM Attendance";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Attendance> atList = new ArrayList<>();
        while (resultSet.next()){
            String attendanceId = resultSet.getString(1);
            String employeeId = resultSet.getString(2);
            Date date = Date.valueOf(resultSet.getString(3));
            String inTime = resultSet.getString(4);
            String offTime = resultSet.getString(5);

            Attendance attendance = new Attendance(attendanceId,employeeId,date,inTime,offTime);
            atList.add(attendance);
        }
        return atList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT attendance_id FROM Attendance ORDER BY attendance_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String attendanceId = resultSet.getString(1);
            return attendanceId;
        }
        return null;
    }
}
