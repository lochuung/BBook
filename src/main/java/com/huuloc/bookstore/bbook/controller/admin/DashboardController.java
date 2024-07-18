package com.huuloc.bookstore.bbook.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @RequestMapping({"", "/"})
    public String index() {
        return "redirect:/admin/order";
    }
}
