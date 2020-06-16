package self.edu.shopbiz.service;

import self.edu.shopbiz.model.CartItem;
import self.edu.shopbiz.model.ShoppingCart;
import self.edu.shopbiz.security.MyUserPrincipal;

import java.util.Optional;

/**
 * Created by mpjoshi on 10/12/19.
 */


public interface ShoppingCartService {


    Optional<ShoppingCart> getShoppingCartByCustomerId(Integer customerId);

    ShoppingCart removeCartItem(MyUserPrincipal userPrincipal, Integer cartItemId);

    ShoppingCart addCartItemToShoppingCart(MyUserPrincipal myUserPrincipal, CartItem cartItem, ShoppingCart shoppingCart);

    ShoppingCart updateCartItem(Integer cartItemId, Integer itemCount);
}
