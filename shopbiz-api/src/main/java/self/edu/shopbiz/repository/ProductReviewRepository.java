package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import self.edu.shopbiz.model.ProductReview;
import self.edu.shopbiz.model.LanguageModel;
import self.edu.shopbiz.model.Product;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {

    @Query("select reviews from ProductReview reviews where reviews.product = ?1")
    List<ProductReview> getAllReviews(Product product);

    @Query("select reviews from ProductReview reviews where reviews.product = ?1 and reviews.rating >= ?2")
    List<ProductReview> getAllReviewsWithRatingFrom(Product product, Double ratingFrom);

    @Query("select reviews from ProductReview reviews where reviews.product = ?1 and reviews.rating <= ?2")
    List<ProductReview> getAllReviewsWithRatingTo(Product product, Double ratingTo);

    @Query("select reviews from ProductReview reviews where reviews.product = ?1 and reviews.rating between ?2 and ?3")
    List<ProductReview> getAllReviewsWithRatings(Product product, Double ratingFrom, Double ratingTo);

    @Query("select count(reviews) from ProductReview reviews where reviews.product = ?1")
    Integer getNumberOfReviews(Product product);

    @Query("select avg(reviews.rating) from ProductReview reviews where reviews.product = ?1")
    Double getAverageRating(Product product);

    @Query("select reviews from ProductReview reviews where reviews.product = ?1 and reviews.language = ?2")
    List<ProductReview> getReviewsForProductAndLanguage(Product product, LanguageModel language);

    @Transactional
    @Modifying
    @Query("delete from ProductReview reviews where reviews.id = ?1")
    void deleteReview(Long id);

}
