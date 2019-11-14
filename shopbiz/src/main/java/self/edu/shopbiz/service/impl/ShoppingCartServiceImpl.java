package self.edu.shopbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.edu.shopbiz.model.CartItem;
import self.edu.shopbiz.model.ShoppingCart;
import self.edu.shopbiz.repository.CartItemRepository;
import self.edu.shopbiz.repository.ShoppingCartRepository;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.ShoppingCartService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/12/19.
 */

@Service
public class ShoppingCartServiceImpl  implements ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public Optional<ShoppingCart> getShoppingCartByCustomerId(Integer customerId) {
        return shoppingCartRepository.findByUserId(customerId);
    }

    @Override
    @Transactional
    public CartItem updateCartItem(Integer cartItemId, Integer itemCount) {
        CartItem cartItem = cartItemRepository.findById(Long.valueOf(cartItemId))
                .orElseThrow(() -> new EntityNotFoundException("No cartItem with id: " + cartItemId));
        cartItem.setQuantity(itemCount);
        CartItem save = cartItemRepository.save(cartItem);
        return save;
    }

    @Override
    @Transactional
    public ShoppingCart removeCartItem(MyUserPrincipal myUserPrincipal, Integer cartItemId) {
        Optional<ShoppingCart> byUserId = shoppingCartRepository.findByUserId(myUserPrincipal.getUser().getId());
        if(byUserId.isPresent()){
            ShoppingCart shoppingCart = byUserId.get();
            shoppingCart.removeCartItem(cartItemRepository.getOne(Long.valueOf(cartItemId)));
            shoppingCart = shoppingCartRepository.save(shoppingCart);
            return shoppingCart;
        }

        return new ShoppingCart();
    }

    @Override
    @Transactional
    public ShoppingCart addCartItemToShoppingCart(MyUserPrincipal myUserPrincipal, CartItem cartItem, ShoppingCart shoppingCart) {
        //check if product is already in shopping cart
        CartItem newCartItem;
        Optional<CartItem> cartItemDB = shoppingCart.getCartItems().stream()
                .filter(item ->
                    item.getProduct() != null &&
                        item.getProduct().getId().equals(cartItem.getProduct().getId())
                )
                .findFirst();
        if(cartItemDB.isPresent()){
            newCartItem = cartItemDB.get();
            newCartItem.setQuantity(cartItemDB.get().getQuantity() + cartItem.getQuantity());
        }else{
            newCartItem = cartItem;

        }
        CartItem save = cartItemRepository.save(newCartItem);
        shoppingCart.addCartItem(save);
        shoppingCart.setUser(myUserPrincipal.getUser());
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }



}
