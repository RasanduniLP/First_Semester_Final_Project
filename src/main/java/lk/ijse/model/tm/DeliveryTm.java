package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryTm {
    private String deliveryId;
    private String orderId;
    private String vehicleId;
    private Date date;
    private String status;
}
