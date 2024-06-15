package lk.ijse.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vehicle {
    private String vehicleId;
    private String employeeId;
    private String vehicleNumber;
    private String model;
}
