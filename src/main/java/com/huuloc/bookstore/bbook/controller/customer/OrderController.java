package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.dto.filter.SearchRequest;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.service.OrderService;
import com.huuloc.bookstore.bbook.util.OrderUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public String viewOrderDetail(@PathVariable("orderId") Long orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "error/404";
        }
        model.addAttribute("order", order);
        model.addAttribute("tienChu", OrderUtils.getTienChu(order.getTotalPrice()));
        return "customer/order-detail";
    }

    @GetMapping("/list")
    public String viewOrderList(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "customer/order-list";
    }

    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable("orderId") Long orderId,
                            HttpServletResponse response) {
        orderService.cancelOrder(orderId);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
