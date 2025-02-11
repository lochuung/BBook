package com.huuloc.bookstore.bbook.service.auth;

import com.huuloc.bookstore.bbook.entity.Role;
import com.huuloc.bookstore.bbook.entity.User;
import com.huuloc.bookstore.bbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found " +
                                "with username or email : " + username)
                );
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities(getAuthorities(user.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(roleName -> "ROLE_" + roleName)
                .map(roleName -> (GrantedAuthority) () -> roleName)
                .toList();
    }
}
