package lk.ijse.util;

import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    public static boolean isTextFieldValid(TextFields textField, String text){
        String filled = "";

        switch (textField){
            case UserName:
                filled = "^[0-9A-Za-z]{6,16}$";
                break;
            case Password:
                filled = "^(?=.*?[0-9])(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[^0-9A-Za-z]).{8,32}$";
                break;
            case UserID:
                filled = "U\\d{3}$";
                break;
            case OrderId:
                filled = "O\\d{3}$";
                break;
            case ProductID:
                filled = "P\\d{3}$";
                break;
            case EmployeeID:
                filled = "E\\d{3}$";
                break;
            case SupplierID:
                filled = "S\\d{3}$";
                break;
            case CustomerID:
                filled = "C\\d{3}$";
                break;
            case MaterialID:
                filled = "R\\d{3}$";
                break;
            case VehicleID:
                filled = "V\\d{3}$";
                break;
            case DeliveryID:
                filled = "D\\d{3}$";
                break;
            case AttendanceID:
                filled = "A\\d{3}$";
                break;
            case MaterialOrderID:
                filled = "M\\d{3}$";
                break;
            case EMAIL:
                filled = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
                break;
            case DOUBLE:
                filled = "^([0-9]){1,}[.]([0-9]){1,}$";
                break;
            case Contact:
                filled = "^([+]94{1,3}|[0])([1-9]{2})([0-9]){7}$";
                break;
            case Name:
                filled = "^[A-Za-z]+([-']?[A-Za-z]+)*\\s[A-Za-z]+([-']?[A-Za-z]+)*$";
                break;

        }
        Pattern pattern = Pattern.compile(filled);

        if (text != null){
            if (text.trim().isEmpty()){
                return false;
            }
        }else {
            return false;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()){
            return true;
        }
        return false;
    }

    public static boolean setTextColor(TextFields location, TextField textField) {
        if (Regex.isTextFieldValid(location, textField.getText())) {
            textField.setStyle("-fx-text-fill: green;");
            return true;
        } else {
            textField.setStyle("-fx-text-fill: red;");
            return false;
        }
    }
}
