package self.edu.shopbiz.Controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.dto.OrderItemDTO;
import self.edu.shopbiz.model.*;
import self.edu.shopbiz.repository.*;
import self.edu.shopbiz.service.OrderService;
import self.edu.shopbiz.util.JsonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShopbizApplication.class)
@AutoConfigureMockMvc
public class OrderControllerIT {

    private final String apiUrl = "/orders";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mvc;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
   ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;


    @BeforeEach
    public void setUserAndDB() {
        //Get the user by username from configured user details service
        UserDetails userDetails = this.userDetailsService.loadUserByUsername ("customer@test.com");
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void whenValidInput_thenCreateOrder() throws Exception {
        List<OrderItemDTO> orderItems = getOrderItems();
        mvc.perform(post(apiUrl).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(orderItems)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderItems", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.orderItems[0].product.name", is("Coffee Cup")));

        Product product1 = productRepository.findById(1L).get();
        assertEquals(3, product1.getAvailableQuantities());
    }

    @Test
    @WithMockUser
    public void givenOrderExists_whenGetOrders_thenStatus200() throws Exception {
        createOrder();

        mvc.perform(get(apiUrl).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderItems", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.orderItems[0].product.name", is("Coffee Cup")));
    }

    @Test
    public void givenOrderExists_whenUpdateOrder_thenStatus200() throws Exception {
        Order order = createOrder();
        entityManager.detach(order);
        OrderItem orderItem = order.getOrderItems().iterator().next();
        orderItem.setOrderedQuantities(3);
        mvc.perform(put(apiUrl+"/1").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(order)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderItems", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.orderItems[0].orderedQuantities", is(3)));
    }

    private List<OrderItemDTO> getOrderItems() {
        Product product1 = productRepository.findById(1L).get();
        addProductToShoppingCart(product1);
        OrderItemDTO orderItem1 = new OrderItemDTO();
        orderItem1.setProduct(product1);
        orderItem1.setOrderedQuantities(2);

        List<OrderItemDTO> orderItems = Arrays.asList(orderItem1);
        return orderItems;
    }

    private Order createOrder() {
        List<OrderItemDTO> orderItemDTOs = getOrderItems();
        List<OrderItem> orderItems = orderItemDTOs.stream().map((orderItemDTO -> modelMapper.map(orderItemDTO, OrderItem.class))).collect(Collectors.toList());
        Order order = orderService.createOrder(orderItems);
        return order;
    }


    public void addProductToShoppingCart(Product product){
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userRepository.findById(2).get());
        shoppingCart.addCartItem(cartItem);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCartRepository.save(shoppingCart);
    }




}
