package self.edu.shopbiz.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.model.Category;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * Created by mpjoshi on 10/31/19.
 */
//

//@SpringBootTest(classes = ShopbizApplication.class)
@RunWith(SpringRunner.class)
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
