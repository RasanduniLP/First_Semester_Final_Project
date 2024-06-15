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
public class MaterialDetail {
    private String materialOrderId;
    private String materialId;
    private String supplierId;
    private int qty;
    private double unitPrice;
    private double payment;
    private Date date;
}
