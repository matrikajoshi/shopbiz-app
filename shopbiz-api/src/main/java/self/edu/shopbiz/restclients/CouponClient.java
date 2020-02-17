package self.edu.shopbiz.restclients;


import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import self.edu.shopbiz.dto.Coupon;

import java.util.List;

@FeignClient("zuul-api-gateway")
//@FeignClient("COUPON-SERVICE")
//@RibbonClient("COUPON-SERVICE")
public interface CouponClient {

    // TO DO - cover with integration test, no unit testing
    @GetMapping("/coupon-service/coupons/{sku}")
    Coupon getCouponBySku(@PathVariable("sku") String sku);

    @GetMapping("/coupon-service/coupons")
    List<Coupon> getAllCoupons();

}
