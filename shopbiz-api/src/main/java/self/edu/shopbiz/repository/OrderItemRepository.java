package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.OrderItem;

import java.util.List;

/**
 * Created by mpjoshi on 10/10/19.
 */

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
