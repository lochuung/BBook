package com.huuloc.bookstore.bbook.controller.admin;

import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    private BookService bookService;

    @GetMapping({"", "/"})
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "admin/product/list";
    }

    @GetMapping("/add")
    public String add() {
        return "admin/product/add";
    }

    @GetMapping("/updateQuantity/{id}")
    public String updateQuantity(Model model,
                                 @PathVariable("id") Long id) {
        model.addAttribute("book", bookService.findById(id));
        return "admin/product/updateQuantity";
    }

    @PostMapping("/updateQuantity")
    public ModelAndView updateQuantity(Book book) {
        return null;
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                       @PathVariable("id") Long id) {
        model.addAttribute("book", bookService.findById(id));
        return "admin/product/edit";
    }

    @PostMapping("/edit")
    public ModelAndView edit(Book book) {
        return null;
    }
}
