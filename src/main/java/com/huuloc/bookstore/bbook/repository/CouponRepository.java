package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT c FROM coupons c WHERE c.code = ?1")
    Coupon findByCodeWithLock(String code);
}
