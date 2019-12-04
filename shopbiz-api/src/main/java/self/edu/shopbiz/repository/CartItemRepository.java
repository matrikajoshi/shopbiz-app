package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.CartItem;

/**
 * Created by mpjoshi on 10/10/19.
 */

@Repository("cartItemRepository")
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
