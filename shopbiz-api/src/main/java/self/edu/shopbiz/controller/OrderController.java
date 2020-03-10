package self.edu.shopbiz.controller;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import self.edu.shopbiz.dto.CartItemDTO;
import self.edu.shopbiz.dto.OrderDTO;
import self.edu.shopbiz.dto.OrderItemDTO;
import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.OrderItem;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.OrderService;
import self.edu.shopbiz.util.AppConstants;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mpjoshi on 11/7/19.
 */

@RestController()
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper, RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping
    public List<OrderDTO> getOrders(Authentication authentication) {
        List<Order> orders;
        MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"))) {
            orders = orderService.findAll();
        } else {
            orders = orderService.getOrdersForUser(userDetails.getUser());
        }
        return orders.stream().map(order -> convertToDTO(order)).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public Order findOrderById(@PathVariable("id") Long orderId, Authentication authentication) {
        Order order = orderService.findById(orderId);
        MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();
        if (userDetails.getAuthorities().contains("ROLE_ORDER_CREATE")
                && !order.getUser().getId().equals(userDetails.getUser().getId())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access Denied for this order");
        }
        return order;
    }

    @PostMapping
    @RolesAllowed("ORDER_CREATE")
    public OrderDTO createOrder(@Valid @RequestBody List<OrderItemDTO> orderItemDTOs) {
        List<OrderItem> orderItems = getOrderItems(orderItemDTOs);
        Order order = orderService.createOrder(orderItems);
        OrderDTO orderDTO = convertToDTO(order);
        sendOrderConfirmation(orderDTO);
        return orderDTO;
    }

    @PutMapping(path="/{id}")
    public OrderDTO updateOrder(@PathVariable("id") Integer orderId, OrderDTO orderDTO, Authentication authentication) {
        Order order = convertToEntity(orderDTO);
        Order save = orderService.updateOrder(order);
        return convertToDTO(save);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("id") Long orderId) {
        orderService.cancelOrder(orderId);
    }

    private OrderItem convertToEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);
        return orderItem;
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        return order;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        return orderDTO;
    }

    private List<OrderItem> getOrderItems(List<OrderItemDTO> orderItemDTOS) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItemDTOS.forEach((orderItemDTO -> {
            OrderItem orderItem = convertToEntity(orderItemDTO);
            orderItems.add(orderItem);
        }));
        return orderItems;
    }

    private OrderItem convertCartToOrderItem(CartItemDTO cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setOrderedQuantities(cartItem.getQuantity());
        orderItem.setExtPrice(cartItem.getTotalPrice());
        return orderItem;
    }

    private void sendOrderConfirmation(OrderDTO orderDTO) {
        System.out.println("Sending message...");
        String message = new Gson().toJson(orderDTO);
        rabbitTemplate.convertAndSend(AppConstants.TOPIC_EXCHANGE_NAME,
                AppConstants.SHOPBIZ_MESSAGE_QUEUE,
                message);
    }
}
