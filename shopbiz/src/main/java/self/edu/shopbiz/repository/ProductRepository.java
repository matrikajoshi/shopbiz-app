package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.Product;

/**
 * Created by mpjoshi on 10/9/19.
 */

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {


}
