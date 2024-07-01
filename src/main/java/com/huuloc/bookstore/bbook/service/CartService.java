package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.dto.cart.AddCartRequest;
import com.huuloc.bookstore.bbook.entity.Order;

public interface CartService {
    void addToCart(AddCartRequest addCartRequest);

    void removeCartItem(Long id);

    void checkout(Order order);
}
