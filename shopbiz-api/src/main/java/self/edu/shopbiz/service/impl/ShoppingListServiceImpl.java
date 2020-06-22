package self.edu.shopbiz.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.ShoppingList;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.ShoppingListRepository;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.service.ShoppingListService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mpjoshi on 9/29/19.
 */

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListServiceImpl.class);

    private final UserRepository userRepository;
    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListServiceImpl(UserRepository customerRepository, ShoppingListRepository shoppingListRepository) {
        this.userRepository = customerRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public ShoppingList getShoppingListByUserId(Integer userId) {
        List<ShoppingList> shoppingList = shoppingListRepository.findByCustomerId(userId);
        return shoppingList.size() > 0 ? shoppingList.get(0) : null;
    }

    @Override
    public List<ShoppingList> getAllShoppingList(Integer customerId) {
        return shoppingListRepository.findByCustomerId(customerId);
    }

    @Override
    public ShoppingList addProductByUserId(User user, Product product) {
        List<ShoppingList> byCustomerId = shoppingListRepository.findByCustomerId(user.getId());
        ShoppingList shoppingList;
        if (byCustomerId.isEmpty()) {
            shoppingList = new ShoppingList();
        } else {
            // take 1st one
            shoppingList = byCustomerId.get(0);
        }
        shoppingList.getProducts().add(product);
        shoppingList.setCustomer(user);
        ShoppingList save = shoppingListRepository.save(shoppingList);
        return save;
    }

    @Override
    public ShoppingList removeProductByUserId(Integer userId, Product product) {
        // take 1st one
        ShoppingList shoppingList = shoppingListRepository.findByCustomerId(userId).get(0);
        shoppingList.getProducts().remove(product);
        ShoppingList save = shoppingListRepository.save(shoppingList);
        return save;
    }

    @Override
    public ShoppingList getShoppingList(Integer id) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        return shoppingListOptional.get();
    }

    @Override
    public ShoppingList addProductByShoppingListId(Integer shoppingListId, Product product) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(shoppingListId);
        ShoppingList shoppingList = shoppingListOptional.get();
        shoppingList.getProducts().add(product);
        shoppingListRepository.save(shoppingList);
        return shoppingList;
    }

}
