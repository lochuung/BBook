package com.huuloc.bookstore.bbook.repository.custom;

public interface CustomOrderRepository {
    void checkoutOrder(Long orderId, Long addressId, String paymentType);
    void cancelOrder(Long orderId);
}
