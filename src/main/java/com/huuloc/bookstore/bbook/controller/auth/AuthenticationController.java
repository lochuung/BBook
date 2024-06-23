package com.huuloc.bookstore.bbook.controller.auth;

import com.huuloc.bookstore.bbook.controller.auth.dto.AuthenticationResponse;
import com.huuloc.bookstore.bbook.controller.auth.dto.LoginRequest;
import com.huuloc.bookstore.bbook.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request,
                                                               HttpServletResponse response) {
        String refreshToken = request.getHeader("Authorization")
                .replace("Bearer ", "");
        return ResponseEntity.ok(authenticationService.refresh(refreshToken));
    }
}
