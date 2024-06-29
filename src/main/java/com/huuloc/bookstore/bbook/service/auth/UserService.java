package com.huuloc.bookstore.bbook.service.auth;

public interface UserService {
    void createTestingData();

    void processOAuthPostLogin(CustomOAuth2User customOAuth2User);
}
