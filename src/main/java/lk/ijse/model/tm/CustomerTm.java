package lk.ijse.model.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerTm {
    private String customer_id;
    private String customer_name;
    private String address;
    private String contact_number;
    private String email;

}
