package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.dto.RegisterDto;
import com.huuloc.bookstore.bbook.entity.Address;
import com.huuloc.bookstore.bbook.entity.User;
import com.huuloc.bookstore.bbook.service.auth.CustomOAuth2User;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface UserService {
    void createTestingData();

    void processOAuthPostLogin(CustomOAuth2User customOAuth2User) throws MalformedURLException;

    Address getDefaultAddress();

    void saveAddress(Address address);

    void register(RegisterDto registerDto);

    User getCurrentUser();

    void updateProfile(User user);

    void uploadProfilePicture(MultipartFile file);
}
