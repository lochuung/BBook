package com.huuloc.bookstore.bbook.config;

import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableRetry
public class AppConfig {
    @Value("${server.base-url:http://localhost:8081}")
    private String baseHostUrl;


    @Bean
    JSONParser jsonParser() {
        return new JSONParser();
    }

    public String getBaseUrl() {
        return baseHostUrl;
    }
}
