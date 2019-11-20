package self.edu.shopbiz.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import self.edu.shopbiz.dto.ShoppingListDto;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.ShoppingList;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.ShoppingListService;
import self.edu.shopbiz.util.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mpjoshi on 9/29/19.
 */

@RestController
@Api(tags = {"Shopping List"})
public class ShoppingListController {

    private ShoppingListService shoppingListService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping(path="/shoppingList")
    @PreAuthorize(value = "hasAnyRole('ORDER_CREATE')")
    public ShoppingListDto getDefaultShoppingList() {
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        ShoppingList shoppingListByUserId = shoppingListService.getShoppingListByUserId(loggedInUser.getUser().getId());
        if (null == shoppingListByUserId) {
            shoppingListByUserId = new ShoppingList();
            shoppingListByUserId.setUser(loggedInUser.getUser());
        }
        ShoppingListDto shoppingListDto = convertToDTO(shoppingListByUserId);
        return shoppingListDto;
    }


    @PutMapping(path = "/shoppingList/addProduct")
    public ShoppingList addProductToShoppingList(@RequestBody Product product ) {
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        ShoppingList shoppingList = shoppingListService.addProductByUserId(loggedInUser.getUser(), product);
        return shoppingList;
    }

    @PutMapping(path = "/shoppingList/removeProduct")
    public ShoppingList removeProductFromShoppingList(@RequestBody Product product ) {
        Integer userId = SecurityUtil.getLoggedInUserId();
        ShoppingList shoppingList = shoppingListService.removeProductByUserId(userId, product);
        return shoppingList;
    }


    private ShoppingListDto convertToDTO(ShoppingList shoppingList){
        ShoppingListDto shoppingListDto = modelMapper.map(shoppingList, ShoppingListDto.class);
        return shoppingListDto;
    }



}
