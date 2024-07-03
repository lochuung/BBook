package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import com.huuloc.bookstore.bbook.entity.enums.OrderState;
import com.huuloc.bookstore.bbook.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @PrePersist
    public void prePersist() {
        if (this.orderItems == null)
            this.orderItems = new java.util.ArrayList<>();

        if (this.state == null)
            this.state = OrderState.NEW;

        if (this.paymentType == null)
            this.paymentType = PaymentType.COD;

        if (this.totalPrice == null)
            this.totalPrice = 0.0;

        if (this.subtotal == null)
            this.subtotal = 0.0;

        if (this.shippingPrice == null)
            this.shippingPrice = 0.0;

        if (this.discount == null)
            this.discount = 0.0;

        if (this.quantity == null)
            this.quantity = 0L;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @OneToMany(mappedBy = "order",
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.REMOVE,
                    CascadeType.DETACH
            })
    private java.util.List<OrderItem> orderItems;

    private Long quantity;

    private String note;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "shipping_price")
    private Double shippingPrice;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "order")
    private java.util.Collection<OnlinePayment> onlinePayments;

}
