package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.Category;

/**
 * Created by mpjoshi on 10/31/19.
 */

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Integer>{
}
