package com.huuloc.bookstore.bbook.config;

import com.huuloc.bookstore.bbook.service.auth.CustomOAuth2User;
import com.huuloc.bookstore.bbook.service.auth.UserService;
import com.huuloc.bookstore.bbook.service.auth.CustomOAuth2UserService;
import com.huuloc.bookstore.bbook.service.auth.CustomUserDetailsService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private UserService userService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RSAKey rsaKey) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/anonymous*")
                        .anonymous()
                        .requestMatchers("/admin**", "/cart/**", "/cart",
                                "/order/**")
                        .authenticated()
                        .anyRequest()
                        .permitAll())
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/login?error=true"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID"));

        http.csrf(AbstractHttpConfigurer::disable);

        http.oauth2Login(oauth2Login -> oauth2Login.loginPage("/login")
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                        .userService(customOAuth2UserService))
                .successHandler((request, response, authentication) -> {

                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

                    userService.processOAuthPostLogin(oauthUser);

                    response.sendRedirect("/");
                })
        );

        http.rememberMe(rememberMe -> {
            try {
                rememberMe.key(String.valueOf(rsaKey.toRSAPrivateKey()))
                        .tokenValiditySeconds(86400)
                        .rememberMeParameter("remember-me");
            } catch (JOSEException e) {
                throw new RuntimeException(e);
            }
        });
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Bean
    RSAKey rsaKey(KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .build();
    }

//    @Bean
//    JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
//        return (jwkSelector, securityContext) -> jwkSelector.select(
//                new JWKSet(rsaKey)
//        );
//    }
//
//    @Bean
//    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
//        return new NimbusJwtEncoder(jwkSource);
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder(KeyPair keyPair) {
//        return NimbusJwtDecoder
//                .withPublicKey((RSAPublicKey) keyPair.getPublic())
//                .build();
//    }
}
