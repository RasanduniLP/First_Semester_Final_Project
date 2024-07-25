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
import lk.ijse.model.Product;
import lk.ijse.model.tm.ProductTm;
import lk.ijse.repository.ProductRepo;

import javafx.scene.input.KeyEvent;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static java.lang.Double.parseDouble;

public class ProductFormController {

    private AnchorPane centerNode;

    public void setCenterNode(AnchorPane centerNode) {
        this.centerNode = centerNode;
    }

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;


    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane productPane;

    @FXML
    private TableView<ProductTm> tblProduct;

    @FXML
    public TextField txtProductId;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtQtyOnHand;
    @FXML
    public TextField txtUnitPrice;

    @FXML
    private Button btnUpdate;

    private Tooltip tooltip;

    public void initialize(){
        setCellValueFactory();
        loadAllProducts();
        getCurrentProductId();


        btnUpdate.setOnMouseExited(event -> {
            if (tooltip != null) {
                tooltip.hide();
            }
        });
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }
    private void loadAllProducts() {
        ObservableList<ProductTm> obList = FXCollections.observableArrayList();

        try {
            List<Product> productList = ProductRepo.getAllProducts();
            for(Product product : productList){
                ProductTm proTm = new ProductTm(
                        product.getProductId(),
                        product.getProductName(),
                        product.getUnitPrice(),
                        product.getQtyOnHand()
                );
                obList.add(proTm);
            }
            tblProduct.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentProductId(){
        try {
            String currentId = ProductRepo.getCurrentId();
            String nextId = generateNextProductId(currentId);
            txtProductId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextProductId(String currentId) {
        if (currentId != null && currentId.matches("P\\d+")) {
            int idNum = Integer.parseInt(currentId.substring(1));
            return "P" + String.format("%03d", ++idNum);
        }
        return "P001";
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
       clearFields();
    }

    private void clearFields() {
        txtProductId.setText("");
        txtProductName.setText("");
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
        getCurrentProductId();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String productId = txtProductId.getText();

        try {
            boolean isDeleted = ProductRepo.deleteProducts(productId);
            if(isDeleted){
                new  Alert(Alert.AlertType.CONFIRMATION,"Product deleted successfully!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String productId = txtProductId.getText();
        String productName = txtProductName.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        Product product = new Product(productId,productName,unitPrice,qtyOnHand);

        try {
            if((productId.isEmpty()) || (productName.isEmpty()) || (txtUnitPrice.getText().isEmpty()) || (txtQtyOnHand.getText().isEmpty())){
                new Alert(Alert.AlertType.INFORMATION,"Empty fields!").show();
            }
            if(isValid()){
                boolean isSaved = ProductRepo.saveProducts(product);
                if(isSaved){
                    new Alert(Alert.AlertType.CONFIRMATION,"Product saved successfully!").show();
                    loadAllProducts();
                    clearFields();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String productId = txtProductId.getText();
        String productName = txtProductName.getText();
        double unitPrice = parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        Product product = new Product(productId,productName,unitPrice,qtyOnHand);

        try {
            if((productId.isEmpty()) || (productName.isEmpty()) || (txtUnitPrice.getText().isEmpty()) || (txtQtyOnHand.getText().isEmpty())){
                new Alert(Alert.AlertType.INFORMATION,"Empty fields!").show();
            }
            if(isValid()) {
                boolean isUpdated = ProductRepo.updateProducts(product);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product updated successfully!").show();
                    loadAllProducts();
                    clearFields();
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

        tooltip.setText("Update information using product name!");

        tooltip.show(btnUpdate, event.getScreenX() + 10, event.getScreenY() + 10);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String productName = txtProductName.getText();

        Product product = ProductRepo.searchProductByName(productName);
        if (product != null) {
            txtProductId.setText(product.getProductId());
            txtProductName.setText(product.getProductName());
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(product.getQtyOnHand()));

        }
        else{
            new Alert(Alert.AlertType.ERROR,"Product is not found!").show();
        }
    }
    @FXML
    void btnDetailsOnAction(ActionEvent event) throws IOException {
        AnchorPane productDetailPane = FXMLLoader.load(getClass().getResource("/view/productDetail_form.fxml"));
        centerNode.getChildren().clear();
        centerNode.getChildren().add(productDetailPane);
    }

    @FXML
    void txtProductIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.ProductID,txtProductId);
    }

    @FXML
    void txtUnitPriceOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.DOUBLE,txtUnitPrice);
    }

    public boolean isValid(){
        if (!Regex.setTextColor(TextFields.ProductID,txtProductId)) return false;
        if(!Regex.setTextColor(TextFields.DOUBLE,txtUnitPrice))return false;
        return true;
    }
}
