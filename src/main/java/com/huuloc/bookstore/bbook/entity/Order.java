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
        if (this.state == null)
            this.state = OrderState.PENDING;

        if (this.paymentType == null)
            this.paymentType = PaymentType.COD;

        if (this.totalPrice == null)
            this.totalPrice = 0.0;

        if (this.totalPriceAfterDiscount == null)
            this.totalPriceAfterDiscount = 0.0;

        if (this.shippingPrice == null)
            this.shippingPrice = 0.0;
    }

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

    @Column(name = "discount")
    private Double discount;

    @Column(name = "total_price_after_discount")
    private Double totalPriceAfterDiscount;

    @Column(name = "shipping_price")
    private Double shippingPrice;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "order")
    private java.util.Collection<OnlinePayment> onlinePayments;

}
