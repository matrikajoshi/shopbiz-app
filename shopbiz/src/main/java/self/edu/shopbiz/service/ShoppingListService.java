package self.edu.shopbiz.service;



import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.ShoppingList;
import self.edu.shopbiz.model.User;

import java.util.List;

/**
 * Created by mpjoshi on 9/29/19.
 */


public interface ShoppingListService {

    ShoppingList getShoppingList(Integer id);

    ShoppingList addProductByUserId(User userId, Product product);

    ShoppingList removeProductByUserId(Integer userId, Product product);

    List<ShoppingList> getAllShoppingList(Integer userId);

    ShoppingList  getShoppingListByUserId(Integer userId);

    ShoppingList addProductByShoppingListId(Integer shoppingListId, Product product);

}
