package self.edu.shopbiz.restclients;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import self.edu.shopbiz.dto.Coupon;

import java.util.List;

//@FeignClient("zuul-api-gateway")
public interface CouponClient {

    // TO DO - cover with integration test, no unit testing
    @GetMapping("/coupon-service/coupons/{sku}")
    Coupon getCouponBySku(@PathVariable("sku") String sku);

    @GetMapping("/coupon-service/coupons")
    List<Coupon> getAllCoupons();

}
