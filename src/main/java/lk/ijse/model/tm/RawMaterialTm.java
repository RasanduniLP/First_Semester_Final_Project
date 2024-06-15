package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RawMaterialTm {
    private String materialId;
    private String materialName;
    private double unitPrice;
}
