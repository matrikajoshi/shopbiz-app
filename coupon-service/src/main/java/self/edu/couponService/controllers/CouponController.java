package self.edu.couponService.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import self.edu.couponService.model.Coupon;
import self.edu.couponService.repos.CouponRepository;

/**
 * Created by mpjoshi on 12/27/19.
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class CouponController {

    @Autowired
    CouponRepository couponRepository;

    @PostMapping(path="/coupons")
    public Coupon addNewCoupon(@RequestBody Coupon coupon)  {
        Coupon save = couponRepository.save(coupon);
        return save;
    }

    @GetMapping(path = "/coupon/{code}")
    public Coupon getCoupon(@PathVariable("code") String code) {
        log.debug("Getting coupon for code {}", code);
        return couponRepository.findByCode(code);
    }


}
