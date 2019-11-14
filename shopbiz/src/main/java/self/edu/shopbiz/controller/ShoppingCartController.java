package self.edu.shopbiz.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import self.edu.shopbiz.dto.CartItemDto;
import self.edu.shopbiz.model.CartItem;
import self.edu.shopbiz.model.ShoppingCart;
import self.edu.shopbiz.security.MyUserPrincipal;
import self.edu.shopbiz.service.ProductService;
import self.edu.shopbiz.service.ShoppingCartService;
import self.edu.shopbiz.service.UserService;
import self.edu.shopbiz.util.SecurityUtil;


@RestController()
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
    }

    @GetMapping(path="/shoppingCart")
    public ShoppingCart getShoppingCart(Authentication authentication){
        MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomerId(userDetails.getUser().getId())
                .orElseGet(() -> new ShoppingCart());

        return shoppingCart;
    }


    @PostMapping(path = "/shoppingCart")
    public ShoppingCart createShoppingCart(@RequestBody CartItemDto cartItemDto) {
        Authentication authentication = SecurityUtil.getAuthentication();
        MyUserPrincipal myUserPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        Integer userId = myUserPrincipal.getUser().getId();
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomerId(userId)
                .orElseGet(() -> new ShoppingCart());
        CartItem cartItem = convertToEntity(cartItemDto);
        ShoppingCart cartItemToShoppingCart = shoppingCartService.addCartItemToShoppingCart(myUserPrincipal, cartItem, shoppingCart);
        return cartItemToShoppingCart;
    }



    @PutMapping(path="/shoppingCart/addItem")
    public ShoppingCart addProductToShoppingCart(@RequestBody CartItem cartItem){
        MyUserPrincipal myUserPrincipal = SecurityUtil.getLoggedInUser();
        Integer userId = myUserPrincipal.getUser().getId();
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCustomerId(userId)
                .orElseGet(() -> new ShoppingCart());
        shoppingCart = shoppingCartService.addCartItemToShoppingCart(myUserPrincipal, cartItem, shoppingCart);
        return shoppingCart;
    }

    @PutMapping(path = "/shoppingCart/{cartItemId}")
    public CartItem updateCartItemQuantity(@PathVariable("cartItemId") Integer cartItemId,
                                               @RequestBody Integer itemCount) {
        CartItem cartItem = shoppingCartService.updateCartItem(cartItemId, itemCount);
        return cartItem;
    }

    @DeleteMapping(path="/shoppingCart/{cartItemId}")
    public ShoppingCart deleteShoppingCartItem(@PathVariable("cartItemId") Integer cartItemId){
        //to do check authentication / authorization
        MyUserPrincipal loggedInUser = SecurityUtil.getLoggedInUser();
        ShoppingCart shoppingCart1 = shoppingCartService.removeCartItem(loggedInUser, cartItemId);
        return shoppingCart1;
    }

    private CartItem convertToEntity(CartItemDto cartItemDto) {
        CartItem cartItem = modelMapper.map(cartItemDto, CartItem.class);
        return cartItem;
    }







}
