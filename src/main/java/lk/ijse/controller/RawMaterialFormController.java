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
import lk.ijse.model.RawMaterial;
import lk.ijse.model.tm.RawMaterialTm;
import lk.ijse.repository.RawMaterialRepo;

import javafx.scene.input.KeyEvent;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class RawMaterialFormController {

    private AnchorPane centerNode;

    public void setCenterNode(AnchorPane centerNode) {
        this.centerNode = centerNode;
    }

    @FXML
    public AnchorPane materialPane;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<RawMaterialTm> tblRawMaterial;

    @FXML
    private TextField txtMaterialId;

    @FXML
    private TextField txtMaterialName;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private Button btnUpdate;

    private Tooltip tooltip;

    public void initialize(){
        setCellValueFactory();
        loadAllMaterials();
        getCurrentMaterialId();

        btnUpdate.setOnMouseExited(event -> {
            if (tooltip != null) {
                tooltip.hide();
            }
        });
    }

    private void loadAllMaterials() {
        ObservableList<RawMaterialTm> obList = FXCollections.observableArrayList();

        try {
            List<RawMaterial> rawMaterialList =  RawMaterialRepo.getAllMaterials();
            for(RawMaterial rawMaterial : rawMaterialList){
                RawMaterialTm materialTm = new RawMaterialTm(
                        rawMaterial.getMaterialId(),
                        rawMaterial.getMaterialName(),
                        rawMaterial.getUnitPrice()
                );
                obList.add(materialTm);
            }
            tblRawMaterial.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    }

    private void getCurrentMaterialId(){
        try {
            String currentId = RawMaterialRepo.getCurrentId();
            String nextId = generateNextMaterialId(currentId);
            txtMaterialId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextMaterialId(String currentId) {
        if (currentId != null && currentId.matches("R\\d+")) {
            int idNum = Integer.parseInt(currentId.substring(1));
            return "R" + String.format("%03d", ++idNum);
        }
        return "R001";
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtMaterialId.setText("");
        txtMaterialName.setText("");
        txtUnitPrice.setText("");
        getCurrentMaterialId();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
      String materialId = txtMaterialId.getText();

        try {
            boolean isDeleted = RawMaterialRepo.deleteMaterials(materialId);
            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"Material deleted successfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
       String materialId = txtMaterialId.getText();
       String materialName = txtMaterialName.getText();
       double unitPrice = Double.parseDouble(txtUnitPrice.getText());

        RawMaterial rawMaterial = new RawMaterial(materialId,materialName,unitPrice);

        try {
            if((materialId.isEmpty()) || (materialName.isEmpty()) || (txtUnitPrice.getText().isEmpty())){
                new Alert(Alert.AlertType.CONFIRMATION,"Empty Fields!Try again").show();
            }
            else if(isValid()){
                boolean isSaved = RawMaterialRepo.saveMaterials(rawMaterial);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Material saved successfully!").show();
                    loadAllMaterials();
                    clearFields();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String materialId = txtMaterialId.getText();
        String materialName = txtMaterialName.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());

        RawMaterial rawMaterial = new RawMaterial(materialId,materialName,unitPrice);

        try {
            if((materialId.isEmpty()) || (materialName.isEmpty()) || (txtUnitPrice.getText().isEmpty())){
                new Alert(Alert.AlertType.CONFIRMATION,"Empty Fields!Try again").show();
            }
            if(isValid()) {
                boolean isUpdated = RawMaterialRepo.updateMaterials(rawMaterial);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Material updated successfully!").show();
                    loadAllMaterials();
                    clearFields();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String materialId = txtMaterialId.getText();

        RawMaterial rawMaterial = RawMaterialRepo.searchMaterialById(materialId);

        if(rawMaterial != null){
            txtMaterialId.setText(rawMaterial.getMaterialId());
            txtMaterialName.setText(rawMaterial.getMaterialName());
            txtUnitPrice.setText(String.valueOf(rawMaterial.getUnitPrice()));
        }
        else{
            new Alert(Alert.AlertType.ERROR,"Material not found!").show();
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

        tooltip.setText("Update information using Material Id!");

        tooltip.show(btnUpdate, event.getScreenX() + 10, event.getScreenY() + 10);
    }

    @FXML
    void btnDetailsOnAction(ActionEvent event) throws IOException {
        AnchorPane materialDetailPane = FXMLLoader.load(getClass().getResource("/view/materialDetail_form.fxml"));
        centerNode.getChildren().clear();
        centerNode.getChildren().add(materialDetailPane);
    }

    public  void txtMaterialIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.MaterialID,txtMaterialId);
    }

    @FXML
    void txtUnitPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.DOUBLE,txtUnitPrice);
    }

    public boolean isValid(){
        if (!Regex.setTextColor(TextFields.MaterialID,txtMaterialId)) return false;
        if(!Regex.setTextColor(TextFields.DOUBLE,txtUnitPrice))return false;
        return true;
    }
}

