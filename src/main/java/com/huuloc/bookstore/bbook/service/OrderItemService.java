package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.dto.cart.AddCartRequest;

public interface OrderItemService {
    void addToCart(AddCartRequest addCartRequest);

    void removeOrderItem(Long id);
}
