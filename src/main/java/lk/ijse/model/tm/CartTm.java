package lk.ijse.model.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartTm {
    private String productId;
    private String productName;
    private double unitPrice;
    private int qty;
    private double total;
    private Button btnRemove;

}
