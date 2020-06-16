package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.Order;
import self.edu.shopbiz.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/10/19.
 */


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByUser(User user);
}
