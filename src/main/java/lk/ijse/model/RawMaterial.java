package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RawMaterial {
    private String materialId;
    private String materialName;
    private double unitPrice;
}
