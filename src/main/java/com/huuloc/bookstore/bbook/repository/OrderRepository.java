package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserEmailAndState(String email, OrderState state);

    List<Order> findAlByUserEmailAndStateNot(String email, OrderState state);

    List<Order> findAllByState(OrderState state);

    List<Order> findAllByStateNot(OrderState state);
}
