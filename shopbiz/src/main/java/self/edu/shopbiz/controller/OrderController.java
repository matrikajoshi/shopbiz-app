package self.edu.shopbiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.OrderItem;
import self.edu.shopbiz.service.OrderService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * Created by mpjoshi on 11/7/19.
 */

@RestController()
@Api(tags = {"Order"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(path = "/order")
    @RolesAllowed("ORDER_CREATE")
    public Order createOrder(@RequestBody List<OrderItem> orderItems) {
        Order order = orderService.createOrder(orderItems);
        return order;
    }


}
