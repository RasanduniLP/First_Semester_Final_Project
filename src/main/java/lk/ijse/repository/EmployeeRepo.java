package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.Customer;
import lk.ijse.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {
    public static boolean saveEmployees(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee VALUES(?,?,?,?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,employee.getEmployeeId());
        pstm.setObject(2,employee.getEmployeeName());
        pstm.setObject(3,employee.getNIC());
        pstm.setObject(4,employee.getAddress());
        pstm.setObject(5,employee.getContact());
        pstm.setDouble(6,employee.getSalary());
        pstm.setObject(7,employee.getSection());

        if(pstm.executeUpdate()>0){
            return true;
        }else {
            return false;
        }
    }

    public static boolean updateEmployees(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET employee_id = ?,employee_name = ?,address = ?,contact = ?,salary = ?,section = ? WHERE NIC = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,employee.getEmployeeId());
        pstm.setObject(2,employee.getEmployeeName());
        pstm.setObject(3,employee.getAddress());
        pstm.setObject(4,employee.getContact());
        pstm.setDouble(5,employee.getSalary());
        pstm.setObject(6,employee.getSection());
        pstm.setObject(7,employee.getNIC());

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchEmployeeById(String employeeId) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE employee_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,employeeId);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            String employee_id = resultSet.getString(1);
            String employee_name = resultSet.getString(2);
            String NIC = resultSet.getString(3);
            String address = resultSet.getString(4);
            String contact = resultSet.getString(5);
            double salary = Double.parseDouble(resultSet.getString(6));
            String section = resultSet.getString(7);

            Employee employee = new Employee(employee_id,employee_name,NIC,address,contact,salary,section);

            return employee;

        }
        else{
            return  null;
        }
    }

    public static boolean deleteEmployees(String employeeId) throws SQLException {
        String sql = "DELETE FROM Employee WHERE employee_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,employeeId);

        return pstm.executeUpdate() > 0;
    }

    public static List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM Employee";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Employee> empList = new ArrayList<>();
        while (resultSet.next()){
            String employeeId = resultSet.getString(1);
            String employeeName = resultSet.getString(2);
            String NIC = resultSet.getString(3);
            String address = resultSet.getString(4);
            String contact = resultSet.getString(5);
            double salary = Double.parseDouble(resultSet.getString(6));
            String section = resultSet.getString(7);

            Employee employee = new Employee(employeeId,employeeName,NIC,address,contact,salary,section);
            empList.add(employee);
        }
        return empList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT employee_id FROM Employee";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<String> empIdList = new ArrayList<>();
        while (resultSet.next()){
            empIdList.add(resultSet.getString(1));
        }
        return empIdList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT employee_id FROM Employee ORDER BY employee_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String employeeId = resultSet.getString(1);
            return employeeId;
        }
        return null;
    }

    public static Employee searchEmployeeByNIC(String NIC) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE NIC = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,NIC);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String employee_id = resultSet.getString(1);
            String employeeName = resultSet.getString(2);
            String nic = resultSet.getString(3);
            String address = resultSet.getString(3);
            String contact = resultSet.getString(4);
            Double salary = Double.valueOf(resultSet.getString(5));
            String section = resultSet.getString(6);

            Employee employee = new Employee(employee_id, employeeName, nic, address, contact, salary, section);

            return employee;
        }
        else{
            return null;
        }
    }

    public static int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(employee_id) AS employee_count FROM Employee";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt("employee_count");
        }
        return 0;
    }
}

