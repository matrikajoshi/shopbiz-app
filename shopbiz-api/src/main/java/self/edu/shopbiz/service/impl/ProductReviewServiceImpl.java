package self.edu.shopbiz.service.impl;

import org.springframework.stereotype.Service;
import self.edu.shopbiz.enums.CurseWords;
import self.edu.shopbiz.exceptionUtil.ResourceNotFoundException;
import self.edu.shopbiz.exceptionUtil.ReviewNotValidException;
import self.edu.shopbiz.model.ProductReview;
import self.edu.shopbiz.model.LanguageModel;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.ProductReviewRepository;
import self.edu.shopbiz.service.ProductReviewService;
import self.edu.shopbiz.util.ServicesUtil;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {


    private final ProductReviewRepository productReviewRepository;
    private Set<String> blockedWords;

    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    @PostConstruct
    public void ReviewServiceInit() {
        // load all blocked words in memory one time
        // this validation logic could be moved to util class
        blockedWords = Arrays
                            .stream(CurseWords.values())
                            .collect(
                                    HashSet::new,
                                    (set, e) -> set.add(e.getValue().toLowerCase()),
                                    HashSet::addAll
                            );
    }

    @Override
    public ProductReview createCustomerReview(Double rating, String headline, String comment, Product product, User user) {
        final ProductReview review = getCustomerReviewModel(rating, headline, comment, product, user);
        ProductReview save = productReviewRepository.save(review);
        return save;
    }

    @Override
    public ProductReview createCustomerReviewWithValidations(Double rating, String headline, String comment, Product product, User user) {
        final ProductReview review = getCustomerReviewModel(rating, headline, comment, product, user);
        validateProductReview(review);
        ProductReview save = productReviewRepository.save(review);
        return save;
    }

    @Override
    public void updateCustomerReview(ProductReview customer, User user, Product product) {

    }

    @Override
    public List<ProductReview> getReviewsForProduct(Product product) {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        return productReviewRepository.getAllReviews(product);
    }

    @Override
    public List<ProductReview> getReviewsForProductWithRatings(Product product, Optional<Double> ratingFrom, Optional<Double> ratingTo) {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);

        Map<String,Double> ratingsLimitMap = ServicesUtil.getRatingsLimit(ratingFrom, ratingTo);
        Double ratingStartFrom = ratingsLimitMap.get(ServicesUtil.RATING_FROM);
        Double ratingUpTo = ratingsLimitMap.get(ServicesUtil.RATING_TO);

        if (ratingStartFrom == 0) {
            return productReviewRepository.getAllReviewsWithRatingTo(product, ratingUpTo);
        }
        if (ratingUpTo.equals(Double.MAX_VALUE)) {
            return productReviewRepository.getAllReviewsWithRatingFrom(product, ratingStartFrom);
        }
        return productReviewRepository.getAllReviewsWithRatings(product, ratingStartFrom, ratingUpTo);
    }

    @Override
    public Double getAverageRating(Product product) {
        return null;
    }

    @Override
    public Integer getNumberOfReviews(Product product) {
        return null;
    }

    @Override
    public List<ProductReview> getReviewsForProductAndLanguage(Product product, LanguageModel language) {
        return null;
    }

    @Override
    public void deleteCustomerReview(Long id) {
        productReviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with id: " + id + " not found"));
        productReviewRepository.deleteById(id);
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

    private boolean validateProductReview(ProductReview customerReviewForm) {
        // if it is negative, return a 422 Unprocessable Entity status code
        if (customerReviewForm.getRating() < 0) {
            throw new ReviewNotValidException("Negative rating is not allowed");
        }
        if (containsCurseWords(customerReviewForm.getHeadline())) {
            throw new ReviewNotValidException("Headline contains restricted words");
        }
        if (containsCurseWords(customerReviewForm.getComment())) {
            throw new ReviewNotValidException("Comment contains restricted words");
        }
        return true;
    }


    private boolean containsCurseWords(String text) {
        for (String word : text.split(" ")) {
            if (blockedWords.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
