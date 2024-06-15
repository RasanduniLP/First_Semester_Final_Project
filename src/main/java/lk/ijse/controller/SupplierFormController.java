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
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.SupplierTm;
import lk.ijse.repository.SupplierRepo;

import javafx.scene.input.KeyEvent;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SupplierFormController {

    @FXML
    public AnchorPane supplierPane;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtSupplierAddress;

    @FXML
    private TextField txtSupplierContact;

    @FXML
    private TextField txtSupplierId;

    @FXML
    private TextField txtSupplierName;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTelNo;

    @FXML
    private Button btnUpdate;

    private Tooltip tooltip;

    public void initialize(){
        setCellValueFactory();
        loadAllSuppliers();
        getCurrentSupplierId();

        boolean isUpdating = true;
        txtSupplierId.setEditable(!isUpdating);

        btnUpdate.setOnMouseExited(event -> {
            if (tooltip != null) {
                tooltip.hide();
            }
        });
    }
    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelNo.setCellValueFactory(new PropertyValueFactory<>("contact"));

    }

    private void loadAllSuppliers(){
        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();

        try {
            List<Supplier> supplierList = SupplierRepo.getAllSuppliers();
            for (Supplier supplier : supplierList){
                SupplierTm supTm = new SupplierTm(
                        supplier.getSupplierId(),
                        supplier.getSupplierName(),
                        supplier.getAddress(),
                        supplier.getContact()
                );
                obList.add(supTm);

                tblSupplier.setItems(obList);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentSupplierId(){
        try {
            String currentId = SupplierRepo.getCurrentId();
            String nextId = generateNextSupplierId(currentId);
            txtSupplierId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextSupplierId(String currentId) {
        if (currentId != null && currentId.matches("S\\d+")) {
            int idNum = Integer.parseInt(currentId.substring(1));
            return "S" + String.format("%03d", ++idNum);
        }
        return "S001";
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
      clearFields();
    }

    private void clearFields() {
        txtSupplierId.setText("");
        txtSupplierName.setText("");
        txtSupplierAddress.setText("");
        txtSupplierContact.setText("");
        getCurrentSupplierId();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
       String supplierId = txtSupplierId.getText();

        try {
            boolean isDeleted = SupplierRepo.deleteSuppliers(supplierId);
            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Supplier deleted successfully!").show();
            }
            else{
                new Alert(Alert.AlertType.ERROR,"Supplier not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String supplierId = txtSupplierId.getText();
        String supplierName = txtSupplierName.getText();
        String address = txtSupplierAddress.getText();
        String contact = txtSupplierContact.getText();

        Supplier supplier = new Supplier(supplierId,supplierName,address,contact);

        try {
            if((supplierId.isEmpty()) || (supplierName.isEmpty()) || (address.isEmpty()) || (contact.isEmpty())){
                new Alert(Alert.AlertType.INFORMATION,"Empty Fields!Try again").show();
            }
            else if(isValid()){
                boolean isSaved = SupplierRepo.saveSuppliers(supplier);
                if(isSaved){
                    new Alert(Alert.AlertType.CONFIRMATION,"Supplier saved successfully!").show();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
         String supplierId = txtSupplierId.getText();
         String supplierName = txtSupplierName.getText();
         String address = txtSupplierAddress.getText();
         String contact = txtSupplierContact.getText();

         Supplier supplier = new Supplier(supplierId,supplierName,address,contact);

        try {
            if((supplierId.isEmpty()) || (supplierName.isEmpty()) || (address.isEmpty()) || (contact.isEmpty())){
                new Alert(Alert.AlertType.INFORMATION,"Empty Fields!Try again").show();
            }
            if(isValid()) {
                boolean isUpdated = SupplierRepo.updateSuppliers(supplier);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier updated successfully!").show();
                }
            }
        } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnMouseExited(MouseEvent event) {
        if (tooltip != null) {
            tooltip.hide();
        }
    }

    @FXML
    void btnUpdateOnMouseMoved(MouseEvent event) {
        if (tooltip == null) {
            tooltip = new Tooltip();
            tooltip.setAutoHide(true);
        }

        tooltip.setText("You can update your information using your contact number!");

        tooltip.show(btnUpdate, event.getScreenX() + 10, event.getScreenY() + 10);
    }
    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String contact = txtSupplierContact.getText();

        Supplier supplier = SupplierRepo.searchSupplierByContact(contact);

        if(supplier != null){
            txtSupplierId.setText(supplier.getSupplierId());
            txtSupplierName.setText(supplier.getSupplierName());
            txtSupplierAddress.setText(supplier.getAddress());
            txtSupplierContact.setText(supplier.getContact());
        }
        else {
            new Alert(Alert.AlertType.ERROR,"Supplier is not found!").show();
        }

    }

    @FXML
    void txtSupplierIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.SupplierID,txtSupplierId);
    }

    public boolean isValid() {
        if (!Regex.setTextColor(TextFields.SupplierID, txtSupplierId)) return false;
        if (!Regex.setTextColor(TextFields.Contact,txtSupplierContact))return false;
        return true;
    }

    public boolean isNameValid(){
        if(!Regex.setTextColor(TextFields.Name,txtSupplierName));
        return true;
    }
    @FXML
    void txtSupNameOnKeyReleased(KeyEvent event) {
      Regex.setTextColor(TextFields.Name,txtSupplierName);
    }

    @FXML
    void txtContactOnKeyReleased(KeyEvent event) {
       Regex.setTextColor(TextFields.Contact,txtSupplierContact);
    }

    @FXML
    void txtSupNameOnAction(ActionEvent event) {
       if (isNameValid()){
           new Alert(Alert.AlertType.INFORMATION,"Should have to consist two names!" +"\n"+
                   "ex:Amali Perera").show();
       }
    }
}

