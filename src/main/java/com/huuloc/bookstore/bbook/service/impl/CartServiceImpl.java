package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.constant.AppConstants;
import com.huuloc.bookstore.bbook.dto.cart.AddCartRequest;
import com.huuloc.bookstore.bbook.entity.Address;
import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.OrderItem;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import com.huuloc.bookstore.bbook.entity.enums.PaymentType;
import com.huuloc.bookstore.bbook.exception.BadRequestException;
import com.huuloc.bookstore.bbook.repository.BookRepository;
import com.huuloc.bookstore.bbook.repository.OrderItemRepository;
import com.huuloc.bookstore.bbook.repository.OrderRepository;
import com.huuloc.bookstore.bbook.service.CartService;
import com.huuloc.bookstore.bbook.service.PaymentService;
import com.huuloc.bookstore.bbook.service.auth.UserService;
import com.huuloc.bookstore.bbook.util.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Override
    @Transactional
    public void addToCart(AddCartRequest addCartRequest) {
        Order order = getOrder(addCartRequest.getOrderId());

        order.getOrderItems().stream()
                .filter(orderItem -> isSameBook(orderItem, addCartRequest.getBookId()))
                .findFirst()
                .ifPresentOrElse(orderItem -> updateOrderItem(orderItem, addCartRequest),
                        () -> createOrderItem(order, addCartRequest));
    }

    @Override
    @Transactional
    public void removeCartItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> BadRequestException.message("Không tìm thấy sách trong giỏ hàng"));
        Order order = orderItem.getOrder();
        checkOrderState(order);

        orderItemRepository.delete(orderItem);
    }

    @Override
    @Transactional
    public Order checkout(PaymentType paymentType) {
        String email = AuthUtils.getEmailFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
        Order order = orderRepository.findByUserEmailAndState(email, OrderState.NEW);
        checkOrderState(order);
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw BadRequestException.message("Giỏ hàng trống");
        }
        Address address = userService.getDefaultAddress();
        if (address == null) {
            throw BadRequestException.message("Vui lòng cập nhật địa chỉ giao hàng");
        }
        order.setAddresses(address);
        order.setPaymentType(paymentType);
        for (OrderItem item : order.getOrderItems()) {
            Book book = bookRepository.findByIdWithLock(item.getBook().getId());
            if (item.getQuantity() > book.getAvailableQuantity()) {
                throw BadRequestException.message("Không đủ số lượng sách");
            }
            book.setAvailableQuantity(book.getAvailableQuantity() - item.getQuantity());
            book.setTotalSold(book.getTotalSold() + item.getQuantity());
            item.setBook(book);
        }
        if (order.getPaymentType() == PaymentType.COD)
            order.setState(OrderState.PENDING);
        else {
            order.setState(OrderState.PAYMENT);
            // schedule if after 15 minutes, the order is still in PAYMENT state, change order to CANCEL
            Runnable task = new Runnable() {
                @Override
                @Transactional
                public void run() {
                    try {
                        Thread.sleep(AppConstants.PAYMENT_EXPIRATION * 60 * 1000);
                        Order order1 = orderRepository.findById(
                                        order.getId())
                                .orElse(null);
                        if (order1 != null && order1.getState() == OrderState.PAYMENT) {
                            order1.setState(OrderState.CANCELLED);

                            // update book quantity
                            for (OrderItem item : order1.getOrderItems()) {
                                Book book = bookRepository.findByIdWithLock(item.getBook().getId());
                                book.setAvailableQuantity(book.getAvailableQuantity() + item.getQuantity());
                                book.setTotalSold(book.getTotalSold() - item.getQuantity());
                                item.setBook(book);
                            }

                            orderRepository.save(order1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(task).start();

        }
        return orderRepository.save(order);
    }

    private Order getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> BadRequestException.message("Không tìm thấy đơn hàng"));
        checkOrderState(order);
        return order;
    }

    private void checkOrderState(Order order) {
        if (order.getState() != OrderState.NEW) {
            throw BadRequestException.message("Đơn hàng đã được xác nhận hoặc hủy bỏ");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = AuthUtils.getEmailFromAuthentication(authentication);
        if (!Objects.equals(order.getUser().getEmail(), email)) {
            throw BadRequestException.message("Không tìm thấy đơn hàng");
        }
    }

    private boolean isSameBook(OrderItem orderItem, Long bookId) {
        return Objects.equals(orderItem.getBook().getId(), bookId);
    }

    private void updateOrderItem(OrderItem orderItem, AddCartRequest addCartRequest) {
        if (orderItem.getQuantity() + addCartRequest.getQuantity()
                > orderItem.getBook().getAvailableQuantity()) {
            throw BadRequestException.message("Không đủ số lượng sách");
        }
        orderItem.setQuantity(orderItem.getQuantity() + addCartRequest.getQuantity());
        orderItemRepository.save(orderItem);
    }

    private void createOrderItem(Order order, AddCartRequest addCartRequest) {
        Book book = bookRepository.findById(addCartRequest.getBookId())
                .orElseThrow(() -> BadRequestException.message("Không tìm thấy sách"));
        if (addCartRequest.getQuantity() > book.getAvailableQuantity()) {
            throw BadRequestException.message("Không đủ số lượng sách");
        }
        orderItemRepository.save(
                OrderItem.builder()
                        .order(order)
                        .book(book)
                        .quantity(addCartRequest.getQuantity())
                        .price(book.getPrice())
                        .totalPrice(book.getPrice() * addCartRequest.getQuantity())
                        .build()
        );
    }
}
