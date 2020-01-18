package self.edu.shopbiz.service;

import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.OrderItem;
import self.edu.shopbiz.model.User;

import java.util.List;

/**
 * Created by mpjoshi on 10/10/19.
 */

public interface OrderService {

    Order createOrder(List<OrderItem> orderItems);

    List<Order> getOrdersForUser(User user);

    List<Order> findAll();

    Order findById(Long orderId);

    Order updateOrder(Order order);

    void cancelOrder(Long orderId);
}
