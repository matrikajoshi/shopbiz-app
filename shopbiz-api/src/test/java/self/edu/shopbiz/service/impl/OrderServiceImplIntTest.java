package self.edu.shopbiz.service.impl;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import self.edu.shopbiz.model.*;
import self.edu.shopbiz.repository.CartItemRepository;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.repository.ShoppingCartRepository;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.OrderService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceImplIntTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void addLoggedInUser()
    {
        //Get the user by username from configured user details service
        UserDetails userDetails = this.userDetailsService.loadUserByUsername ("customer@test.com");

        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        this.testAddProductToShoppingCart();
    }


    @Test
    @DirtiesContext
    public void createOrder() {
        List<OrderItem> orderItems = getOrderItems();
        Order order = orderService.createOrder(orderItems);
        assertEquals(2, order.getOrderItems().size());

    }

    private List<OrderItem> getOrderItems() {
        Product product1 = productRepository.findById(1L).get();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(product1);
        orderItem1.setOrderedQuantities(5);
        //
        Product product2 = productRepository.findById(2L).get();
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setProduct(product2);
        orderItem2.setOrderedQuantities(5);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);
        return orderItems;
    }

    @Test
    @DirtiesContext
    public void updateOrderTest() {
        List<OrderItem> orderItems = getOrderItems();
        Order order = orderService.createOrder(orderItems);

        OrderItem orderItem1 = order.getOrderItems().stream().filter(orderItem -> orderItem.getProduct().getId().equals(1L)).findFirst().get();
        orderItem1.setOrderedQuantities(3);
        Order updateOrder = orderService.updateOrder(order);

        OrderItem orderItem1db = updateOrder.getOrderItems().stream().filter(orderItem -> orderItem.getProduct().getId().equals(1L)).findFirst().get();
        assertEquals(3, orderItem1db.getOrderedQuantities());

        Product product1 = productRepository.findById(1L).get();
        assertEquals(2, product1.getAvailableQuantities());
    }

    public void testAddProductToShoppingCart(){
        Optional<Product> productOptional = productRepository.findById(1l);
        Product product = productOptional.get();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        //cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userRepository.findById(2).get());
        shoppingCart.addCartItem(cartItem);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCartRepository.save(shoppingCart);
//        Optional<ShoppingCart> byCustomerId = shoppingCartRepository.findByUserId(1);
//        assertTrue(byCustomerId.isPresent());
    }
    
    
}
