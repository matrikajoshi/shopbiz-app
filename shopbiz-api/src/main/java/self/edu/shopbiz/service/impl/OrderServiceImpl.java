package self.edu.shopbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.edu.shopbiz.model.CartItem;
import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.OrderItem;
import self.edu.shopbiz.model.ShoppingCart;
import self.edu.shopbiz.repository.OrderItemRepository;
import self.edu.shopbiz.repository.OrderRepository;
import self.edu.shopbiz.repository.ShoppingCartRepository;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.OrderService;
import self.edu.shopbiz.util.SecurityUtil;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mpjoshi on 10/10/19.
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    public void placeOrder(ShoppingCart cart){
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        order.setUser(loggedInUser.getUser());

        Set<CartItem> cartItems = cart.getCartItems();
        for(CartItem cartItem: cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setCartItem(cartItem);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order createOrder(List<OrderItem> orderItems) {
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(loggedInUser.getUser().getId()).get();
        Optional<BigDecimal> totalPriceOptional = shoppingCart.getCartItems().stream()
                .map(cartItem ->
                        cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                ).reduce(BigDecimal::add);
        shoppingCartRepository.delete(shoppingCart);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(loggedInUser.getUser());
        order.setTotalAmount(totalPriceOptional.get());
        order.setDelivered(false);
        Order save = orderRepository.save(order);
        Set<OrderItem> orderItemSet = orderItems.stream()
                .map(orderItem -> {
                    orderItem.setOrder(save);
                    return orderItem;
                })
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemRepository.saveAll(orderItemSet);
        order.setOrderItems(orderItemSet);

        return save;
    }
}
