package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.dto.RegisterDto;
import com.huuloc.bookstore.bbook.entity.*;
import com.huuloc.bookstore.bbook.entity.enums.Provider;
import com.huuloc.bookstore.bbook.repository.AddressRepository;
import com.huuloc.bookstore.bbook.repository.PrivilegeRepository;
import com.huuloc.bookstore.bbook.repository.RoleRepository;
import com.huuloc.bookstore.bbook.repository.UserRepository;
import com.huuloc.bookstore.bbook.service.ImageService;
import com.huuloc.bookstore.bbook.service.auth.CustomOAuth2User;
import com.huuloc.bookstore.bbook.service.UserService;
import com.huuloc.bookstore.bbook.util.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.huuloc.bookstore.bbook.util.ImageUtils.getBytesFromUrl;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ImageService imageService;

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
        userRepository.save(User.builder().username("user").email("user@gmail.com").password(passwordEncoder.encode("123")).roles(Collections.singletonList(roleUser)).build());

        userRepository.save(User.builder().username("admin").email("admin@gmail.com").password(passwordEncoder.encode("123")).roles(Collections.singletonList(roleAdmin)).build());
    }

    @Override
    @Transactional
    public void processOAuthPostLogin(CustomOAuth2User customOAuth2User) throws MalformedURLException {
        String email = customOAuth2User.getEmail();
        String name = customOAuth2User.getName();
        String picture = customOAuth2User.getPicture();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            // add new user
            Role roleUser = roleRepository.findByName("USER").orElseThrow(
                    () -> new RuntimeException("Role not found")
            );
            byte[] avatar = getBytesFromUrl(picture);

            Image image = imageService.upload(avatar, email + ".jpg");

            User newUser = User.builder().email(email)
                    .roles(Collections.singletonList(roleUser))
                    .fullName(name)
                    .provider(Provider.OAUTH2)
                    .birthday(LocalDate.now().toString())
                    .avatar(image)
                    .enabled(true)
                    .build();
            userRepository.save(newUser);
        }
    }

    @Override
    public Address getDefaultAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = AuthUtils.getEmailFromAuthentication(authentication);
        Optional<User> user = userRepository.findByEmail(email);
        return user.stream()
                .flatMap(u -> u.getAddresses().stream())
                .filter(Address::getIsDefault)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveAddress(Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = AuthUtils.getEmailFromAuthentication(authentication);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return;
        }
        User u = user.get();
        address.setUser(u);
        address.setIsDefault(true);
        addressRepository.save(address);
    }

    @Override
    public void register(RegisterDto registerDto) {
        String email = registerDto.getEmail();
        String password = registerDto.getPassword();
        String fullName = registerDto.getFullName();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Role roleUser = roleRepository.findByName("USER").orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        // check email exist
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại");
        }
        User newUser = User.builder().email(email)
                .roles(Collections.singletonList(roleUser))
                .fullName(fullName)
                .provider(Provider.LOCAL)
                .enabled(true)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(newUser);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = AuthUtils.getEmailFromAuthentication(authentication);
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void updateProfile(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = AuthUtils.getEmailFromAuthentication(authentication);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return;
        }
        User u = userOptional.get();
        u.setFullName(user.getFullName());
        u.setPhoneNumber(user.getPhoneNumber());
//        u.setEmail(user.getEmail());
        u.setBirthday(user.getBirthday());
        userRepository.save(u);
    }

    @Override
    @Transactional
    public void uploadProfilePicture(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = AuthUtils.getEmailFromAuthentication(authentication);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return;
        }
        User user = userOptional.get();
        user.setAvatar(imageService.upload(file));
        userRepository.save(user);
    }
}
