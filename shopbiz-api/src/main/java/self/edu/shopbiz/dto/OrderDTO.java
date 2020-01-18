package self.edu.shopbiz.dto;

import lombok.Data;
import self.edu.shopbiz.enums.OrderStatus;
import self.edu.shopbiz.model.OrderItem;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class OrderDTO {

    private Long id;

    private String customerEmail;

    private Set<OrderItem> orderItems;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;
}
