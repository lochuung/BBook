package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @OneToMany(mappedBy = "order")
    private java.util.Collection<OrderItem> orderItems;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "total_price_after_discount")
    private Double totalPriceAfterDiscount;

}
