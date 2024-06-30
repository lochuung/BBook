package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import com.huuloc.bookstore.bbook.exception.BadRequestException;
import com.huuloc.bookstore.bbook.repository.OrderRepository;
import com.huuloc.bookstore.bbook.repository.UserRepository;
import com.huuloc.bookstore.bbook.service.OrderService;
import com.huuloc.bookstore.bbook.service.auth.CustomOAuth2User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Order getPendingOrder() {
        String email = getEmailFromAuthentication();
        if (email == null) {
            return null;
        }

        Order order = orderRepository.findByUserEmailAndState(email, OrderState.PENDING);
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

    private String getEmailFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getUsername();
        } else if (principal instanceof CustomOAuth2User) {
            return ((CustomOAuth2User) principal).getEmail();
        }
        return null;
    }
}
