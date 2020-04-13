package self.edu.couponService.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import self.edu.couponService.exceptionUtil.ResourceNotFoundException;
import self.edu.couponService.model.Coupon;
import self.edu.couponService.repos.CouponRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by mpjoshi on 12/27/19.
 */

@Slf4j
@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    Environment environment;

    @Autowired
    CouponRepository couponRepository;

    @PostMapping
    public Coupon addNewCoupon(@RequestBody Coupon coupon)  {
        Coupon save = couponRepository.save(coupon);
        return save;
    }

    @GetMapping
    public List<Coupon> getAllCoupons() {
        log.debug("***Running in port: " + environment.getProperty("local.server.port"));
        List<Coupon> coupons = couponRepository.findAll();
        return coupons;
    }

    @GetMapping(path = "/{sku}")
    public Coupon getCouponBySku(@PathVariable("sku") String sku) {
        log.debug("***Running in port: " + environment.getProperty("local.server.port"));
        log.debug("Getting coupon for sku {}", sku);
        List<Coupon> couponList = couponRepository.findBySku(sku);
        Optional<Coupon> couponOptional = couponList
                .stream()
                .filter(coupon ->
                            coupon.getExpDate()
                                    .isAfter(LocalDate.now()))
                .findFirst();
        return couponOptional.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }


}
