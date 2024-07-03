package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.dto.cart.AddCartRequest;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.enums.PaymentType;

public interface CartService {
    void addToCart(AddCartRequest addCartRequest);

    void removeCartItem(Long id);

    Order checkout(PaymentType order);
}
