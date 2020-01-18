package self.edu.shopbiz.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import self.edu.shopbiz.model.Product;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDTO {

    private Long Id;

    private Product product;

    private Integer orderedQuantities;

    private BigDecimal extPrice;
}
