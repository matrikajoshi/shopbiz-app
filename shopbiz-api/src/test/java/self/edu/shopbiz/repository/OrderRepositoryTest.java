package self.edu.shopbiz.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import self.edu.shopbiz.model.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by mpjoshi on 10/10/19.
 */


@SpringBootTest
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
        Order saveOrder = new Order();
        saveOrder.setUser(user);
        saveOrder.setTotalAmount(BigDecimal.valueOf(0));
        ShoppingCart cart = getShoppingCart();
        cart.getCartItems().forEach((cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setCartItem(cartItem);
            saveOrder.setTotalAmount(saveOrder.getTotalAmount().add(cartItem.getTotalPrice()));
            saveOrder.getOrderItems().add(orderItem);
        }));
        orderItemRepository.saveAll(saveOrder.getOrderItems());
        orderRepository.save(saveOrder);
        Optional<Order> orderOptional = orderRepository.findByUser(user);
        assertTrue(orderOptional.isPresent());
        assertEquals(1, orderOptional.get().getOrderItems().size());
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
