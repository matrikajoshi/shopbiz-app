package self.edu.shopbiz.controller;

import org.modelmapper.ModelMapper;
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

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpjoshi on 11/7/19.
 */

@RestController()
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    public List<Order> getOrders(Authentication authentication) {
        List<Order> orders;
        MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"))) {
            orders = orderService.findAll();
        } else {
            orders = orderService.getOrdersForUser(userDetails.getUser());
        }
        return orders;
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
    public OrderDTO createOrder(@RequestBody List<OrderItemDTO> orderItemDTOS) {
        List<OrderItem> orderItems = getOrderItems(orderItemDTOS);
        Order order = orderService.createOrder(orderItems);
        return convertToDTO(order);
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
}
