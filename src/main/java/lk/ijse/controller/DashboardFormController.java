package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DBConnection;
import lk.ijse.repository.*;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DashboardFormController {


    public AnchorPane rootNode;

    @FXML
    public   AnchorPane centerNode;

    @FXML
    private Label lblExpenses;

    @FXML
    private Label lblIncome;

    @FXML
    private Label lblProfit;

    @FXML
    private PieChart pieChart;

    @FXML
    public BarChart barChart;

    @FXML
    private Label lblCustomerCount;

    @FXML
    private Label lblEmployeeCount;

    @FXML
    private Label lblOrderCount;

    @FXML
    private Label lblSupplierCount;

    private int customerCount;

    private int employeeCount;

    private int supplierCount;

    private int orderCount;

    public void initialize() throws SQLException {
        setPieChart();
        setBarChart();
        setExpenses();
        setIncome();
        setProfit();
        getCount();
    }

    private void getCount(){
        try {
            customerCount = CustomerRepo.getCustomerCount();
            employeeCount = EmployeeRepo.getEmployeeCount();
            supplierCount = SupplierRepo.getSupplierCount();
            orderCount = OrderRepo.getOrderCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage());
        }
        setCount(customerCount,employeeCount,supplierCount,orderCount);

    }
    private void setCount(int customerCount,int employeeCount,int supplierCount,int orderCount) {
        lblCustomerCount.setText(String.valueOf(customerCount));
        lblEmployeeCount.setText(String.valueOf(employeeCount));
        lblSupplierCount.setText(String.valueOf(supplierCount));
        lblOrderCount.setText(String.valueOf(orderCount));
    }
    private void setExpenses(){
        String expenses = null;
        try {
            expenses = MaterialDetailRepo.totalExpenses();
            lblExpenses.setText(expenses);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void  setIncome(){
        String income = null;
        try {
            income = OrderRepo.totalIncome();
            lblIncome.setText(income);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setProfit() {
        try {
            String expensesStr = MaterialDetailRepo.totalExpenses();
            String incomeStr = OrderRepo.totalIncome();

            if (expensesStr != null && incomeStr != null) {
                double expenses = Double.parseDouble(expensesStr);
                double income = Double.parseDouble(incomeStr);

                String profit = String.valueOf(income - expenses);
                lblProfit.setText(profit);
            } else {
                // Handle the case where either expenses or income is null
                lblProfit.setText("Error: Expenses or Income is null");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails
            lblProfit.setText("Error: Failed to parse expenses or income");
        }
    }

    private void setPieChart() throws SQLException {
            String expensesStr = MaterialDetailRepo.totalExpenses();
            String incomeStr = OrderRepo.totalIncome();

            if (expensesStr != null && incomeStr != null) {
                double expenses = Double.parseDouble(expensesStr.trim());
                double income = Double.parseDouble(incomeStr.trim());
                double profit = income - expenses;

                ObservableList<PieChart.Data> pieChartData =
                        FXCollections.observableArrayList(
                                new PieChart.Data("P", profit),
                                new PieChart.Data("E", expenses),
                                new PieChart.Data("I", income));

                pieChart.getData().addAll(pieChartData);
            } else {
                // Handle case when expenses or income is null
                // For example, show an error message or handle it according to your application's logic
            }
        }


    private void setBarChart() throws SQLException {
        Map<String, Double> monthlyIncomes = OrderRepo.getTotalMonthlyIncome();
        Map<String, Double> monthlyExpenses = MaterialDetailRepo.getTotalMonthlyExpenses();
        Map<String, Double> monthlyProfit = getProfit(monthlyIncomes,monthlyExpenses);

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        for (Map.Entry<String, Double> entry : monthlyIncomes.entrySet()) {
            incomeSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        XYChart.Series<String, Number> outcomeSeries = new XYChart.Series<>();
        outcomeSeries.setName("Expenses");
        for (Map.Entry<String, Double> entry : monthlyExpenses.entrySet()) {
            outcomeSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        profitSeries.setName("Profit");
        for (Map.Entry<String, Double> entry : monthlyProfit.entrySet()) {
            profitSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().addAll(incomeSeries, outcomeSeries,profitSeries);
    }

    private Map<String, Double> getProfit(Map<String, Double> incomes, Map<String, Double> expenses) {
        Map<String, Double> monthlyProfit = new HashMap<>();
        for (Map.Entry<String, Double> incomeEntry : incomes.entrySet()) {
            String month = incomeEntry.getKey();
            double income = incomeEntry.getValue();
            double expense = expenses.getOrDefault(month, 0.0); // If there are no expenses for that month, assume 0
            double profit = income - expense;
            monthlyProfit.put(month, profit);
        }
        return monthlyProfit;
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        AnchorPane customerPane = FXMLLoader.load(this.getClass().getResource("/view/customer_form.fxml"));
        centerNode.getChildren().clear();
        centerNode.getChildren().add(customerPane);

    }

    @FXML
    void btnDeliveryOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/deliveryDetail_form.fxml"));

        try {
            AnchorPane deliveryDetailPane = loader.load();
            DeliveryFormController controller = loader.getController();
            controller.setCenterNode(centerNode);
            centerNode.getChildren().clear();
            centerNode.getChildren().add(deliveryDetailPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employeeDetail_form.fxml"));

        try {
            AnchorPane employeeFormPane = loader.load();
            EmployeeFormController controller = loader.getController();
            controller.setCenterNode(centerNode);
            centerNode.getChildren().clear();
            centerNode.getChildren().add(employeeFormPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Login Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) throws IOException {
        AnchorPane placeOrderPane = FXMLLoader.load(this.getClass().getResource("/view/placeOrder_form.fxml"));
        centerNode.getChildren().clear();
        centerNode.getChildren().add(placeOrderPane);

    }

    @FXML
    void btnProductsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/product_form.fxml"));

        try {
            AnchorPane productPane = loader.load();
            ProductFormController controller = loader.getController();
            controller.setCenterNode(centerNode);
            centerNode.getChildren().clear();
            centerNode.getChildren().add(productPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnMaterialOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/rawMaterial_form.fxml"));

        try {
            AnchorPane materialPane = loader.load();
           RawMaterialFormController controller = loader.getController();
            controller.setCenterNode(centerNode);
            centerNode.getChildren().clear();
            centerNode.getChildren().add(materialPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) throws IOException {
        AnchorPane supplierPane = FXMLLoader.load(this.getClass().getResource("/view/supplierDetail_form.fxml"));
        centerNode.getChildren().clear();
        centerNode.getChildren().add(supplierPane);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
      navigateToTheDashboard();
    }

    @FXML
    void btnReportOnAction(ActionEvent event) throws IOException {
        AnchorPane reportPane = FXMLLoader.load(this.getClass().getResource("/view/report_form.fxml"));
        centerNode.getChildren().clear();
        centerNode.getChildren().add(reportPane);
    }

    public  void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage =(Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }


}
