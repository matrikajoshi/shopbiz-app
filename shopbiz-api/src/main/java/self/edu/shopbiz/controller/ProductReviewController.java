package self.edu.shopbiz.controller;


import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import self.edu.shopbiz.dto.ProductReviewDTO;
import self.edu.shopbiz.dto.ShoppingCartDTO;
import self.edu.shopbiz.exceptionUtil.ProductNotFoundException;
import self.edu.shopbiz.exceptionUtil.UserNotFoundException;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.ProductReview;
import self.edu.shopbiz.model.ShoppingCart;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.service.ProductReviewService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProductReviewController {


    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductReviewService productReviewService;
    private final ModelMapper modelMapper;

    public ProductReviewController(ProductRepository productRepository, UserRepository userRepository, ProductReviewService productReviewService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productReviewService = productReviewService;
        this.modelMapper = modelMapper;
    }


    @GetMapping({ "products/{productId:\\d+}/reviews" })
    public List<ProductReviewDTO> getReviews(@PathVariable final Long productId,
                                          @RequestParam(required = false) final Optional<Double> ratingFrom,
                                          @RequestParam(required = false) final Optional<Double> ratingTo)
    {
        final Product product = productRepository.getOne(productId);

        if (product == null)
        {
            throw new ProductNotFoundException(productId);
        }
        List<ProductReview> reviewsForProduct;
        if (!ratingFrom.isPresent() && !ratingTo.isPresent()) {
            reviewsForProduct = productReviewService.getReviewsForProduct(product);
        } else {
            reviewsForProduct = productReviewService.getReviewsForProductWithRatings(product, ratingFrom, ratingTo);
        }

        List<ProductReviewDTO> productReviewDTOS = reviewsForProduct.stream()
                .map(review -> convertToDTO(review))
                .collect(Collectors.toList());

        return productReviewDTOS;
    }

    @PostMapping({ "products/{productId:\\d+}/users/{userId:\\d+}/reviews" })
    public ProductReview createReview(@PathVariable final Integer userId,
                                      @PathVariable final Long productId,
                                      @RequestBody final ProductReviewDTO productReviewDTO)
    {
        final Product product = productRepository.getOne(productId);
        if (product == null)
        {
            throw new ProductNotFoundException(productId);
        }

        final User user = userRepository.getOne(userId);
        if (user == null)
        {
            throw new UserNotFoundException(userId.longValue());
        }

        return productReviewService
                .createCustomerReviewWithValidations(productReviewDTO.getRating(), productReviewDTO.getHeadline(),
                        productReviewDTO.getComment(), product, user);
    }


//        Todo
//    @PutMapping({ "products/{productId:\\d+}/users/{userId:\\d+}/reviews/{reviewId}" })
//    public ProductReview createReview(@PathVariable final Integer userId,
//                                      @PathVariable final Long productId,
//                                      @PathVariable final Integer reviewId,
//                                      @RequestBody final ProductReviewDTO productReviewDTO)
//    {
//
//    }


//
//    @DeleteMapping({ "reviews/{reviewId:\\d+}" })
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @RolesAllowed("ORDER_CREATE")
//    public void deleteReview(@PathVariable final Long reviewId)
//    {
//        productReviewService.deleteCustomerReview(reviewId);
//    }

    public ProductReviewDTO convertToDTO(ProductReview productReview) {
        ProductReviewDTO productReviewDTO = modelMapper.map(productReview, ProductReviewDTO.class);
        return productReviewDTO;
    }


}
