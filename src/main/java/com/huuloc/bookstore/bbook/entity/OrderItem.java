package com.huuloc.bookstore.bbook.entity;

import jakarta.persistence.*;

@Entity(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long quantity;

    private Double price;

    private Double totalPrice;
}
