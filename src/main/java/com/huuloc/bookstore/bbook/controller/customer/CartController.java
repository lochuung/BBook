package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String showCart() {
        return "customer/cart";
    }
}
