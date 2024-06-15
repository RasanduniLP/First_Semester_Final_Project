package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lk.ijse.controller.LoginFormController.*;
import lk.ijse.repository.UserRepo;
import org.w3c.dom.Text;
import javafx.scene.input.MouseEvent;

public class ForgotPasswordFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtConfirmPassword;

    @FXML
    public  TextField txtPassword;

    public String tempUsername = LoginFormController.tempUsername;
    public void btnResetOnAction(ActionEvent event) {
      String newPassword = txtPassword.getText();
      String confirmPassword = txtConfirmPassword.getText();

     if(!newPassword.equals(confirmPassword)){
         new Alert(Alert.AlertType.ERROR,"Passwords are do not match! Try again").show();
     } else if((newPassword.isEmpty())&&(confirmPassword.isEmpty())) {
            new Alert(Alert.AlertType.INFORMATION, "Empty Fields!").show();
     }
        else{
         try {
             UserRepo.updateUser(newPassword,tempUsername);
             txtPassword.setText("");
            txtConfirmPassword.setText("");
         } catch (SQLException e) {
             new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
         }

     }
    }
    @FXML
    void hyperlinkLoginOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Login Form");
        stage.centerOnScreen();
    }

}
