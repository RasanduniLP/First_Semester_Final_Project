package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import lk.ijse.db.DBConnection;
import lk.ijse.repository.MaterialDetailRepo;
import lk.ijse.repository.OrderRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReportFormController {

    @FXML
    private BarChart barChart1;

    @FXML
    private BarChart barChart2;

    @FXML
    private AnchorPane reportPane;

    public void initialize() throws SQLException {
        setBarChart1();
        setBarChart2();
    }
    private void setBarChart1() throws SQLException {
        Map<String,Double> monthlyCount = OrderRepo.getOrderCountMonthly();

        XYChart.Series<String, Number> countSeries = new XYChart.Series<>();
        countSeries.setName("Order Count");
        for (Map.Entry<String, Double> entry : monthlyCount.entrySet()) {
            countSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart1.getData().addAll(countSeries);
    }

    private void setBarChart2() throws SQLException {
        Map<String,Double> monthlyAmount = MaterialDetailRepo.getTotalMonthlyExpenses();

        XYChart.Series<String, Number> amountSeries = new XYChart.Series<>();
        amountSeries.setName("Monthly Material Expense");
        for (Map.Entry<String, Double> entry : monthlyAmount.entrySet()) {
            amountSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart2.getData().addAll(amountSeries);
    }
    @FXML
    void btnCustomerOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/customer_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/employee_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void btnItemOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/item_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void btnMaterialOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/material_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/order_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/supplier_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

}
