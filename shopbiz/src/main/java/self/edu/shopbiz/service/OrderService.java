package self.edu.shopbiz.service;

import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.OrderItem;

import java.util.List;

/**
 * Created by mpjoshi on 10/10/19.
 */

public interface OrderService {

    Order createOrder(List<OrderItem> orderItems);

}
