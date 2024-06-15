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
import lk.ijse.model.MaterialDetail;
import lk.ijse.model.RawMaterial;
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.MaterialDetailTm;
import lk.ijse.repository.MaterialDetailRepo;
import lk.ijse.repository.RawMaterialRepo;
import lk.ijse.repository.SupplierRepo;

import javafx.scene.input.KeyEvent;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MaterialDetailFormController {

    @FXML
    private ComboBox<String> cmbMaterialId;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colMaterialId;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPayment;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    public AnchorPane materialDetailPane;

    @FXML
    private TableView<MaterialDetailTm> tblMaterialDetail;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtMaterialOrderId;

    @FXML
    private TextField txtMaterialQty;

    @FXML
    private TextField txtPayment;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private Button btnUpdate;

    private Tooltip tooltip;

    public void initialize(){
        getMaterialIds();
        getSupplierIds();
        setCellValueFactory();
        loadAllDetails();
        getCurrentMtOrderId();

        btnUpdate.setOnMouseExited(event -> {
            if (tooltip != null) {
                tooltip.hide();
            }
        });

        txtMaterialQty.setOnKeyReleased(event -> {
            updatePaymentField();
        });
    }

    private void updatePaymentField() {
        try {
            int qty = Integer.parseInt(txtMaterialQty.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText()); // You may need to add validation for this as well
            double payment = qty * unitPrice;
            txtPayment.setText(String.valueOf(payment));
        } catch (NumberFormatException e) {
            txtPayment.setText("");
        }
    }

    private void loadAllDetails() {
        ObservableList<MaterialDetailTm> obList = FXCollections.observableArrayList();

        try {
            List<MaterialDetail> materialDetailList = MaterialDetailRepo.getAllDetails();
            for (MaterialDetail materialDetail : materialDetailList){
                MaterialDetailTm detailTm = new MaterialDetailTm(
                        materialDetail.getMaterialOrderId(),
                        materialDetail.getMaterialId(),
                        materialDetail.getSupplierId(),
                        materialDetail.getQty(),
                        materialDetail.getUnitPrice(),
                        materialDetail.getPayment(),
                        materialDetail.getDate()
                );
                obList.add(detailTm);
            }
            tblMaterialDetail.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("materialOrderId"));
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void getCurrentMtOrderId(){
        try {
            String currentId = MaterialDetailRepo.getCurrentId();
            String nextId = generateNextMtOrderId(currentId);
            txtMaterialOrderId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextMtOrderId(String currentId) {
        if (currentId != null && currentId.matches("M\\d+")) {
            int idNum = Integer.parseInt(currentId.substring(1));
            return "M" + String.format("%03d", ++idNum);
        }
        return "M001";
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
       clearFields();
    }

    private void clearFields() {
        txtMaterialOrderId.setText("");
        cmbMaterialId.setValue("");
        cmbSupplierId.setValue("");
        txtMaterialQty.setText("");
        txtUnitPrice.setText("");
        txtDate.setText("");
        txtPayment.setText("");
        getCurrentMtOrderId();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String materialOrderId = txtMaterialOrderId.getText();

        try {
            boolean isDeleted = MaterialDetailRepo.deleteDetails(materialOrderId);
            if(materialOrderId.isEmpty()){
                new Alert(Alert.AlertType.INFORMATION,"Invalid material order id!").show();
            }
            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted successfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String materialOrderId = txtMaterialOrderId.getText();
        String materialId = cmbMaterialId.getValue();
        String supplierId = cmbSupplierId.getValue();
        int qty = Integer.parseInt(txtMaterialQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double payment = qty*unitPrice;
        txtPayment.setText(String.valueOf(payment));
        Date date = Date.valueOf(txtDate.getText());

        MaterialDetail materialDetail = new MaterialDetail(materialOrderId,materialId,supplierId,qty,unitPrice,payment,date);

        try {

            if((materialOrderId.isEmpty()) || (materialId.isEmpty()) || (supplierId.isEmpty()) || (txtMaterialQty.getText().isEmpty()) || (txtUnitPrice.getText().isEmpty()) || (txtDate.getText().isEmpty()) || (txtPayment.getText().isEmpty())){
                new Alert(Alert.AlertType.INFORMATION,"Empty Fields!Try again").show();
            }
            else if(isValid()){
                boolean isSaved = MaterialDetailRepo.saveDetails(materialDetail);
                if(isSaved){
                    new Alert(Alert.AlertType.CONFIRMATION,"Saved successfully!").show();
                }
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    @FXML
    void txtQtyOnKeyReleased(KeyEvent event) {

    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String materialOrderId = txtMaterialOrderId.getText();
        String materialId = cmbMaterialId.getValue();
        String supplierId = cmbSupplierId.getValue();
        int qty = Integer.parseInt(txtMaterialQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double payment = qty*unitPrice;
        Date date = Date.valueOf(txtDate.getText());

        MaterialDetail materialDetail = new MaterialDetail(materialOrderId,materialId,supplierId,qty,unitPrice,payment,date);

        try {
            if((materialOrderId.isEmpty()) || (materialId.isEmpty()) || (supplierId.isEmpty()) || (txtMaterialQty.getText().isEmpty()) || (txtUnitPrice.getText().isEmpty()) || (txtDate.getText().isEmpty()) || (txtPayment.getText().isEmpty())){
                new Alert(Alert.AlertType.INFORMATION,"Empty Fields!Try again").show();
            }
            if(isValid()) {
                boolean isUpdated = MaterialDetailRepo.updateDetails(materialDetail);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated successfully!").show();
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

        tooltip.setText("Update information using material order id!");

        tooltip.show(btnUpdate, event.getScreenX() + 10, event.getScreenY() + 10);
    }

    @FXML
    void cmbMaterialOnAction(ActionEvent event) {
        String materialId = cmbMaterialId.getValue();

        try {
            RawMaterial rawMaterial = RawMaterialRepo.searchMaterialById(materialId);
            txtUnitPrice.setText(String.valueOf(rawMaterial.getUnitPrice()));
            txtMaterialQty.requestFocus();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMaterialIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> mtIDList = RawMaterialRepo.getIds();

            for (String materialId : mtIDList) {
                obList.add(materialId);
            }
            cmbMaterialId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getSupplierIds(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> supIdList = SupplierRepo.getIds();
            for(String supplierId : supIdList){
                obList.add(supplierId);
            }
            cmbSupplierId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String materialOrderId = txtMaterialOrderId.getText();

        MaterialDetail materialDetail = MaterialDetailRepo.searchOrderById(materialOrderId);

        if(materialDetail != null){
            txtMaterialOrderId.setText(materialDetail.getMaterialOrderId());
            cmbMaterialId.setValue(materialDetail.getMaterialId());
            cmbSupplierId.setValue(materialDetail.getSupplierId());
            txtMaterialQty.setText(String.valueOf(materialDetail.getQty()));
            txtPayment.setText(String.valueOf(materialDetail.getPayment()));
            txtDate.setText(String.valueOf(materialDetail.getDate()));
        }
        else{
            new Alert(Alert.AlertType.ERROR,"Material Order not found!").show();
        }
    }

    @FXML
    void txtMtOrderIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.MaterialOrderID,txtMaterialOrderId);
    }

    @FXML
    void txtPaymentOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.DOUBLE,txtPayment);
    }

    public boolean isValid(){
        if (!Regex.setTextColor(TextFields.MaterialOrderID,txtMaterialOrderId)) return false;
        if(!Regex.setTextColor(TextFields.DOUBLE,txtPayment))return false;
        return true;
    }


}

