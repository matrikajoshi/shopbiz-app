package self.edu.shopbiz.dto;

import lombok.Data;
import self.edu.shopbiz.model.Product;

import java.math.BigDecimal;

/**
 * Created by mpjoshi on 11/12/19.
 */

@Data
public class CartItemDTO {

    private Integer id;
    private Product product;
    private Integer quantity;
    private BigDecimal totalPrice;

}
