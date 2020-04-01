package self.edu.shopbiz.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import self.edu.shopbiz.controller.ProductReviewController;
import self.edu.shopbiz.dto.CustomerReviewForm;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.ProductReview;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.service.ProductReviewService;
import self.edu.shopbiz.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {ProductReviewController.class})
@WebMvcTest(ProductReviewController.class)
class ProductReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    ProductReviewService productReviewService;

    @MockBean
    UserRepository userRepository;

    @Test
    @WithMockUser
    void getReviews() throws Exception {
        Product product = new Product();
        given(productRepository.getOne(eq(1l))).willReturn(product);
        given(productReviewService.getReviewsForProduct(any())).willReturn(getReviewsList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/1/reviews")
                .accept(MediaType.APPLICATION_JSON);
        ResultActions results = mockMvc.perform(requestBuilder);
        results.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();
        verify(productReviewService, times(1)).getReviewsForProduct(product);
        verifyNoMoreInteractions(productReviewService);
    }

    @Test
    @WithMockUser
    void getReviewsWithRatings() throws Exception {
        Product product = new Product();
        given(productRepository.getOne(eq(1l))).willReturn(product);
        List<ProductReview> reviewModels = getReviewsList().stream().filter(r -> r.getRating() > 4).collect(Collectors.toList());
        given(productReviewService.getReviewsForProductWithRatings(any(), any(), any())).willReturn(reviewModels);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/1/reviews?ratingFrom=4")
                .accept(MediaType.APPLICATION_JSON);
        ResultActions results = mockMvc.perform(requestBuilder);
        results.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].rating", greaterThanOrEqualTo(Double.valueOf(4.0d))))
                .andReturn();
        verify(productReviewService, times(1)).getReviewsForProductWithRatings(product, Optional.ofNullable(4.0d), Optional.empty());
        verifyNoMoreInteractions(productReviewService);
    }


    @Test
    @WithMockUser
    public void createReview() throws Exception {
        CustomerReviewForm customerReviewForm = new CustomerReviewForm();
        Product product = new Product();
        when(productRepository.getOne(Long.valueOf(1l))).thenReturn(product);
        final User user = new User();
        when(userRepository.getOne(1)).thenReturn(user);
        ProductReview review1 = getCustomerReviewModel(2.5d, "headline1", "comment1", product, user);
        doReturn(review1).when(productReviewService).createCustomerReviewWithValidations(review1.getRating(), review1.getHeadline(), review1.getComment(), product, user);

        //invoke test method
        String apiUrl = "/products/1/users/1/reviews";
        mockMvc.perform(post(apiUrl).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(customerReviewForm)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteReview() {
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