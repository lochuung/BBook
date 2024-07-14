package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.entity.Coupon;

public interface CouponService {
    Coupon addCouponByOrderId(Long orderId, String code);

    Coupon cancelCouponByOrderId(Long orderId);
}
