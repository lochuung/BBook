package com.huuloc.bookstore.bbook.service;

public interface PaymentService {
    String createPaymentLink(Long orderId,
                             String returnUrl,
                             String cancelUrl) throws Exception;

    Long updatePaymentStatus(String orderCode) throws Exception;
}
