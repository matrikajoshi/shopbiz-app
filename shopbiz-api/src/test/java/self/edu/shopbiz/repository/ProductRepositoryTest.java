package self.edu.shopbiz.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.model.Product;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * Created by mpjoshi on 10/9/19.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopbizApplication.class)
public class ProductRepositoryTest {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductRepository repository;

    @Test
    public void findByIdIfPresent(){
        Optional<Product> productOptional = repository.findById(1L);
        assertTrue(productOptional.isPresent());
        logger.info("Product name from test -> {}", productOptional.get().getName());
    }
}
