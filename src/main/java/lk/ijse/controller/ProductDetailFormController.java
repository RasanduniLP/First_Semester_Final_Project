package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.Product;
import lk.ijse.model.ProductDetail;
import lk.ijse.model.RawMaterial;
import lk.ijse.model.tm.ProductDetailTm;
import lk.ijse.repository.ProductDetailRepo;
import lk.ijse.repository.ProductRepo;
import lk.ijse.repository.RawMaterialRepo;
import lk.ijse.repository.SupplierRepo;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductDetailFormController {

    @FXML
    private ComboBox<String> cmbMaterialId;

    @FXML
    private ComboBox<String> cmbProductId;

    @FXML
    private TableColumn<?, ?> colMaterialId;

    @FXML
    private TableColumn<?, ?> colProductId;

    @FXML
    private AnchorPane productDetailPane;

    @FXML
    private TableView<ProductDetailTm> tblProductDetail;


    public void initialize(){
        setCellValueFactory();
        loadAllDetails();
        getProductIds();
        getMaterialIds();
    }

    private void getProductIds(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> prIdList = ProductRepo.getIds();
            for(String productId : prIdList){
                obList.add(productId);
            }
            cmbProductId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMaterialIds(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> mtIdList = RawMaterialRepo.getIds();
            for(String materialId : mtIdList){
                obList.add(materialId);
            }
            cmbMaterialId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
       clearFields();
    }

    private void clearFields() {
        cmbProductId.setValue("");
        cmbMaterialId.setValue("");
    }

    private void setCellValueFactory(){
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("materialId"));
    }

    private void loadAllDetails(){
        ObservableList<ProductDetailTm> obList = FXCollections.observableArrayList();

        try {
            List<ProductDetail> productDetailList = ProductDetailRepo.getAllDetails();
            for (ProductDetail productDetail : productDetailList){
                ProductDetailTm detailTm = new ProductDetailTm(
                       productDetail.getProductId(),
                        productDetail.getMaterialId()
                );
                obList.add(detailTm);
            }
            tblProductDetail.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
       String productId = cmbProductId.getValue();

        try {
            boolean isDeleted = ProductDetailRepo.deleteDetails(productId);
            if(isDeleted){
                new  Alert(Alert.AlertType.CONFIRMATION,"Details deleted successfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String productId = cmbProductId.getValue();
        String materialId = cmbMaterialId.getValue();

        ProductDetail productDetail = new ProductDetail(productId,materialId);

        try {
            if((productId.isEmpty()) || (materialId.isEmpty()) ){
                new Alert(Alert.AlertType.INFORMATION,"Empty Fields!Try again").show();
            }
            else {
                boolean isSaved = ProductDetailRepo.saveDetails(productDetail);
                if(isSaved){
                    new Alert(Alert.AlertType.CONFIRMATION,"Details saved successfully!").show();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String productId = cmbProductId.getValue();
        String materialId = cmbMaterialId.getValue();

        ProductDetail productDetail = new ProductDetail(productId,materialId);

        try {
            boolean isUpdated = ProductDetailRepo.updateDetails(productDetail);
            if((productId.isEmpty()) || (materialId.isEmpty()) ){
                new Alert(Alert.AlertType.INFORMATION,"Empty Fields!Try again").show();
            }
            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Details updated successfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void cmbProductOnAction(ActionEvent event) {
        String productId = cmbProductId.getValue();

        try {
            ProductDetail productDetail = ProductDetailRepo.searchDetailById(productId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane productDetailPane = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        Scene scene = new Scene(productDetailPane);
        Stage stage =(Stage) this.productDetailPane.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }


}

