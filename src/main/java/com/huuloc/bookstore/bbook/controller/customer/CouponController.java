package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@Slf4j
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping("/add")
    public ResponseEntity<?> addCouponByOrderId(@RequestBody CouponRequest request) {
        log.info("Add coupon by order id: {}", request.orderId());
        log.info("Coupon code: {}", request.code());
        return ResponseEntity.ok(couponService
                .addCouponByOrderId(request.orderId(), request.code())
        );
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelCouponByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(couponService.cancelCouponByOrderId(orderId));
    }

    record CouponRequest(Long orderId, String code) {
    }
}
