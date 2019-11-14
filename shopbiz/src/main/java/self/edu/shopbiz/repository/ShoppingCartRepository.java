package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.ShoppingCart;

import java.util.Optional;

/**
 * Created by mpjoshi on 10/10/19.
 */

@Repository("shoppingCartRepository")
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>{

    Optional<ShoppingCart> findByUserId(Integer customerId);

}
