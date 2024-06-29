package com.huuloc.bookstore.bbook.controller;

import com.huuloc.bookstore.bbook.entity.Genre;
import com.huuloc.bookstore.bbook.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private GenreService genreService;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.baseHostUrl:http://localhost}")
    private String baseHostUrl;

    // add model attribute for all request
    @ModelAttribute("genres")
    public List<Genre> getGenres() {
        return genreService.findAll();
    }

    @ModelAttribute("baseUrl")
    public String getBaseUrl() {
        return baseHostUrl + ":" + serverPort;
    }

}
