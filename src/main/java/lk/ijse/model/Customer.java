package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
    private String customer_id;
    private String customer_name;
    private String address;
    private String contact_number;
    private String email;

}