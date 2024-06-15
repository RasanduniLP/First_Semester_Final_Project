package lk.ijse.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetail {
    private String orderId;
    private String productId;
    private int qty;
    private double unitPrice;
}
