package self.edu.shopbiz.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import self.edu.shopbiz.dto.ShoppingListDTO;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.ShoppingList;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.ShoppingListService;
import self.edu.shopbiz.util.SecurityUtil;

/**
 * Created by mpjoshi on 9/29/19.
 */

@RestController
@RequestMapping(path = "/shoppingList")
@Tag(name="Shopping List")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    private final ModelMapper modelMapper;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService, ModelMapper modelMapper) {
        this.shoppingListService = shoppingListService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ORDER_CREATE')")
    public ShoppingListDTO getDefaultShoppingList() {
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        ShoppingList shoppingListByUserId = shoppingListService.getShoppingListByUserId(loggedInUser.getUser().getId());
        if (null == shoppingListByUserId) {
            shoppingListByUserId = new ShoppingList();
            shoppingListByUserId.setCustomer(loggedInUser.getUser());
        }
        ShoppingListDTO shoppingListDto = convertToDTO(shoppingListByUserId);
        return shoppingListDto;
    }

    @PostMapping
    public ShoppingListDTO addProductToShoppingList(@RequestBody Product product ) {
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        ShoppingList shoppingList = shoppingListService.addProductByUserId(loggedInUser.getUser(), product);
        ShoppingListDTO shoppingListDto = convertToDTO(shoppingList);
        return shoppingListDto;
    }

    @PutMapping(path = "/removeProduct")
    public ShoppingListDTO removeProductFromShoppingList(@RequestBody Product product ) {
        Integer userId = SecurityUtil.getLoggedInUserId();
        ShoppingList shoppingList = shoppingListService.removeProductByUserId(userId, product);
        ShoppingListDTO shoppingListDto = convertToDTO(shoppingList);
        return shoppingListDto;
    }


    private ShoppingListDTO convertToDTO(ShoppingList shoppingList){
        ShoppingListDTO shoppingListDto = modelMapper.map(shoppingList, ShoppingListDTO.class);
        return shoppingListDto;
    }



}
