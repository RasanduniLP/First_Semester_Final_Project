package lk.ijse.model.tm;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VehicleTm {
    private String vehicleId;
    private String employeeId;
    private String vehicleNumber;
    private String model;
}