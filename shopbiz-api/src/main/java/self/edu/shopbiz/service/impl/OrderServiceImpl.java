package self.edu.shopbiz.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import self.edu.shopbiz.enums.OrderStatus;
import self.edu.shopbiz.exceptionUtil.InventoryNotAvailableException;
import self.edu.shopbiz.model.*;
import self.edu.shopbiz.repository.OrderItemRepository;
import self.edu.shopbiz.repository.OrderRepository;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.repository.ShoppingCartRepository;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.OrderService;
import self.edu.shopbiz.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mpjoshi on 10/10/19.
 */

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderIemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, OrderItemRepository orderIemRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.orderIemRepository = orderIemRepository;
    }


    @Override
    public Order createOrder(List<OrderItem> orderItems) {
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        User user = loggedInUser.getUser();
        Order save = createOrder(orderItems, user);
        return save;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    Order createOrder(List<OrderItem> orderItems, User user) {
        Order order = new Order();
        final BigDecimal[] totalPrice = {BigDecimal.ZERO};
        // get product list with map from order items, get total price from products
        Map<Long, Product> productsById = getProductsMapById(orderItems);
        orderItems.forEach((orderItem -> {
            Product product = productsById.get(orderItem.getProduct().getId());
            if (orderItem.getOrderedQuantities() > product.getAvailableQuantities()) {
                throw new InventoryNotAvailableException("Inventory not sufficient for id: " + product.getId());
            } else {
                updateProductInventory(product, orderItem.getOrderedQuantities());
                BigDecimal itemCost = product.getPrice().multiply(new BigDecimal(orderItem.getOrderedQuantities()));
                totalPrice[0] = totalPrice[0].add(itemCost);
                order.getOrderItems().add(orderItem);
                orderItem.setOrder(order);
            }
        }));

        order.setTotalAmount(totalPrice[0]);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setUser(user);
        Order save = orderRepository.save(order);
        productRepository.saveAll(productsById.values());
        deleteShoppingCartForOrder(user);
        return save;
    }

    private synchronized void updateProductInventory(Product product, Integer orderQuantity) {
        product.setAvailableQuantities(product.getAvailableQuantities() - orderQuantity);
    }

    // delete shopping cart after creating order
    private void deleteShoppingCartForOrder(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).get();
        shoppingCartRepository.delete(shoppingCart);
    }

    @Override
    public List<Order> getOrdersForUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order id " + orderId + " doesn't exist"));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Order updateOrder(Order order) {
        List<OrderItem> orderItems = new ArrayList<>(order.getOrderItems());
        List<OrderItem> orderItemsDB = orderIemRepository.findByOrder(order);
        // get product list with map from order items and get total price from products
        Map<Long, Product> productsById = getProductsMapById(orderItems);
        orderItems.forEach(orderItem -> {
            OrderItem orderItemDB = orderItemsDB.stream().filter(itemDB -> itemDB.getId().equals(orderItem.getId())).findFirst().get();
            int diff = orderItem.getOrderedQuantities() - orderItemDB.getOrderedQuantities();
            Product product = productsById.get(orderItem.getProduct().getId());
            synchronized (product) {
                if (diff > 0) {
                    if (diff > product.getAvailableQuantities()) {
                        throw new InventoryNotAvailableException("Inventory not sufficient for id: " + orderItem.getProduct().getId());
                    }
                    product.setAvailableQuantities(product.getAvailableQuantities() - diff);
                } else {
                    product.setAvailableQuantities(product.getAvailableQuantities() + Math.abs(diff));
                }
            }
            orderItemDB.setOrderedQuantities(orderItem.getOrderedQuantities());
        });
        Order save = orderRepository.save(order);
        productRepository.saveAll(productsById.values());
        return save;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        Map<Long, Product> productsById = getProductsMapById(new ArrayList<>(order.getOrderItems()));
        order.getOrderItems().forEach(orderItem -> {
            Product product = productsById.get(orderItem.getProduct().getId());
            product.setAvailableQuantities(product.getAvailableQuantities() + orderItem.getOrderedQuantities());
        });
        productRepository.saveAll(productsById.values());
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    private Map<Long, Product> getProductsMapById(List<OrderItem> orderItems) {
        // get list of inventories from db
        List<Long> productIds = new ArrayList<>();
        orderItems.forEach(orderItem -> productIds.add(orderItem.getProduct().getId()));
        List<Product> inventories = productRepository.findAllById(productIds);
        Map<Long, Product> productsById = new HashMap<>();
        inventories.forEach(product -> productsById.put(product.getId(), product));
        return productsById;
    }


}
