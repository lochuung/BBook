package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.OnlinePayment;
import com.huuloc.bookstore.bbook.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<OnlinePayment, Long> {
    Optional<OnlinePayment> findByPaymentId(String paymentId);

    Optional<OnlinePayment> findByStatusNot(PaymentStatus status);
}
