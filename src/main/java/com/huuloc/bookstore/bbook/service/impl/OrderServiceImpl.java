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

import static com.huuloc.bookstore.bbook.util.AuthUtils.getEmailFromAuthentication;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

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

        return null;
    }
}
