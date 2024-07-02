package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.dto.cart.AddCartRequest;
import com.huuloc.bookstore.bbook.entity.Address;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.enums.PaymentType;
import com.huuloc.bookstore.bbook.service.CartService;
import com.huuloc.bookstore.bbook.service.OrderService;
import com.huuloc.bookstore.bbook.service.auth.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController implements WebMvcConfigurer {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showCart() {
        return "customer/cart/cart";
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddCartRequest addCartRequest) {
        cartService.addToCart(addCartRequest);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @PostMapping(value = "/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeCartItem(id);
        return "redirect:/cart";
    }

    @GetMapping("/address")
    public String showAddress(Model model) {
        Address address = userService.getDefaultAddress();
        if (address == null) {
            address = new Address();
        }
        model.addAttribute("address", address);
        return "customer/cart/address";
    }

    @PostMapping("/address")
    public ModelAndView saveAddress(@ModelAttribute @Valid Address address,
                                    BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.addObject("error", "Thông tin không hợp lệ!");
            modelAndView.addObject("address", address);
            modelAndView.setViewName("customer/cart/address");
            return modelAndView;
        }
        userService.saveAddress(address);
        modelAndView.setViewName("redirect:/cart/checkout");
        return modelAndView;
    }

    @GetMapping("/checkout")
    public String showPayment(Model model,
                              @ModelAttribute("newOrder") Order newOrder) {
        if (newOrder.getOrderItems() == null || newOrder.getOrderItems().isEmpty()) {
            return "redirect:/cart";
        }

        Address address = userService.getDefaultAddress();
        if (address == null) {
            return "redirect:/cart/address";
        }
        model.addAttribute("address", address);
        return "/customer/cart/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("paymentMethod") PaymentType paymentType) {
        Order order = cartService.checkout(paymentType);
        if (paymentType == PaymentType.ONLINE) {
            return "redirect:/payment/order/" + order.getId();
        }
        return "redirect:/order/" + order.getId();
    }
}
