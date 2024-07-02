package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "addresses")
public class Address extends BaseEntity {
    @PrePersist
    public void prePersist() {
        if (this.isDefault == null)
            this.isDefault = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Tên không được để trống")
    private String fullName;
    @NotEmpty(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;
    @NotEmpty(message = "Tỉnh/Thành phố không được để trống")
    private String province;
    @NotEmpty(message = "Quận/Huyện không được để trống")
    private String district;
    @NotEmpty(message = "Phường/Xã không được để trống")
    private String ward;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String addressLine;

    @Column(name = "is_default", columnDefinition = "boolean default false")
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "addresses")
    private List<Order> order;
}
