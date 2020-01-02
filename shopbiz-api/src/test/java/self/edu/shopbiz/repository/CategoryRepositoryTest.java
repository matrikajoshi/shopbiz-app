package self.edu.shopbiz.repository;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import self.edu.shopbiz.model.Category;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by mpjoshi on 10/31/19.
 */
//

//@SpringBootTest(classes = ShopbizApplication.class)

@DataJpaTest
public class CategoryRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testGetCategoryById(){
        Optional<Category> categoryOptional = categoryRepository.findById(1);
        assertTrue(categoryOptional.isPresent());
    }


}
