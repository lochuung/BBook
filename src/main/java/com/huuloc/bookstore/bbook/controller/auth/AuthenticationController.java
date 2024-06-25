package com.huuloc.bookstore.bbook.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    @GetMapping({"/login"})
    public String getLoginPage(@RequestParam(name = "error", required = false) String error,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Đăng nhập không thành công!");
        }
        return "login";
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
