package com.huuloc.bookstore.bbook.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping({"", "/"})
    public String getHomePage() {
        return "Hello World!<br /><a href=\"/logout\">Logout</a>";
    }
}
