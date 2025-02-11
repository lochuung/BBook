package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.OrderItem;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import com.huuloc.bookstore.bbook.entity.enums.PaymentType;
import com.huuloc.bookstore.bbook.exception.BadRequestException;
import com.huuloc.bookstore.bbook.repository.BookRepository;
import com.huuloc.bookstore.bbook.repository.OrderRepository;
import com.huuloc.bookstore.bbook.repository.UserRepository;
import com.huuloc.bookstore.bbook.repository.custom.CustomOrderRepository;
import com.huuloc.bookstore.bbook.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.huuloc.bookstore.bbook.util.AuthUtils.getEmailFromAuthentication;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomOrderRepository customOrderRepository;

    @Override
    @Transactional
    public Order getNewOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuthentication(authentication);
        if (email == null) {
            return null;
        }

        Order order = orderRepository.findByUserEmailAndState(email, OrderState.NEW);
        if (order == null) {
            com.huuloc.bookstore.bbook.entity.User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> BadRequestException.message("User not found"));
            order = Order.builder()
                    .user(user)
                    .build();
            order = orderRepository.save(order);
        }
        return order;
    }

    @Override
    public Order findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuthentication(authentication);
        if (email == null) {
            return null;
        }
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuthentication(authentication);
        if (email == null) {
            return;
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return;
        }
        if (!order.getUser().getEmail().equals(email)) {
            return;
        }
        customOrderRepository.cancelOrder(orderId);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllByStateNot(OrderState.NEW)
                .stream().sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()))
                .toList();
    }

    @Override
    public List<Order> findAllCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuthentication(authentication);
        if (email == null) {
            return null;
        }
        return orderRepository.findAlByUserEmailAndStateNot(email, OrderState.NEW)
                .stream().sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()))
                .toList();
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order updateState(Long id, String state) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }
        if (OrderState.NEW.isState(state)) {
            return null;
        }
        if (OrderState.PAYMENT.isState(state)) {
            order.setState(OrderState.PAYMENT);
        } else if (OrderState.PENDING.isState(state)) {
            order.setState(OrderState.PENDING);
        } else if (OrderState.SHIPPING.isState(state)) {
            order.setState(OrderState.SHIPPING);
        } else if (OrderState.DELIVERED.isState(state)) {
            order.setState(OrderState.DELIVERED);
        }
        return orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
