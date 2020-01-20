package self.edu.couponService.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import self.edu.couponService.model.Coupon;

import java.util.List;

/**
 * Created by mpjoshi on 12/27/19.
 */


public interface CouponRepository extends MongoRepository<Coupon, String> {

    List<Coupon> findByCode(String code);
}
