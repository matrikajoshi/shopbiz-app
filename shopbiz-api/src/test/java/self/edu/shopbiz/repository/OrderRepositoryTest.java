package self.edu.shopbiz.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import self.edu.shopbiz.model.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by mpjoshi on 10/10/19.
 */


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    UserRepository userRepository;

    @DirtiesContext
    @Test
    @Transactional
    public void testAddOrder(){
        User user = userRepository.findById(1).get();
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(BigDecimal.valueOf(10));
        ShoppingCart cart = getShoppingCart();
        cart.getCartItems().forEach((cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrderedQuantities(cartItem.getQuantity());
            orderItem.setOrder(order);
            order.setTotalAmount(order.getTotalAmount().add(cartItem.getTotalPrice()));
            order.getOrderItems().add(orderItem);
        }));
        //orderItemRepository.saveAll(order.getOrderItems());
        orderRepository.save(order);
        List<Order> orders = orderRepository.findByUser(user);
        assertTrue(orders.size() > 0);
        assertEquals(1, orders.get(0).getOrderItems().size());
    }

    ShoppingCart getShoppingCart(){
        Optional<Product> productOptional = productRepository.findById(1l);
        Product product = productOptional.get();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userRepository.findById(1).get());
        shoppingCart.addCartItem(cartItem);
        return shoppingCartRepository.save(shoppingCart);
    }
}
