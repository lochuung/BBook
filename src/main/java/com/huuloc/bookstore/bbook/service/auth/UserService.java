package com.huuloc.bookstore.bbook.service.auth;

import com.huuloc.bookstore.bbook.entity.Address;

public interface UserService {
    void createTestingData();

    void processOAuthPostLogin(CustomOAuth2User customOAuth2User);

    Address getDefaultAddress();

    void saveAddress(Address address);
}
