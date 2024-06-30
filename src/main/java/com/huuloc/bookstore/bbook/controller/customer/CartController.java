package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.dto.cart.AddCartRequest;
import com.huuloc.bookstore.bbook.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public String showCart() {
        return "customer/cart";
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddCartRequest addCartRequest) {
        orderItemService.addToCart(addCartRequest);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @PostMapping(value = "/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        orderItemService.removeOrderItem(id);
        return "redirect:/cart";
    }
}
