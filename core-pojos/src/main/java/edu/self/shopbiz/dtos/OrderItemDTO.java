package edu.self.shopbiz.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private Long Id;

    private ProductDTO product;

    private Integer orderedQuantities;

    private BigDecimal extPrice;
}
