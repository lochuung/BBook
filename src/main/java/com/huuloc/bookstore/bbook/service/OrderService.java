package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.entity.Order;

import java.util.List;

public interface OrderService {

    Order getNewOrder();

    Order findById(Long id);

    void cancelOrder(Long orderId);

    List<Order> findAll();

    List<Order> findAllCurrentUser();

    void save(Order order);

    Order updateState(Long id, String state);

    void delete(Long id);
}
