package lk.ijse.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.Customer;
import lk.ijse.model.tm.CustomerTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.OrderRepo;
import lk.ijse.util.Regex;
import lk.ijse.util.TextFields;

import java.sql.SQLException;
import java.util.List;



public class CustomerFormController {


    @FXML
    public AnchorPane customerPane;

    @FXML
    private TextField txtCustomerAddress;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerTel;

    @FXML
    private TextField txtEmailAddress;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private Button btnUpdate;

    private Tooltip tooltip;

    private boolean typingComplete = false;

    public void initialize(){
      setCellValueFactory();
      loadAllCustomers();
      getCurrentCustomerId();


        btnUpdate.setOnMouseExited(event -> {
            if (tooltip != null) {
                tooltip.hide();
            }
        });


    }
    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("contact_number"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllCustomers(){
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAllCustomers();
            for(Customer customer : customerList){
                CustomerTm cusTm = new CustomerTm(
                        customer.getCustomer_id(),
                        customer.getCustomer_name(),
                        customer.getAddress(),
                        customer.getContact_number(),
                        customer.getEmail()
                );
                obList.add(cusTm);
            }
            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
      clearFields();
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        txtCustomerAddress.setText("");
        txtCustomerTel.setText("");
        txtEmailAddress.setText("");
        getCurrentCustomerId();
    }

    private void getCurrentCustomerId(){
        try {
            String currentId = CustomerRepo.getCurrentId();
            String nextId = generateNextCustomerId(currentId);
            txtCustomerId.setText(nextId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextCustomerId(String currentId) {
        if (currentId != null && currentId.matches("C\\d+")) {
            int idNum = Integer.parseInt(currentId.substring(1));
            return "C" + String.format("%03d", ++idNum);
        }
        return "C001";
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
       String customer_id = txtCustomerId.getText();

        try {
            boolean isDeleted = CustomerRepo.deleteCustomers(customer_id);
            if((customer_id.isEmpty()) ){
                new Alert(Alert.AlertType.INFORMATION,"Enter valid customer id!").show();
            }
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted!").show();
                loadAllCustomers();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
      String customer_id = txtCustomerId.getText();
      String customer_name = txtCustomerName.getText();
      String address = txtCustomerAddress.getText();
      String contact_number = txtCustomerTel.getText();
      String email = txtEmailAddress.getText();

        Customer customer = new Customer(customer_id,customer_name,address,contact_number,email);
      try {
          if((customer_id.isEmpty()) || (customer_name.isEmpty()) || (address.isEmpty()) || (contact_number.isEmpty()) || (email.isEmpty())){
              new Alert(Alert.AlertType.INFORMATION,"Empty fields!").show();
          }
          if (isValid() && (isEmailValid()) && (isNameValid())){
                boolean isSaved = CustomerRepo.saveCustomers(customer);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer saved successfully!").show();
                    loadAllCustomers();
                    clearFields();
                }
          }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String customer_id = txtCustomerId.getText();
        String customer_name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();
        String contact_number = txtCustomerTel.getText();
        String email = txtEmailAddress.getText();

        Customer customer = new Customer(customer_id,customer_name,address,contact_number,email);
        try {
            if((customer_id.isEmpty()) || (customer_name.isEmpty()) || (address.isEmpty()) || (contact_number.isEmpty()) || (email.isEmpty())){
                new Alert(Alert.AlertType.INFORMATION,"Empty fields!").show();
            }
            if(isValid() && (isNameValid()) && (isEmailValid())) {
                boolean isUpdated = CustomerRepo.updateCustomers(customer);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer updated successfully!").show();
                    loadAllCustomers();
                    clearFields();
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String contact = txtCustomerTel.getText();

        Customer customer = CustomerRepo.searchCustomerByContact(contact);
        if(customer != null){
            txtCustomerId.setText(customer.getCustomer_id());
            txtCustomerName.setText(customer.getCustomer_name());
            txtCustomerAddress.setText(customer.getAddress());
            txtCustomerTel.setText(customer.getContact_number());
            txtEmailAddress.setText(customer.getEmail());
        }
        else {
            new Alert(Alert.AlertType.ERROR,"Customer is not found!").show();
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
    void btnUpdateOnMouseExited(MouseEvent event) {
        if (tooltip != null) {
            tooltip.hide();
        }
    }

    public void txtCustomerIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.CustomerID,txtCustomerId);
    }

    @FXML
    void txtEmailOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.EMAIL,txtEmailAddress);
    }

    @FXML
    void txtContactOnKeyReleased(KeyEvent event) {
       Regex.setTextColor(TextFields.Contact,txtCustomerTel);
    }

    public  void txtCustomerNameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(TextFields.Name, txtCustomerName);
    }
    public boolean isNameValid(){
       if(!Regex.setTextColor(TextFields.Name,txtCustomerName));
       return true;
    }

    public boolean isEmailValid(){
        if(!Regex.setTextColor(TextFields.EMAIL,txtEmailAddress));
        return true;
    }
    public boolean isValid(){
        if (!Regex.setTextColor(TextFields.CustomerID,txtCustomerId)) return false;

        if (!Regex.setTextColor(TextFields.Contact,txtCustomerTel)) return false;
        return true;
    }

    @FXML
    void txtCustomerNameOnAction(ActionEvent event) {
        if(isNameValid()){
            new Alert(Alert.AlertType.INFORMATION,"Should have to consist two names!" +"\n"+
                    "ex:Amali Perera").show();
        }
    }

    @FXML
    void txtEmailOnAction(ActionEvent event) {
       if (isEmailValid()){
           new Alert(Alert.AlertType.INFORMATION,"Invalid email!Try again").show();
       }
    }
}
