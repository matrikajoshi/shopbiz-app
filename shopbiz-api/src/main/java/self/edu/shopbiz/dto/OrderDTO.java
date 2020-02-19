package self.edu.shopbiz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import self.edu.shopbiz.enums.OrderStatus;
import self.edu.shopbiz.model.OrderItem;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class OrderDTO {

    private Long id;

    private String userUserName;

    private Set<OrderItemDTO> orderItems;

    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private BigDecimal totalAmount;

    private OrderStatus orderStatus;
}
