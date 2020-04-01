package self.edu.shopbiz.controller;


import org.springframework.web.bind.annotation.*;
import self.edu.shopbiz.dto.CustomerReviewForm;
import self.edu.shopbiz.exceptionUtil.ProductNotFoundException;
import self.edu.shopbiz.exceptionUtil.UserNotFoundException;
import self.edu.shopbiz.model.ProductReview;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.service.ProductReviewService;

import java.util.*;

@RestController
public class ProductReviewController {


    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductReviewService productReviewService;

    public ProductReviewController(ProductRepository productRepository, UserRepository userRepository, ProductReviewService productReviewService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productReviewService = productReviewService;
    }


    @GetMapping({ "products/{productId:\\d+}/reviews" })
    public List<ProductReview> getReviews(@PathVariable final Long productId,
                                          @RequestParam(required = false) final Optional<Double> ratingFrom,
                                          @RequestParam(required = false) final Optional<Double> ratingTo)
    {
        final Product product = productRepository.getOne(productId);

        if (product == null)
        {
            throw new ProductNotFoundException(productId);
        }

        if (!ratingFrom.isPresent() && !ratingTo.isPresent()) {
            return productReviewService.getReviewsForProduct(product);
        } else {
            return productReviewService.getReviewsForProductWithRatings(product, ratingFrom, ratingTo);
        }

    }

    @PostMapping({ "products/{productId:\\d+}/users/{userId:\\d+}/reviews" })
    public ProductReview createReview(@PathVariable final Integer userId, @PathVariable final Long productId,
                                      @RequestBody final CustomerReviewForm customerReviewForm)
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
                .createCustomerReviewWithValidations(customerReviewForm.getRating(), customerReviewForm.getHeadline(),
                        customerReviewForm.getComment(), product, user);
    }



    @DeleteMapping({ "reviews/{reviewId:\\d+}" })
    public void deleteReview(@PathVariable final Long reviewId)
    {
        productReviewService.deleteCustomerReview(reviewId);
    }


}
