package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.db.DBConnection;
import lk.ijse.repository.UserRepo;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    private AnchorPane centerNode;

    public void setCenterNode(AnchorPane centerNode) {
        this.centerNode = centerNode;
    }
    public Text txtForgotPassword;

    @FXML
    private PasswordField txtPassword;

    public TextField txtUsername;

    public AnchorPane rootNode;

    public static String tempUsername;


    public void btnLoginOnAction(ActionEvent event) throws IOException{
      String userName = txtUsername.getText();
      String pw = txtPassword.getText();

        try {
            checkCredentials(userName,pw);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public  void checkCredentials(String userName, String pw) throws SQLException, IOException {
        String sql = "SELECT username,password FROM Users WHERE username = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setObject(1,userName);

        ResultSet resultSet = pst.executeQuery();
        if((userName.isEmpty())&&(pw.isEmpty())){
            new Alert(Alert.AlertType.INFORMATION,"Empty fields!Try again").show();
        }
        else if(resultSet.next()){
            String dbPw = resultSet.getString("password");
            if(pw.isEmpty()){
                new Alert(Alert.AlertType.INFORMATION,"Password is empty!Try again").show();
            }
            else if(dbPw.equals(pw)){

                navigateToTheDashboard();

            }else{
                new Alert(Alert.AlertType.ERROR,"Sorry!Password is incorrect").show();
            }
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Sorry!Username can't be found").show();
        }

    }

    public  void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage =(Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void hyperlinkSignUpOnAction(ActionEvent event) throws IOException {
         AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/register_form.fxml"));
         Scene scene = new Scene(rootNode);
         Stage stage = (Stage) this.rootNode.getScene().getWindow();
         stage.setScene(scene);

         stage.setTitle("Register Form");
         stage.centerOnScreen();
    }

    public void txtForgotPasswordOnAction(MouseEvent event) throws IOException {
        tempUsername = txtUsername.getText();
        String pw = txtPassword.getText();

        try {
            checkPasswordCredentials(tempUsername);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    public void checkPasswordCredentials(String tempUsername) throws SQLException, IOException {

        String sql = "SELECT * FROM Users WHERE username = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setObject(1,tempUsername);

        ResultSet resultSet = pst.executeQuery();

        if(tempUsername.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Empty Fields!Enter username").show();
        }
        else if (resultSet.next()) {
                String dbUsername = resultSet.getString("username");

                if (dbUsername.equals(tempUsername)) {
                    AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/forgotPassword_form.fxml"));
                    Scene scene = new Scene(rootNode);
                    Stage stage = (Stage) txtForgotPassword.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Reset Password");
                    stage.centerOnScreen();


                }
        }else{
            new Alert(Alert.AlertType.ERROR, "Sorry!Invalid username").show();
        }
    }
    @FXML
    void txtPasswordOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.Password,txtPassword);
    }

    @FXML
    void txtUsernameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.UserName,txtUsername);
    }

    public boolean isUsernameValid(){
        if(!Regex.setTextColor(TextFields.UserName,txtUsername));
        return true;
    }

    public boolean isPasswordValid(){
        if(!Regex.setTextColor(TextFields.Password,txtPassword));
        return true;
    }
    @FXML
    void txtUsernameOnAction(ActionEvent event) {
      if(isUsernameValid()){
          new Alert(Alert.AlertType.INFORMATION,"Invalid username!").show();
      }
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
       if (isPasswordValid()){
           new Alert(Alert.AlertType.INFORMATION,"Invalid Password!").show();
       }
    }
}
