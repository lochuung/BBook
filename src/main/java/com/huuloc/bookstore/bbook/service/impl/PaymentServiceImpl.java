package com.huuloc.bookstore.bbook.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.huuloc.bookstore.bbook.entity.OnlinePayment;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import com.huuloc.bookstore.bbook.entity.enums.PaymentStatus;
import com.huuloc.bookstore.bbook.repository.OrderRepository;
import com.huuloc.bookstore.bbook.repository.PaymentRepository;
import com.huuloc.bookstore.bbook.service.PaymentService;
import com.lib.payos.PayOS;
import com.lib.payos.type.ItemData;
import com.lib.payos.type.PaymentData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PayOS payOS;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Retryable(retryFor = Exception.class, maxAttempts = 5)
    public String createPaymentLink(Long orderId, String returnUrl, String cancelUrl) throws Exception {
        Order order = getOrder(orderId);
        validateOrderState(order);
        OnlinePayment payment = order.getOnlinePayments().stream()
                .filter(onlinePayment -> onlinePayment.getStatus() == PaymentStatus.PENDING)
                .findFirst().orElse(null);
        if (payment != null) {
            return payOS.getPaymentLinkInfomation(Integer
                            .parseInt(payment.getOrderCode()))
                    .get("checkoutUrl").asText();
        }
        List<ItemData> itemData = createItemData(order);
        PaymentData paymentData = createPaymentData(order, itemData, cancelUrl, returnUrl);
        JsonNode response = payOS.createPaymentLink(paymentData);
        saveOnlinePayment(order, response);
        return response.get("checkoutUrl").asText();
    }

    @Override
    @Transactional
    public Long updatePaymentStatus(String id) throws Exception {
        OnlinePayment onlinePayment = paymentRepository.findByPaymentId(id).orElse(null);
        if (onlinePayment == null) {
            return null;
        }
        JsonNode info =
                payOS.getPaymentLinkInfomation(Integer.parseInt(onlinePayment.getOrderCode()));
        String status = info.get("status").asText();
        if (PaymentStatus.PAID.name().equals(status)) {
            onlinePayment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(onlinePayment);
            Order order = onlinePayment.getOrder();
            order.setState(OrderState.PENDING);
            orderRepository.save(order);
        } else if (PaymentStatus.CANCELLED.name().equals(status)) {
            onlinePayment.setStatus(PaymentStatus.CANCELLED);
        }
        paymentRepository.save(onlinePayment);
        return onlinePayment.getOrder().getId();
    }

    private Order getOrder(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found"));
    }

    private void validateOrderState(Order order) throws Exception {
        if (order.getState() != OrderState.PAYMENT) {
            throw new Exception("Order is not in payment state");
        }
    }

    private List<ItemData> createItemData(Order order) {
        return order.getOrderItems().stream().map(orderItem -> new ItemData(orderItem.getBook().getTitle(), orderItem.getQuantity().intValue(), orderItem.getBook().getPrice().intValue())).toList();
    }

    private PaymentData createPaymentData(Order order, List<ItemData> itemData, String cancelUrl, String returnUrl) {
        int randomId = (int) (Math.random() * Integer.MAX_VALUE);
        return new PaymentData(randomId, order.getTotalPrice().intValue(), "", itemData, cancelUrl, returnUrl);
    }

    private void saveOnlinePayment(Order order, JsonNode response) {
        paymentRepository.save(OnlinePayment.builder().order(order)
                .orderCode(response.get("orderCode").asText())
                .paymentId(response.get("paymentLinkId").asText()).status(PaymentStatus.PENDING).build());
    }
}
