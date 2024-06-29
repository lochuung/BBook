package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.entity.Image;
import com.huuloc.bookstore.bbook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/{slug}")
    public String getBookDetail(Model model,
                                @PathVariable("slug") String slug) {
        Book book = bookService.findBySlug(slug);
        // images array string
        List<String> images = new ArrayList<>();
        images.add("\"" + book.getThumbnailUrl() + "\"");

        for (Image image : book.getImages()) {
            images.add("\"" + image.getUrl() + "\"");
        }
        String imagesString = "[" + String.join(",", images) + "]";
        model.addAttribute("images", imagesString);
        model.addAttribute("book", book);
        return "customer/book-detail";
    }
}
