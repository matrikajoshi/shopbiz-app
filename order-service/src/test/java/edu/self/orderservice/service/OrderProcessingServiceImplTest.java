package edu.self.orderservice.service;

import edu.self.shopbiz.dtos.OrderDTO;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderProcessingServiceImplTest {

    @Autowired
    OrderProcessingServiceImpl orderProcessingService;

    @Test
    @Ignore
    void sendEmail() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalAmount(BigDecimal.valueOf(9.99));
        System.out.println(new DecimalFormat("#0.##").format(BigDecimal.valueOf(9.99)));
        orderDTO.setUserEmail("");
        orderDTO.setUserUserName("TestCustomer");
       // orderProcessingService.sendEmail(orderDTO);
    }
}