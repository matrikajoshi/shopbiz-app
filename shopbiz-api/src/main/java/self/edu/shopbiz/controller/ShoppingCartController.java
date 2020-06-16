package self.edu.shopbiz.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import self.edu.shopbiz.dto.CartItemDTO;
import self.edu.shopbiz.dto.ShoppingCartDTO;
import self.edu.shopbiz.model.CartItem;
import self.edu.shopbiz.model.ShoppingCart;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.ProductService;
import self.edu.shopbiz.service.ShoppingCartService;
import self.edu.shopbiz.service.UserService;
import self.edu.shopbiz.util.SecurityUtil;


@RestController()
@Tag(name ="Shopping Cart")
public class ShoppingCartController {

    private static final String SHOPPING_CART = "shoppingCart";

    private ProductService productService;

    private ShoppingCartService shoppingCartService;

    private UserService userService;

    private ModelMapper modelMapper;

    @Autowired
    public ShoppingCartController(ProductService productService,
                                  ShoppingCartService shoppingCartService,
                                  UserService userService,
                                  ModelMapper modelMapper) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @GetMapping(path="/shoppingCart")
    public ShoppingCartDTO getShoppingCart(Authentication authentication){
        MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomerId(userDetails.getUser().getId())
                .orElseGet(() -> new ShoppingCart());

        return convertToDTO(shoppingCart);
    }

    @PostMapping(path = "/shoppingCart")
    public ShoppingCartDTO createShoppingCart(@RequestBody CartItemDTO cartItemDto) {
        Authentication authentication = SecurityUtil.getAuthentication();
        MyUserPrincipal myUserPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        Integer userId = myUserPrincipal.getUser().getId();
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomerId(userId)
                .orElseGet(() -> new ShoppingCart());
        CartItem cartItem = convertToEntity(cartItemDto);
        ShoppingCart cartItemToShoppingCart = shoppingCartService.addCartItemToShoppingCart(myUserPrincipal, cartItem, shoppingCart);
        ShoppingCartDTO shoppingCartDTO = convertToDTO(cartItemToShoppingCart);
        return shoppingCartDTO;
    }

    @PutMapping(path = "/shoppingCart/{cartItemId}")
    public ShoppingCartDTO updateCartItemQuantity(@PathVariable("cartItemId") Integer cartItemId,
                                               @RequestBody Integer itemCount) {
        ShoppingCart cart = shoppingCartService.updateCartItem(cartItemId, itemCount);
        ShoppingCartDTO shoppingCartDTO = convertToDTO(cart);
        return shoppingCartDTO;
    }

    @DeleteMapping(path="/shoppingCart/{cartItemId}")
    public ShoppingCart deleteShoppingCartItem(@PathVariable("cartItemId") Integer cartItemId){
        //to do check authentication / authorization
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        ShoppingCart shoppingCart1 = shoppingCartService.removeCartItem(loggedInUser, cartItemId);
        return shoppingCart1;
    }

    private CartItem convertToEntity(CartItemDTO cartItemDto) {
        CartItem cartItem = modelMapper.map(cartItemDto, CartItem.class);
        return cartItem;
    }

    public ShoppingCartDTO convertToDTO(ShoppingCart shoppingCart) {
        ShoppingCartDTO shoppingCartDTO = modelMapper.map(shoppingCart, ShoppingCartDTO.class);
        return shoppingCartDTO;
    }


    private CartItemDTO convertCartItemToDTO(ShoppingCart cartItem) {
        CartItemDTO itemDTO = modelMapper.map(cartItem, CartItemDTO.class);
        return itemDTO;
    }








}
