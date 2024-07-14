package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import com.huuloc.bookstore.bbook.entity.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "coupons")
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String description;
    private Double value;
    private DiscountType type;
    private LocalDateTime expiredDate;
    private Double maxOrderValue;
    private Double minOrderValue;
    private Integer maxUse;
    private Integer currentUse;
    @Version
    private Long version;

    @OneToMany(mappedBy = "coupon")
    private Collection<Order> orders;
}
