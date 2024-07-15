package com.huuloc.bookstore.bbook.entity;

import com.huuloc.bookstore.bbook.entity.common.BaseEntity;
import com.huuloc.bookstore.bbook.entity.enums.Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", unique = true)
    private String username;
    private String password;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Address> addresses;
    @Column(name = "name")
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    @NotBlank(message = "Ngày sinh không được để trống")
    private String birthday;
    private String status;
    private boolean enabled;
    private boolean isUsing2FA;
    private String secret;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private Image avatar;
}
