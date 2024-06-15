package lk.ijse.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private String employeeId;
    private String employeeName;
    private String NIC;
    private String address;
    private String contact;
    private double salary;
    private String section;



}
