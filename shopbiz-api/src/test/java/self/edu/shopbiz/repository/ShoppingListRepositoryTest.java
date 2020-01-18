package self.edu.shopbiz.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.model.ShoppingList;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by mpjoshi on 10/9/19.
 */


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ShoppingListRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ShoppingListRepository shoppingListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void saveShoppingListTest() {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setCustomer(userRepository.findById(1).get());
        shoppingList.setName("test");
        shoppingListRepository.save(shoppingList);
        logger.info("Shopping List -> {} ", shoppingListRepository.findAll());
        logger.info("Shopping List count -> {} ", shoppingListRepository.count());
    }

    @Test
    @Transactional
    public void findShoppingListByCustomerIdTest(){
        List<ShoppingList> shoppingLists = shoppingListRepository.findByCustomerId(1);
        assertTrue(shoppingLists.size() == 0);
    }

//    @Test
//    @Transactional
//    @DirtiesContext
//    public void testAddProductToShoppingList(){
//        ShoppingList shoppingList = new ShoppingList();
//        shoppingList.setUser(userRepository.findById(1).get());
//        shoppingList.setName("test");
//        shoppingListRepository.save(shoppingList);
//
//        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByCustomerIdAndName("test", 1);
//        assertTrue(shoppingListOptional.isPresent());
//        shoppingList = shoppingListOptional.get();
//        Product product = productRepository.findById(8L).get();
//        shoppingList.addProduct(product);
//        ShoppingList save = shoppingListRepository.save(shoppingList);
//        logger.info("new count: ", save.getProducts().size());
//        Set<Product> products = shoppingList.getProducts();
//        assertTrue(products.size() > 0);
//    }



}
