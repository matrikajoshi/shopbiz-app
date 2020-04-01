package self.edu.shopbiz.service;

import self.edu.shopbiz.model.ProductReview;
import self.edu.shopbiz.model.LanguageModel;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.model.User;

import java.util.List;
import java.util.Optional;


public interface ProductReviewService
{
	ProductReview createCustomerReview(Double rating, String headline, String comment, Product product, User user);

	ProductReview createCustomerReviewWithValidations(Double rating, String headline, String comment, Product product, User user);

	void updateCustomerReview(ProductReview customer, User user, Product product);

	List<ProductReview> getReviewsForProduct(Product product);

	List<ProductReview> getReviewsForProductWithRatings(Product product, Optional<Double> ratingFrom, Optional<Double> ratingTo);

	Double getAverageRating(Product product);

	Integer getNumberOfReviews(Product product);

	List<ProductReview> getReviewsForProductAndLanguage(Product product, LanguageModel language);

	void deleteCustomerReview(Long id);
}
