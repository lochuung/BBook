package com.huuloc.bookstore.bbook.config;

import net.minidev.json.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    JSONParser jsonParser() {
        return new JSONParser();
    }
}
