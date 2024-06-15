package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.User;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.UserRepo;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserId;

    @FXML
    private TextField txtUsername;

    public void initialize(){
        getCurrentUserId();
        boolean isUpdating = true;
        txtUserId.setEditable(!isUpdating);
    }
    private void getCurrentUserId(){
        try {
            String currentId = UserRepo.getCurrentId();
            String nextId = generateNextUserId(currentId);
            txtUserId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextUserId(String currentId) {
        if (currentId != null && currentId.matches("U\\d+")) {
            int idNum = Integer.parseInt(currentId.substring(1));
            return "U" + String.format("%03d", ++idNum);
        }
        return "U001";
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) throws SQLException {
        String userId = txtUserId.getText();
        String username = txtUsername.getText();
        String pw = txtPassword.getText();
        String email = txtEmail.getText();


            try {
                if ((userId.isEmpty()) || (username.isEmpty()) || (pw.isEmpty()) || (email.isEmpty())) {
                    new Alert(Alert.AlertType.INFORMATION, "Empty Files!Try again").show();
                }

                else {
                    User user = new User(userId, username, pw, email);
                    boolean isSaved = UserRepo.saveUsers(user);
                    if (isUserIdValid() && (isUserNameValid()) && (isPasswordValid()) && (isEmailValid())) {
                        if (isSaved) {
                            new Alert(Alert.AlertType.CONFIRMATION, "User saved successfully!").show();

                        }
                    }
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }


    }
    @FXML
    void hyperlinkLogInOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Login Form");
        stage.centerOnScreen();
    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.EMAIL,txtEmail);
    }

    @FXML
    void txtPasswordOnKeyReleased(KeyEvent event) {
       Regex.setTextColor(TextFields.Password,txtPassword);
    }

    @FXML
    void txtUserIdOnKeyReleased(KeyEvent event) {
       Regex.setTextColor(TextFields.UserID,txtUserId);
    }

    @FXML
    void txtUserNameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.UserName,txtUsername);
    }

    public boolean isUserIdValid(){
        if(!Regex.setTextColor(TextFields.UserID,txtUserId)) return false;
        return true;
    }
    public boolean isPasswordValid(){
        if(!Regex.setTextColor(TextFields.Password,txtPassword));
        return true;
    }
    public boolean isEmailValid(){
        if(!Regex.setTextColor(TextFields.EMAIL,txtEmail));
        return true;
    }
    public boolean isUserNameValid(){
        if(!Regex.setTextColor(TextFields.UserName,txtUsername));
        return true;
    }

    @FXML
    void txtUsernameOnAction(ActionEvent event) {
       if (isUserNameValid()){
           new Alert(Alert.AlertType.INFORMATION,"Username must contain at least 6 characters"+"\n"+"only with letters and numbers").show();
       }
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
       if(isPasswordValid()){
           new Alert(Alert.AlertType.INFORMATION,"Password must contain at least 8 characters"+"\n"+"with at least 1 digit,1 lowercase letter,1 uppercase letter and 1 special character").show();
       }
    }

    @FXML
    void txtEmailOnAction(ActionEvent event) {
       if (isEmailValid()){
           new Alert(Alert.AlertType.INFORMATION,"Invalid email").show();
       }
    }
}

