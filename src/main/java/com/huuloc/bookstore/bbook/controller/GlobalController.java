package com.huuloc.bookstore.bbook.controller;

import com.huuloc.bookstore.bbook.config.AppConfig;
import com.huuloc.bookstore.bbook.entity.Genre;
import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.entity.User;
import com.huuloc.bookstore.bbook.service.GenreService;
import com.huuloc.bookstore.bbook.service.OrderService;
import com.huuloc.bookstore.bbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private GenreService genreService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Autowired
    private AppConfig appConfig;

    // add model attribute for all request
    @ModelAttribute("genres")
    public List<Genre> getGenres() {
        return genreService.findAll();
    }

    @ModelAttribute("baseUrl")
    public String getBaseUrl() {
        return appConfig.getBaseUrl();
    }

    @ModelAttribute("newOrder")
    public Order getNewOrder() {
        return orderService.getNewOrder();
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}