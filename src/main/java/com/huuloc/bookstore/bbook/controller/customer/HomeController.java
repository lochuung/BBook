package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.dto.TopBooksDto;
import com.huuloc.bookstore.bbook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private BookService bookService;

    private static final int LIMIT = 10;


    @GetMapping({"", "/"})
    public String getHomePage(Model model) {
        TopBooksDto topBooksDto = bookService.getTopBooks(LIMIT);
        model.addAttribute("topRateBooks", topBooksDto.getTopRateBooks());
        model.addAttribute("topNewBooks", topBooksDto.getTopNewBooks());
        model.addAttribute("topSellBooks", topBooksDto.getTopSellBooks());
        return "customer/index";
    }
}
