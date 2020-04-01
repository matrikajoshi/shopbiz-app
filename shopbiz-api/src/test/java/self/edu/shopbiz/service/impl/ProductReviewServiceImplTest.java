package self.edu.shopbiz.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;
import self.edu.shopbiz.enums.CurseWords;
import self.edu.shopbiz.model.ProductReview;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.ProductReviewRepository;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class ProductReviewServiceImplTest {

    @Mock
    ProductReviewRepository productReviewRepository;

    @InjectMocks
    ProductReviewServiceImpl customerReviewService;

    @BeforeEach
    void setUp() {
        Field field = ReflectionUtils.findField(ProductReviewServiceImpl.class, "blockedWords");
        ReflectionUtils.makeAccessible(field);
        HashSet<String> blockedWords = Arrays
                .stream(CurseWords.values())
                .collect(
                        HashSet::new,
                        (set, e) -> set.add(e.getValue().toLowerCase()),
                        HashSet::addAll
                );
        ReflectionUtils.setField(field, customerReviewService, blockedWords);
    }

    @Test
    void createCustomerReviewWithValidations() {
        Product product = new Product();
        User user = new User();
        ProductReview review1 = getCustomerReviewModel(2.5d, "headline1", "comment1", product, user);
        when(productReviewRepository.save(any(ProductReview.class))).thenReturn(review1);
        //invoke test method
        ProductReview actualReview = customerReviewService.createCustomerReviewWithValidations(review1.getRating(), review1.getHeadline(), review1.getComment(), product, user);
        assertEquals(review1, actualReview);
    }

    @Test
    public void createCustomerReviewWithValidations_WithBlockedWords() {
        Product product = new Product();
        User user = new User();
        ProductReview review1 = getCustomerReviewModel(2.5d, "headline1", "comment1-mother", product, user);
        when(productReviewRepository.save(Matchers.any(ProductReview.class))).thenReturn(review1);
        //invoke test method
        ProductReview actualReview = customerReviewService.createCustomerReviewWithValidations(review1.getRating(), review1.getHeadline(), review1.getComment(), product, user);
        assertEquals(review1, actualReview);
    }

    @Test
    void getReviewsForProductWithRatings() {
        List<ProductReview> reviews = getReviewsList();
        // when(customerReviewRepository.getAllReviewsWithRatingTo(any(), eq(3.5d))).thenReturn(reviews.subList(0, reviews.size()-1));
        // when(customerReviewRepository.getAllReviewsWithRatingFrom(any(), eq(3.5d))).thenReturn(reviews.subList(1, reviews.size()));
        when(productReviewRepository.getAllReviewsWithRatings(any(), eq(Double.valueOf(2.5)), eq(4.5))).thenReturn(reviews);
        List<ProductReview> reviewsForProductWithRatings = customerReviewService.getReviewsForProductWithRatings(new Product(), Optional.ofNullable(2.5d), Optional.ofNullable(4.5d));
        assertEquals(reviews, reviewsForProductWithRatings);
    }


    private List<ProductReview> getReviewsList() {
        List<ProductReview> reviews = new ArrayList<>();
        Product product = new Product();
        User user = new User();
        ProductReview review1 = getCustomerReviewModel(2.5d, "headline1", "comment1", product, user);
        ProductReview review2 = getCustomerReviewModel(3.5d, "headline2", "comment2", product, user);
        ProductReview review3 = getCustomerReviewModel(4.5d, "headline3", "comment3", product, user);
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        return reviews;
    }

    private ProductReview getCustomerReviewModel(Double rating, String headline, String comment, Product product, User user) {
        final ProductReview review = new ProductReview();
        review.setRating(rating);
        review.setHeadline(headline);
        review.setComment(comment);
        review.setProduct(product);
        review.setUser(user);
        return review;
    }
}