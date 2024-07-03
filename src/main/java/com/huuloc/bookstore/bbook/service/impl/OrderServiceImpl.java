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
        if (order.getState() != OrderState.PAYMENT &&
                (order.getState() != OrderState.PENDING ||
                        order.getPaymentType() != PaymentType.COD)
        ) {
            return;
        }
        for (OrderItem item : order.getOrderItems()) {
            Book book = bookRepository.findByIdWithLock(item.getBook().getId());
            book.setAvailableQuantity(book.getAvailableQuantity() + item.getQuantity());
            book.setTotalSold(book.getTotalSold() - item.getQuantity());
            item.setBook(book);
            bookRepository.save(book);
        }
        order.setState(OrderState.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuthentication(authentication);
        if (email == null) {
            return null;
        }
        return orderRepository.findAlByUserEmailAndStateNot(email, OrderState.NEW);
    }
}
