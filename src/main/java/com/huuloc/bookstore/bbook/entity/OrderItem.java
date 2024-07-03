package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "order_items")
public class OrderItem extends BaseEntity {
    @PrePersist
    public void prePersist() {
        if (this.quantity == null)
            this.quantity = 0L;

        if (this.price == null)
            this.price = 0.0;

        if (this.totalPrice == null)
            this.totalPrice = 0.0;

        if (this.order != null) {
            if (!this.order.getOrderItems().contains(this))
                this.order.getOrderItems().add(this);
            updateOrderDetail();
        }
    }

    @PreRemove
    public void preRemove() {
        if (this.order != null) {
            this.order.getOrderItems().remove(this);
            updateOrderDetail();
            this.order = null;
        }
    }

    private void updateOrderDetail() {
        Long quantity = order.getOrderItems().stream()
                .map(OrderItem::getQuantity)
                .reduce(0L, Long::sum);
        order.setQuantity(quantity);
        order.setSubtotal(order.getOrderItems().stream()
                .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum());
        order.setTotalPrice(order.getSubtotal() + order.getShippingPrice() - order.getDiscount());
    }

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long quantity;

    private Double price;

    private Double totalPrice;
}
