package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.entity.Privilege;
import com.huuloc.bookstore.bbook.entity.Role;
import com.huuloc.bookstore.bbook.entity.User;
import com.huuloc.bookstore.bbook.entity.enums.Provider;
import com.huuloc.bookstore.bbook.repository.PrivilegeRepository;
import com.huuloc.bookstore.bbook.repository.RoleRepository;
import com.huuloc.bookstore.bbook.repository.UserRepository;
import com.huuloc.bookstore.bbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void createTestingData() {
        if (userRepository.count() > 0) {
            return;
        }
        Privilege privilege1 = privilegeRepository.save(Privilege.builder().name("read").build());
        Privilege privilege2 = privilegeRepository.save(Privilege.builder().name("write").build());
        Privilege privilege3 = privilegeRepository.save(Privilege.builder().name("edit").build());

        Role roleUser = roleRepository.save(Role.builder().name("USER")
                .privileges(Collections.singletonList(privilege1)).build());
        Role roleAdmin = roleRepository.save(Role.builder().name("ADMIN")
                .privileges(Collections.singletonList(privilege2)).build());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userRepository.save(User.builder().username("user").email("user@cnj.vn").password(passwordEncoder.encode("123")).roles(Collections.singletonList(roleUser)).build());

        userRepository.save(User.builder().username("admin").email("admin@cnj.vn").password(passwordEncoder.encode("123")).roles(Collections.singletonList(roleAdmin)).build());
    }

    @Override
    public void processOAuthPostLogin(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            // add new user
            Role roleUser = roleRepository.findByName("USER").orElseThrow(
                    () -> new RuntimeException("Role not found")
            );
            User newUser = User.builder().email(email)
                    .roles(Collections.singletonList(roleUser))
                    .provider(Provider.OAUTH2)
                    .enabled(true)

                    .build();
            userRepository.save(newUser);
            return;
        }
    }
}
