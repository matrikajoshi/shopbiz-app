package edu.self.shopbiz.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.self.shopbiz.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;

    private String userUserName;

    private String userEmail;

    private Set<OrderItemDTO> orderItems;

    @JsonFormat(shape= JsonFormat.Shape.STRING)
    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private LocalDateTime orderDate;
}
