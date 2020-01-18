package self.edu.shopbiz.Controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.OrderItem;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.repository.OrderItemRepository;
import self.edu.shopbiz.repository.OrderRepository;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.service.OrderService;
import self.edu.shopbiz.util.JsonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

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
@AutoConfigureTestDatabase
public class OrderIntTest {

    private final String apiUrl = "/orders";

    @Autowired
    private MockMvc mvc;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void resetDb() {
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateOrder() throws Exception {
        List<OrderItem> orderItems = getOrderItems();
        mvc.perform(post(apiUrl).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(orderItems)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderItems", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.orderItems[0].product.name", is("Product101")));

        Product product1 = productRepository.findById(101L).get();
        assertEquals(5, product1.getAvailableQuantities());
    }

    @Test
    public void givenOrderExists_whenGetOrders_thenStatus200() throws Exception {
        createOrder();

        mvc.perform(get(apiUrl).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderItems", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.orderItems[0].product.name", is("Product101")));
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

    private List<OrderItem> getOrderItems() {
        Product product1 = productRepository.findById(101L).get();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(product1);
        orderItem1.setOrderedQuantities(5);

        List<OrderItem> orderItems = Arrays.asList(orderItem1);
        return orderItems;
    }

//    private void createInventory(String name) {
//        inventoryRepository.saveAndFlush(getInventory(name));
//    }


    private Order createOrder() {
        List<OrderItem> orderItems = getOrderItems();
        Order order = orderService.createOrder(orderItems);
        return order;
    }
}
