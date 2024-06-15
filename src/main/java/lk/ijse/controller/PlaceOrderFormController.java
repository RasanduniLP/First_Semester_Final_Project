package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DBConnection;
import lk.ijse.model.*;
import lk.ijse.model.tm.CartTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.OrderRepo;
import lk.ijse.repository.PlaceOrderRepo;
import lk.ijse.repository.ProductRepo;

import javafx.scene.input.KeyEvent;
import lk.ijse.smtp.Mail;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.lowagie.text.Annotation.URL;
import static lk.ijse.util.TextFields.Name;
import static lk.ijse.util.TextFields.OrderId;

public class PlaceOrderFormController {

    @FXML
    public AnchorPane placeOrderPane;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbProductId;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<CartTm> tblOrderDetails;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtxProductName;

    @FXML
    private TextField txtxUnitPrice;

    @FXML
    private TextField txtPayment;

    @FXML
    private TextField txtQtyOnHand;

    private ObservableList<CartTm> obList = FXCollections.observableArrayList();


    public void initialize(){
        getCurrentOrderId();
        setDate();
        getCustomerIds();
        getProductIds();
        setCellValueFactory();
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }
    private void getCurrentOrderId(){
        try {
            String currentId = OrderRepo.getCurrentId();
            String nextId = generateNextOrderId(currentId);
            txtOrderId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextOrderId(String currentId) {
        if (currentId != null) {
            // Extract the numeric part of the ID
            int idNum = Integer.parseInt(currentId.substring(1));
            // Increment the numeric part of the ID
            idNum++;
            // Format the numeric part to include leading zeros
            String formattedIdNum = String.format("%03d", idNum);
            return "O" + formattedIdNum;
        }
        // Start from O001 if currentId is null
        return "O001";
    }


    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }

    private void getProductIds(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> prIdList = ProductRepo.getIds();
            for (String productId : prIdList){
                obList.add(productId);
            }
            cmbProductId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCustomerIds(){
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
           List<String> cusIdList = CustomerRepo.getIds();
           for (String customerId : cusIdList){
               obList.add(customerId);
           }
           cmbCustomerId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String productId = cmbProductId.getValue();
        String productName = txtxProductName.getText();
        double unitPrice = Double.parseDouble(txtxUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = unitPrice * qty;
        Button btnRemove = new Button("remove");
        btnRemove.setCursor(Cursor.HAND);
        btnRemove.setStyle("-fx-background-color: #F67280");

            btnRemove.setOnAction((e) -> {
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

                if (type.orElse(no) == yes) {
                    int selectedIndex = tblOrderDetails.getSelectionModel().getSelectedIndex();
                    if (type.orElse(no) == yes) {
                        obList.remove(selectedIndex);

                        tblOrderDetails.refresh();
                        calculateNetAmount();

                    } else {
                        // Handle case when no row is selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No item selected to remove!");
                        alert.showAndWait();
                    }
                }
            });
            for (int i = 0; i < tblOrderDetails.getItems().size(); i++) {
                if (productId.equals(colId.getCellData(i))) {

                    CartTm tm = obList.get(i);
                    qty += tm.getQty();
                    total = qty * unitPrice;

                    tm.setQty(qty);
                    tm.setTotal(total);

                    tblOrderDetails.refresh();

                    calculateNetAmount();
                    return;
                }

        }
        CartTm tm = new CartTm(productId, productName, unitPrice, qty,total, btnRemove);
        obList.add(tm);

        if(isValid()) {
            tblOrderDetails.setItems(obList);
            calculateNetAmount();
            txtQty.setText("");
        }

    }
    private void calculateNetAmount() {
        int netAmount = 0;
        for (int i = 0; i < tblOrderDetails.getItems().size(); i++) {
            netAmount += (double) colTotal.getCellData(i);
        }
        txtTotal.setText(String.valueOf(netAmount));
        txtPayment.setText(String.valueOf(netAmount));
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
       String customerName = txtCustomerName.getText();

        try {
            Customer customer = CustomerRepo.searchCustomerByName(customerName);

            cmbCustomerId.setValue(customer.getCustomer_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbProductOnAction(ActionEvent event) {
        String productId = cmbProductId.getValue();

        try {
            Product product = ProductRepo.searchProductById(productId);

            if(product != null){
                txtxProductName.setText(product.getProductName());
                txtxUnitPrice.setText(String.valueOf(product.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(product.getQtyOnHand()));
            }
            txtQty.requestFocus();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void txtQtyOnAction(ActionEvent event) {
      btnAddToCartOnAction(event);
    }


    public  void placeOrderOnAction(ActionEvent event) {
       String orderId = txtOrderId.getText();
       String customerId = cmbCustomerId.getValue();
       Date date = Date.valueOf(LocalDate.now());
       double payment = Double.parseDouble(txtPayment.getText());

        var order = new Order(orderId,customerId,date,payment);

       List<OrderDetail> odtList = new ArrayList<>();

        for (int i = 0; i < tblOrderDetails.getItems().size(); i++) {
            CartTm tm = obList.get(i);

            OrderDetail od = new OrderDetail(
                    orderId,
                    tm.getProductId(),
                    tm.getQty(),
                    tm.getUnitPrice()
            );

            odtList.add(od);
        }
        PlaceOrder po = new PlaceOrder(order,odtList);

        try {

            boolean isPlaced = PlaceOrderRepo.placeOrder(po);
            boolean isMailSent = SendMail();
            if(isPlaced){
                if (isMailSent) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order placed and email sent successfully !").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to send email!").show();
                }
            }
            else{
               new Alert(Alert.AlertType.ERROR,"Order is not placed!").show();
            }
        } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private boolean SendMail() throws SQLException {
        try {
            Mail mail = new Mail();
            mail.setMsg("We received your payment successfully!" + "\n" +
                    "\nTime : " +
                    Time.valueOf(LocalTime.now()) + "\n" +
                    "Date : " +
                    Date.valueOf(LocalDate.now()));//email message
            mail.setTo(CustomerRepo.searchCustomerById(cmbCustomerId.getValue()).getEmail()); //receiver's mail
            mail.setSubject("Alert"); //email subject

            Thread thread = new Thread(mail);
            thread.start();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void txtNameOnKeyReleased(KeyEvent event) {
       Regex.setTextColor(Name,txtCustomerName);
    }

    @FXML
    void txtOrderIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(OrderId,txtOrderId);
    }

    public boolean isValid(){
        if (!Regex.setTextColor(OrderId,txtOrderId)) return false;
        if (!Regex.setTextColor(Name,txtCustomerName))return false;
        return true;
    }

    @FXML
    void btnPrintBillOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/order_bill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("OrderId",txtOrderId.getText());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,data,  DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
       clearFields();
    }

    private void clearFields() {
        txtPayment.setText("");
        cmbCustomerId.setValue("");
        txtCustomerName.setText("");
        cmbProductId.setValue("");
        txtxUnitPrice.setText("");
        txtQtyOnHand.setText("");
        txtQty.setText("");
        txtTotal.setText("");
        txtxProductName.setText("");
        getCurrentOrderId();
    }

}
