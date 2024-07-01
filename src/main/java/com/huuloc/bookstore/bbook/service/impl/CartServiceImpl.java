package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.dto.cart.AddCartRequest;
import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.OrderItem;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import com.huuloc.bookstore.bbook.exception.BadRequestException;
import com.huuloc.bookstore.bbook.repository.BookRepository;
import com.huuloc.bookstore.bbook.repository.OrderItemRepository;
import com.huuloc.bookstore.bbook.repository.OrderRepository;
import com.huuloc.bookstore.bbook.service.CartService;
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
    public void checkout(Order order) {

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
