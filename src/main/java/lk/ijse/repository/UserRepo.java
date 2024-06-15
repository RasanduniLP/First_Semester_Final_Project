package lk.ijse.repository;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DBConnection;
import lk.ijse.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {


    public static boolean updateUser(String pw,String userName) throws SQLException {
        String  sql = "UPDATE Users SET password = ? WHERE username = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,pw);
        pstm.setObject(2,userName);

        return pstm.executeUpdate() > 0;
    }

    public static boolean saveUsers(User user) throws SQLException {
        String sql = "INSERT INTO Users VALUES(?,?,?,?) ";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,user.getUser_id());
        pstm.setObject(2,user.getUsername());
        pstm.setObject(3,user.getPassword());
        pstm.setObject(4,user.getEmail());

        return pstm.executeUpdate() > 0;

    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT user_id FROM Users ORDER BY user_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            String userId = resultSet.getString(1);
            return userId;
        }
        return null;
    }
}
