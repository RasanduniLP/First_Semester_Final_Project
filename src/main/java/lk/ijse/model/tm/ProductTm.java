package lk.ijse.model.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductTm {
    private String productId;
    private String productName;
    private double unitPrice;
    private int qtyOnHand;
}
