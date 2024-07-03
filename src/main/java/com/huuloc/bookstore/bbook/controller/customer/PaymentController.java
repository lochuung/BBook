package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.config.AppConfig;
import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.service.OrderService;
import com.huuloc.bookstore.bbook.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/order/{orderId}")
    public void checkout(@PathVariable Long orderId, HttpServletResponse response) throws IOException {
        String successUrl = appConfig.getBaseUrl() + "/payment/result";
        String cancelUrl = appConfig.getBaseUrl() + "/payment/result";
        try {
            String url = paymentService.createPaymentLink(orderId, successUrl, cancelUrl);
            response.addHeader("Location", url);
            response.setStatus(HttpStatus.FOUND.value());
        } catch (Exception e) {
            log.error("Error when creating payment link: {}", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
        }
    }

    @GetMapping("/result")
    public String success(@RequestParam("id") String id,
                          @RequestParam("cancel") Boolean cancel) throws Exception {
        Long orderId = paymentService.updatePaymentStatus(id);
        return "redirect:/order/" + orderId;
    }
}
