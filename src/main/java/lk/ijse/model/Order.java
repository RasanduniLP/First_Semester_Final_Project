package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    private String orderId;
    private String customerId;
    private Date date;
    private double payment;
}
