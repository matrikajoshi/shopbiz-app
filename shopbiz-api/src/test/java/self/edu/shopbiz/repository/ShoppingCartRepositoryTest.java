package self.edu.shopbiz.repository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.model.CartItem;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.ShoppingCart;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by mpjoshi on 10/10/19.
 */


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ShoppingCartRepositoryTest {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DirtiesContext
    public void testAddProductToShoppingCart(){
        Optional<Product> productOptional = productRepository.findById(1l);
        Product product = productOptional.get();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userRepository.findById(1).get());
        shoppingCart.addCartItem(cartItem);
        shoppingCartRepository.save(shoppingCart);
        Optional<ShoppingCart> byCustomerId = shoppingCartRepository.findByUserId(1);
        assertTrue(byCustomerId.isPresent());
    }

    @Test
    public void testFindCartByCustomerId(){
        Optional<ShoppingCart> byCustomerId = shoppingCartRepository.findByUserId(1);
        assertFalse(byCustomerId.isPresent());
    }


}
