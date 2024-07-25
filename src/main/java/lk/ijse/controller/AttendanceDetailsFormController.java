package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.Attendance;
import lk.ijse.model.tm.AttendanceTm;
import lk.ijse.repository.AttendanceRepo;
import lk.ijse.repository.EmployeeRepo;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class AttendanceDetailsFormController {

    @FXML
    public static AnchorPane attendancePane;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private TableColumn<?, ?> colAttendanceId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colInTime;

    @FXML
    private TableColumn<?, ?> colOffTime;

    @FXML
    private TableView<AttendanceTm> tblAttendance;

    @FXML
    private TextField txtAttendanceId;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtInTime;

    @FXML
    private TextField txtOffTime;

    public   void initialize(){
        getEmployeeIds();
        setCellValueFactory();
        loadAllDetails();
        getCurrentAttendanceId();

    }

    private void setCellValueFactory(){
        colAttendanceId.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colInTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colOffTime.setCellValueFactory(new PropertyValueFactory<>("offTime"));
    }

    private void loadAllDetails(){
        ObservableList<AttendanceTm> obList = FXCollections.observableArrayList();

        try {
            List<Attendance> attendanceList = AttendanceRepo.getAllDetails();
            for(Attendance attendance: attendanceList){
                AttendanceTm tm = new AttendanceTm(
                        attendance.getAttendanceId(),
                        attendance.getEmployeeId(),
                        attendance.getDate(),
                        attendance.getInTime(),
                        attendance.getOffTime()
                );
                obList.add(tm);
            }
            tblAttendance.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getEmployeeIds(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> empIdList = EmployeeRepo.getIds();
            for (String employee : empIdList){
                obList.add(employee);
            }
            cmbEmployeeId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentAttendanceId(){
        try {
            String currentId = AttendanceRepo.getCurrentId();
            String nextId = generateNextAttendanceId(currentId);
            txtAttendanceId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextAttendanceId(String currentId) {
        if (currentId != null && currentId.matches("A\\d+")){
            int idNum = Integer.parseInt(currentId.substring(1));
            return "A" + String.format("%03d", ++idNum);
        }
        return "A001";
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
       clearFields();
    }

    private void clearFields() {
        txtAttendanceId.setText("");
        cmbEmployeeId.setValue("");
        txtDate.setText("");
        txtInTime.setText("");
        txtOffTime.setText("");
        getCurrentAttendanceId();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String attendanceId = txtAttendanceId.getText();

        try {
            boolean isDeleted = AttendanceRepo.deleteAttendance(attendanceId);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String attendanceId = txtAttendanceId.getText();
        String employeeId = cmbEmployeeId.getValue();
        Date date = Date.valueOf(txtDate.getText());
        String inTime = txtInTime.getText();
        String offTime = txtOffTime.getText();

        Attendance attendance = new Attendance(attendanceId,employeeId,date,inTime,offTime);
        try {
            if ((attendanceId.isEmpty()) || (employeeId.isEmpty()) || (txtDate.getText().isEmpty()) || (inTime.isEmpty()) || (offTime.isEmpty())) {
                new Alert(Alert.AlertType.INFORMATION, "Empty Fields!Try again").show();
            }
            else if(isValid()){
                boolean isSaved = AttendanceRepo.saveAttendance(attendance);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Attendance Details saved successfully!").show();
                    loadAllDetails();
                }
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String attendanceId = txtAttendanceId.getText();
        String employeeId = cmbEmployeeId.getValue();
        Date date = Date.valueOf(txtDate.getText());
        String inTime = txtInTime.getText();
        String offTime = txtOffTime.getText();

        Attendance attendance = new Attendance(attendanceId,employeeId,date,inTime,offTime);

        try {
            boolean isUpdated = AttendanceRepo.updateAttendance(attendance);
            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Attendance Details updated successfully!").show();
                loadAllDetails();
            }
        } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String attendanceId = txtAttendanceId.getText();

       Attendance attendance = AttendanceRepo.searchAttendanceById(attendanceId);
        if(attendance != null){
            txtAttendanceId.setText(attendance.getAttendanceId());
            cmbEmployeeId.setValue(attendance.getEmployeeId());
            txtDate.setText(String.valueOf(attendance.getDate()));
            txtInTime.setText(attendance.getInTime());
            txtOffTime.setText(attendance.getOffTime());
        }
        else {
            new Alert(Alert.AlertType.ERROR,"Attendance Details not found!").show();
        }
    }


    public void txtAttendanceIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.AttendanceID,txtAttendanceId);
    }

    public boolean isValid(){
        if (!Regex.setTextColor(TextFields.AttendanceID,txtAttendanceId)) return false;
        return true;
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        
    }
}

