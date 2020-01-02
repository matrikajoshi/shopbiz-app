package self.edu.couponService.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import self.edu.couponService.model.Coupon;

/**
 * Created by mpjoshi on 12/27/19.
 */


public interface CouponRepository extends MongoRepository<Coupon, String> {

    Coupon findByCode(String code);
}
