package self.edu.shopbiz.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.model.Product;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by mpjoshi on 10/9/19.
 */



@ExtendWith(SpringExtension.class)
@DataJpaTest
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
