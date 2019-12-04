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
import self.edu.shopbiz.model.User;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mpjoshi on 10/9/19.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopbizApplication.class)
public class UserRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository repository;

    @Test
    public void findByIdIfPresent(){
        Optional<User> customerOptional = repository.findById(1);
        assertTrue(customerOptional.isPresent());
    }

    @Test
    public void findById_CustomerNotPresent() {
        Optional<User> customerOptional = repository.findById(100);
        assertFalse(customerOptional.isPresent());
    }


}
