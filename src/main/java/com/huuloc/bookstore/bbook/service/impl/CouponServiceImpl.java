package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.entity.Coupon;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.enums.DiscountType;
import com.huuloc.bookstore.bbook.exception.BadRequestException;
import com.huuloc.bookstore.bbook.repository.CouponRepository;
import com.huuloc.bookstore.bbook.repository.OrderRepository;
import com.huuloc.bookstore.bbook.service.CouponService;
import com.huuloc.bookstore.bbook.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public Coupon addCouponByOrderId(Long orderId, String code) {
        Order order = orderService.findById(orderId);
        Coupon coupon = couponRepository.findByCodeWithLock(code);
        if (coupon == null) {
            log.info("Coupon not found");
            throw BadRequestException.message("Mã giảm giá không tồn tại");
        }
        // Check if the coupon is valid
        if (coupon.getExpiredDate().isBefore(LocalDateTime.now())) {
            log.info("Coupon is expired");
            throw BadRequestException.message("Mã giảm giá đã hết hạn");
        }
        // Check if the coupon is used
        if (coupon.getCurrentUse() >= coupon.getMaxUse()) {
            log.info("Coupon is used up");
            throw BadRequestException.message("Mã giảm giá đã được sử dụng hết");
        }
        // Check if the coupon is used by this order
        if (order.getCoupon() != null) {
            log.info("Order already has a coupon");
            throw BadRequestException.message("Mã giảm giá đã được sử dụng cho đơn hàng này");
        }
        if (order.getSubtotal() < coupon.getMinOrderValue() || order.getSubtotal() > coupon.getMaxOrderValue()) {
            log.info("Order value is not valid");
            throw BadRequestException.message("Giá trị đơn hàng không hợp lệ");
        }

        if (coupon.getType() == DiscountType.PERCENT) {
            order.setDiscount(order.getSubtotal() * coupon.getValue() * 1.0 / 100);
        } else {
            order.setDiscount(coupon.getValue());
        }

        order.setCoupon(coupon);
        order.setTotalPrice(order.getSubtotal() - order.getDiscount());
        orderRepository.save(order);
        coupon.setCurrentUse(coupon.getCurrentUse() + 1);
        couponRepository.save(coupon);
        return coupon;
    }

    @Override
    public Coupon cancelCouponByOrderId(Long orderId) {
        Order order = orderService.findById(orderId);
        Coupon coupon = order.getCoupon();
        if (coupon == null) {
            return null;
        }
        order.setCoupon(null);
        order.setTotalPrice(order.getTotalPrice() + order.getDiscount());
        order.setDiscount(0.0);
        orderRepository.save(order);
        return coupon;
    }
}
