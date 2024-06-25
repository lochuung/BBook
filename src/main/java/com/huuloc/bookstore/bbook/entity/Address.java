package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "addresses")
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phoneNumber;

    private String province;
    private String district;
    private String ward;

    private String address;

    @Column(name = "is_default", columnDefinition = "boolean default false")
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "address")
    private Order order;
}
